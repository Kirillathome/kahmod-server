package me.kirillathome.kahmod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.netty.buffer.Unpooled;
import me.kirillathome.kahmod.config.ConfigManager;
import me.kirillathome.kahmod.config.ServerConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.*;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.command.argument.ColorArgumentType.getColor;
import static net.minecraft.server.command.CommandManager.literal;

public final class CustomCommands {
    static Map<String, List<String>> blacklist = new HashMap<>();
    static ServerConfig config = ConfigManager.getServerConfig();

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
                                    .executes(context -> blacklist_get_full_response(context.getSource(), context.getArgument("target", EntitySelector.class).getPlayer(context.getSource()).getName().getString()))
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
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("config")
                        .then(CommandManager.literal("reload")
                                .executes(context -> reloadConfig(context.getSource()))
                        )
                )
        );
    }

    public static int default_response(ServerCommandSource source){
        String version = KahMod.getVersion().raw();
        source.sendFeedback(() -> Text.literal(config.getCommandResponse("rentner_smp").formatted(version)), true);
        return Command.SINGLE_SUCCESS;
    }

    public static int custom_model_data_response(ServerCommandSource source, Integer data){
        ServerPlayerEntity player = source.getPlayer();
        if (!source.isPlayer() || player == null){
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("feedback.console")), false);
            return 0;
        }
        if (is_blacklisted_for(player.getName().getString(), "cmd")){
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("feedback.forbidden")), true);
            return 0;
        }
        ItemStack playerItem = player.getMainHandStack();
        if (playerItem == ItemStack.EMPTY){
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("cmd.invalid_item")), true);
            return 2;
        }
        if (data == 0) {
            playerItem.removeSubNbt("CustomModelData");
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("cmd.reset").formatted(playerItem.getName().getString())), true);
        }
        else {
            playerItem.getOrCreateNbt().putInt("CustomModelData", data);
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("cmd.set").formatted(playerItem.getName().getString(), data)), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int blacklist_set_response(ServerCommandSource source, String target, String command, Boolean value){
        if (!blacklist.containsKey(target)){
            blacklist.put(target, new ArrayList<>());
        }
        List<String> commands = blacklist.get(target);
        if (value) {
            if (commands.contains(command)){
                source.sendFeedback(() -> Text.literal(config.getCommandResponse("blacklist.set.already_added").formatted(target, command)), false);
            }
            else{
                commands.add(command);
                blacklist.put(target, commands);
                source.sendFeedback(() -> Text.literal(config.getCommandResponse("blacklist.set.added").formatted(target, command)), true);
            }
        }
        else{
            if (!commands.contains(command)){
                source.sendFeedback(() -> Text.literal(config.getCommandResponse("blacklist.set.already_removed").formatted(target, command)), false);
            }
            else{
                commands.remove(command);
                blacklist.put(target, commands);
                source.sendFeedback(() -> Text.literal(config.getCommandResponse("blacklist.set.removed").formatted(target, command)), true);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int blacklist_get_response(ServerCommandSource source, String target, String command){
        if (is_blacklisted_for(target, command)){
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("blacklist.get.true").formatted(target, command)), false);
        }
        else{
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("blacklist.get.false").formatted(target, command)), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int blacklist_get_full_response(ServerCommandSource source, String target){
        if (blacklist.containsKey(target)){
            if (!blacklist.get(target).isEmpty()){
                source.sendFeedback(() -> Text.literal(config.getCommandResponse("blacklist.get.full").formatted(target, blacklist.get(target).toString())), false);
                return 1;
            }
        }
        source.sendFeedback(() -> Text.literal(config.getCommandResponse("blacklist.get.full.fail").formatted(target)), false);
        return Command.SINGLE_SUCCESS;
    }
    public static boolean is_blacklisted_for(String player, String command){
        if (blacklist.containsKey(player)){
            List<String> commands = blacklist.get(player);
            return commands.contains(command);
        }
        return false;
    }

    public static void registerMaulCommand(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager.literal("maul")
                .then(CommandManager.argument("target", StringArgumentType.string())
                        .suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
                        .then(CommandManager.argument("color", ColorArgumentType.color())
                                .executes(context -> maul_response(
                                        context.getSource(),
                                        getString(context, "target"),
                                        getColor(context, "color")
                                        )
                                )
                        )
                        .executes(context -> maul_response(
                                context.getSource(),
                                getString(context, "target"),
                                Formatting.WHITE
                        )
                        )
                )
        );
    }
    public static int maul_response(ServerCommandSource source, String target, Formatting formatting){
        ServerPlayerEntity player = source.getPlayer();
        if (player != null && is_blacklisted_for(player.getName().getString(), "maul") || player != null && target.toLowerCase(Locale.ROOT).contains("kiril") || player != null && target.toLowerCase(Locale.ROOT).contains("jens")){
           Text title = Text.literal("%s ist kein Gewinner!".formatted(player.getName().getString())).formatted(formatting);
           TitleS2CPacket packet = new TitleS2CPacket(title);
           for (ServerPlayerEntity players : source.getServer().getPlayerManager().getPlayerList()){
               players.networkHandler.sendPacket(packet);
           }
        }
        else {
            Text title = Text.literal("Maul %s".formatted(target)).formatted(formatting);
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
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("feedback.console")), false);
            return 0;
        }
        if (is_blacklisted_for(player.getName().getString(), "status")){
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("feedback.forbidden")), true);
            return 0;
        }
        if (status.length() > 12){
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("status.long")), true);
            return 1;
        }
        String player_name = Objects.requireNonNull(source.getPlayer()).getName().getString();
        if (source.getServer().getScoreboard().getTeam(player_name) == null){
            source.getServer().getScoreboard().addTeam(player_name);
        }
        Team player_team = source.getServer().getScoreboard().getTeam(player_name);
        source.getServer().getScoreboard().addPlayerToTeam(player_name, player_team);
        if (player_team == null){
            return 1;
        }
        player_team.setPrefix(Text.literal("["+status+"] ").formatted(formatting));
        source.sendFeedback(() -> Text.literal(config.getCommandResponse("status.set")).append(Text.literal("["+status+"]").formatted(formatting)), true);
        return Command.SINGLE_SUCCESS;
    }
    public static int resetStatus(ServerCommandSource source){
        ServerPlayerEntity player = source.getPlayer();
        if (!source.isPlayer() || player == null){
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("feedback.console")), false);
            return 0;
        }
        if (is_blacklisted_for(player.getName().getString(), "status")){
            source.sendFeedback(() -> Text.literal(config.getCommandResponse("feedback.forbidden")), true);
            return 0;
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
        player_team.setPrefix(Text.empty().formatted(Formatting.RESET));
        source.sendFeedback(() -> Text.literal(config.getCommandResponse("status.reset")), true);
        return Command.SINGLE_SUCCESS;
    }
    public static int reloadConfig(ServerCommandSource source){
        ConfigManager.reloadServerConfig();
        config = ConfigManager.getServerConfig();
        for (ServerPlayerEntity player : source.getWorld().getPlayers()){
            player.networkHandler.sendPacket(new CustomPayloadS2CPacket(CustomPayloadS2CPacket.BRAND, new PacketByteBuf(Unpooled.buffer())));
        }
        source.sendFeedback(() -> Text.literal("Successfully reloaded config!"), true);
        return Command.SINGLE_SUCCESS;
    }
}
