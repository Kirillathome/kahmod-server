package me.kirillathome.kahmod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.command.argument.ColorArgumentType.getColor;
import static net.minecraft.server.command.CommandManager.literal;

public final class CustomCommands {

    static Map<String, String[]> blacklist = new HashMap<>();
    public static void registerRSMPCommand(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager.literal("rentnersmp")
                .executes(context -> default_response(context.getSource()))
                .then(CommandManager.literal("cmd")
                        .then(CommandManager.argument("data", IntegerArgumentType.integer())
                                .executes(context -> custom_model_data_response(
                                        context.getSource(),
                                        context.getArgument("data", Integer.class)
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("blacklist")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("get")
                            .then(CommandManager.argument("target", EntityArgumentType.player())
                                    .then(CommandManager.argument("command", StringArgumentType.string())
                                            .suggests((context, builder) -> CommandSource.suggestMatching(new String[]{"cmd", "status", "maul"}, builder))
                                            .executes(context -> blacklist_get_response(
                                                    context.getSource(),
                                                    context.getArgument("target", EntitySelector.class).getPlayer(context.getSource()).getName().getString(),
                                                    getString(context, "command")
                                                    )
                                            )
                                    )
                            )
                        )
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .then(CommandManager.argument("command", StringArgumentType.string())
                                                .suggests((context, builder) -> CommandSource.suggestMatching(new String[]{"cmd", "status", "maul"}, builder))
                                                .then(CommandManager.argument("value", BoolArgumentType.bool())
                                                        .executes(context -> blacklist_set_response(
                                                                context.getSource(),
                                                                context.getArgument("target", EntitySelector.class).getPlayer(context.getSource()).getName().getString(),
                                                                getString(context, "command"),
                                                                context.getArgument("value", Boolean.class)
                                                                )
                                                        )
                                                )
                                                .executes(context -> blacklist_set_response(
                                                                context.getSource(),
                                                                context.getArgument("target", EntitySelector.class).getPlayer(context.getSource()).getName().getString(),
                                                                getString(context, "command"),
                                                                true
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public static int default_response(ServerCommandSource source){
        String version = KahMod.getVersion().raw();
        source.sendFeedback(() -> Text.literal("Der RentnerSMP von Kirill.\nMod version: ".concat(version)), true);
        return Command.SINGLE_SUCCESS;
    }

    public static int custom_model_data_response(ServerCommandSource source, Integer data){
        ServerPlayerEntity player = source.getPlayer();
        if (!source.isPlayer() || player == null){
            KahMod.LOGGER.info("Can't execute this command from the console!");
            return 1;
        }
        if (is_blacklisted_for(player.getName().getString(), "cmd")){
            player.sendMessage(Text.literal("You are not allowed to use this command!"), false);
            return 3;
        }
        ItemStack playerItem = player.getMainHandStack();
        if (playerItem == ItemStack.EMPTY){
            source.sendFeedback(() -> Text.literal("Can't add NBT to Air!"), true);
            return 2;
        }
        if (data == 0) {
            playerItem.removeSubNbt("CustomModelData");
            source.sendFeedback(() -> Text.literal("Successfully reset the CustomModelData of your ".concat(playerItem.getName().getString().concat("!"))), true);
        }
        else {
            playerItem.getOrCreateNbt().putInt("CustomModelData", data);
            source.sendFeedback(() -> Text.literal("Successfully set the CustomModelData of your ".concat(playerItem.getName().getString().concat(" to ").concat(data.toString()).concat("!"))), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int blacklist_set_response(ServerCommandSource source, String target, String command, Boolean value){
        KahMod.LOGGER.info("Blacklisting ".concat(target).concat(" from ").concat(command));
        String added_response = "Successfully blacklisted ".concat(target).concat(" from using ").concat(command).concat("!");
        String removed_response = "Successfully allowed ".concat(target).concat(" to use ").concat(command).concat("!");
        String already_blacklisted_response = target.concat(" is already blacklisted from using ").concat(command).concat("!");
        String already_allowed_response = target.concat(" is already allowed to use ").concat(command).concat("!");
        if (!blacklist.containsKey(target)){
            blacklist.put(target, new String[]{});
        }
        List<String> commands = new ArrayList<>(Arrays.asList(blacklist.get(target)));
        KahMod.LOGGER.info(commands.toString());
        if (value) {
            if (commands.contains(command)){
                source.sendFeedback(() -> Text.literal(already_blacklisted_response), true);
            }
            else{
                commands.add(command);
                String[] new_commands = commands.toArray(new String[1]);
                blacklist.put(target, new_commands);
                source.sendFeedback(() -> Text.literal(added_response), true);
            }
        }
        else{
            if (!commands.contains(command)){
                source.sendFeedback(() -> Text.literal(already_allowed_response), true);
            }
            else{
                commands.remove(command);
                String[] new_commands = commands.toArray(new String[0]);
                blacklist.put(target, new_commands);
                source.sendFeedback(() -> Text.literal(removed_response), true);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int blacklist_get_response(ServerCommandSource source, String target, String command){
        boolean value = is_blacklisted_for(target, command);
        String is_blacklisted = target.concat(" is blacklisted for ").concat(command).concat("!");
        String is_not_blacklisted = target.concat(" is not blacklisted for ").concat(command).concat("!");
        if (value){
            source.sendFeedback(() -> Text.literal(is_blacklisted), true);
        }
        else{
            source.sendFeedback(() -> Text.literal(is_not_blacklisted), true);
        }
        return Command.SINGLE_SUCCESS;
    }
    public static boolean is_blacklisted_for(String player, String command){
        if (blacklist.containsKey(player)){
            String[] commands = blacklist.get(player);
            return Arrays.asList(commands).contains(command);
        }
        return false;
    }

    public static void registerMaulCommand(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager.literal("maul")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .then(CommandManager.argument("color", ColorArgumentType.color())
                                .executes(context -> maul_response(
                                        context.getSource(),
                                        context.getArgument("target", EntitySelector.class).getPlayer(context.getSource()).getName().getString(),
                                        getColor(context, "color")
                                        )
                                )
                        )
                        .executes(context -> maul_response(
                                context.getSource(),
                                context.getArgument("target", EntitySelector.class).getPlayer(context.getSource()).getName().getString(),
                                Formatting.WHITE
                        )
                        )
                )
        );
    }
    public static int maul_response(ServerCommandSource source, String target, Formatting formatting){
        ServerPlayerEntity player = source.getPlayer();
        if (player != null && is_blacklisted_for(player.getName().getString(), "maul") || player != null && target.equals("Kirill_at_home")){
           Text title = Text.literal(player.getName().getString().concat(" ist kein Gewinner")).formatted(formatting);
           TitleS2CPacket packet = new TitleS2CPacket(title);
           for (ServerPlayerEntity players : source.getServer().getPlayerManager().getPlayerList()){
               players.networkHandler.sendPacket(packet);
           }
        }
        else {
            Text title = Text.literal("Maul ".concat(target)).formatted(formatting);
            TitleS2CPacket packet = new TitleS2CPacket(title);
            for (ServerPlayerEntity players : source.getServer().getPlayerManager().getPlayerList()){
                players.networkHandler.sendPacket(packet);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void registerStatusCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("status")
                .then(literal("reset").executes(context -> (resetStatus(context.getSource()))))
                .then(
                        CommandManager.argument("status_arg", StringArgumentType.string())
                                .then(
                                        CommandManager.argument("color_arg", ColorArgumentType.color())
                                                .executes(context -> (setStatus(context.getSource(), getString(context, "status_arg"), getColor(context, "color_arg")))))
                                .executes(context -> (setStatus(context.getSource(), getString(context, "status_arg"), Formatting.WHITE)))
                )
        );
    }
    public static int setStatus(ServerCommandSource source, String status, Formatting formatting){
        ServerPlayerEntity player = source.getPlayer();
        if (!source.isPlayer() || player == null){
            KahMod.LOGGER.info("Can't execute this command from the console!");
            source.sendFeedback(() -> Text.literal("Can't execute this command from the console!"), true);
            return 1;
        }
        if (is_blacklisted_for(player.getName().getString(), "status")){
            source.sendFeedback(() -> Text.literal("You are not allowed to use this command!"), true);
            return 3;
        }
        if (status.length() > 12){
            source.sendFeedback(() -> Text.literal("Status is too long!"), true);
            return 3;
        }
        String player_name = Objects.requireNonNull(source.getPlayer()).getName().getString();
        if (source.getServer().getScoreboard().getTeam(player_name) == null){
            source.getServer().getScoreboard().addTeam(player_name);
        }
        Team player_team = source.getServer().getScoreboard().getTeam(player_name);
        source.getServer().getScoreboard().addPlayerToTeam(player_name, player_team);
        if (player_team == null){
            return 2;
        }
        player_team.setPrefix(Text.literal("["+status+"] ").formatted(formatting));
        source.getPlayer().sendMessage(Text.literal("Successfully set your status to: ").append(Text.literal("[" + status + "]").formatted(formatting)).append("."), false);
        return Command.SINGLE_SUCCESS;
    }
    public static int resetStatus(ServerCommandSource source){
        ServerPlayerEntity player = source.getPlayer();
        if (!source.isPlayer() || player == null){
            source.sendFeedback(() -> Text.literal("Can't execute this command from the console!"), true);
            return 1;
        }
        if (is_blacklisted_for(player.getName().getString(), "status")){
            source.sendFeedback(() -> Text.literal("You are not allowed to use this command!"), true);
            return 3;
        }
        String player_name = Objects.requireNonNull(source.getPlayer()).getName().getString();
        if (source.getServer().getScoreboard().getTeam(player_name) == null){
            source.getServer().getScoreboard().addTeam(player_name);
        }
        Team player_team = source.getServer().getScoreboard().getTeam(player_name);
        source.getServer().getScoreboard().addPlayerToTeam(player_name, player_team);
        if (player_team == null){
            return 2;
        }
        player_team.setPrefix(Text.literal("").formatted(Formatting.RESET));
        source.sendFeedback(() -> Text.literal("Successfully reset your status."), true);
        return Command.SINGLE_SUCCESS;
    }

}
