package dev.kirillathome.kahmod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.kirillathome.kahmod.config.ConfigManager;
import dev.kirillathome.kahmod.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.HexColorArgument;
import net.minecraft.commands.arguments.TeamColorArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.BrandPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.TeamColor;

import java.util.*;
import java.util.concurrent.CompletableFuture;


public final class CustomCommands {
    static Map<String, List<String>> blacklist = new HashMap<>();
    static ServerConfig config = ConfigManager.getServerConfig();

    public static void registerRSMPCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("kahmod")
                .executes(context -> defaultResponse(context.getSource()))
                .then(Commands.literal("blacklist")
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .then(Commands.literal("get")
                            .then(Commands.argument("target", EntityArgument.player())
                                    .executes(context -> blacklistGetFullResponse(context.getSource(), context.getArgument("target", EntitySelector.class).findSinglePlayer(context.getSource()).getName().getString()))
                                    .then(Commands.argument("command", StringArgumentType.string())
                                            .suggests(new BlacklistSuggestionProvider())
                                            .executes(context -> blacklistGetResponse(
                                                    context.getSource(),
                                                    context.getArgument("target", EntitySelector.class).findSinglePlayer(context.getSource()).getName().getString(),
                                                    StringArgumentType.getString(context, "command")
                                                    )
                                            )
                                    )
                            )
                        )
                        .then(Commands.literal("set")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("command", StringArgumentType.string())
                                                .suggests(new BlacklistSuggestionProvider())
                                                .then(Commands.argument("value", BoolArgumentType.bool())
                                                        .executes(context -> blacklistSetResponse(
                                                                context.getSource(),
                                                                context.getArgument("target", EntitySelector.class).findSinglePlayer(context.getSource()).getName().getString(),
                                                                StringArgumentType.getString(context, "command"),
                                                                context.getArgument("value", Boolean.class)
                                                                )
                                                        )
                                                )
                                                .executes(context -> blacklistSetResponse(
                                                                context.getSource(),
                                                                context.getArgument("target", EntitySelector.class).findSinglePlayer(context.getSource()).getName().getString(),
                                                                StringArgumentType.getString(context, "command"),
                                                                true
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .then(Commands.literal("config")
                        .then(Commands.literal("reload")
                                .executes(context -> reloadConfig(context.getSource()))
                        )
                )
        );
    }

    public static int defaultResponse(CommandSourceStack source){
        String version = Kahmod.getVersion().getFriendlyString();
        source.sendSuccess(() -> Component.literal(config.getCommandResponse("rentner_smp").formatted(version)), false);
        return Command.SINGLE_SUCCESS;
    }
    
    public static int blacklistSetResponse(CommandSourceStack source, String target, String command, Boolean value){
        if (!blacklist.containsKey(target)){
            blacklist.put(target, new ArrayList<>());
        }
        List<String> commands = blacklist.get(target);
        if (value) {
            if (commands.contains(command)){
                source.sendSuccess(() -> Component.literal(config.getCommandResponse("blacklist.set.already_added").formatted(target, command)), true);
            }
            else {
                commands.add(command);
                blacklist.put(target, commands);
                source.sendSuccess(() -> Component.literal(config.getCommandResponse("blacklist.set.added").formatted(target, command)), true);
            }
        }
        else{
            if (!commands.contains(command)) {
                source.sendSuccess(() -> Component.literal(config.getCommandResponse("blacklist.set.already_removed").formatted(target, command)), true);
            }
            else {
                commands.remove(command);
                blacklist.put(target, commands);
                source.sendSuccess(() -> Component.literal(config.getCommandResponse("blacklist.set.removed").formatted(target, command)), true);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
    
    public static int blacklistGetResponse(CommandSourceStack source, String target, String command){
        if (is_blacklisted_for(target, command)){
            source.sendSuccess(() -> Component.literal(config.getCommandResponse("blacklist.get.true").formatted(target, command)), true);
        }
        else{
            source.sendSuccess(() -> Component.literal(config.getCommandResponse("blacklist.get.false").formatted(target, command)), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int blacklistGetFullResponse(CommandSourceStack source, String target){
        if (blacklist.containsKey(target)){
            if (!blacklist.get(target).isEmpty()){
                source.sendSuccess(() -> Component.literal(config.getCommandResponse("blacklist.get.full").formatted(target, blacklist.get(target).toString())), true);
                return 1;
            }
        }
        source.sendSuccess(() -> Component.literal(config.getCommandResponse("blacklist.get.full.fail").formatted(target)), true);
        return Command.SINGLE_SUCCESS;
    }
    public static boolean is_blacklisted_for(String player, String command){
        if (blacklist.containsKey(player)){
            List<String> commands = blacklist.get(player);
            return commands.contains(command);
        }
        return false;
    }

    public static void registerStatusCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("status")
                .then(Commands.literal("reset").executes(context -> (resetStatus(context.getSource()))))
                .then(
                        Commands.argument("status_arg", StringArgumentType.string())
                                .then(
                                        Commands.argument("color_arg", TeamColorArgument.teamColor())
                                                .executes(context -> (setStatus(context.getSource(), StringArgumentType.getString(context, "status_arg"), TeamColorArgument.getTeamColor(context, "color_arg").textColor())))
                                )
                                .then(
                                        Commands.argument("hex_arg", HexColorArgument.hexColor())
                                                .executes(context -> (setStatus(context.getSource(), StringArgumentType.getString(context, "status_arg"), TextColor.fromRgb(HexColorArgument.getHexColor(context, "hex_arg")))))
                                )
                                .executes(context -> (setStatus(context.getSource(), StringArgumentType.getString(context, "status_arg"), TeamColor.WHITE.textColor())))
                )
        );
    }
    public static int setStatus(CommandSourceStack source, String status, TextColor formatting){
        ServerPlayer player = source.getPlayer();
        if (!source.isPlayer() || player == null){
            source.sendFailure(Component.literal(config.getCommandResponse("feedback.console")));
            return 0;
        }
        if (is_blacklisted_for(player.getName().getString(), "status")){
            source.sendSuccess(() -> Component.literal(config.getCommandResponse("feedback.forbidden")), true);
            return 0;
        }
        if (status.length() > ConfigManager.serverConfig.maxStatusLength){
            source.sendFailure(Component.literal(config.getCommandResponse("status.long")));
            return 1;
        }
        //String playerName = Objects.requireNonNull(source.getPlayer()).getName().getString();
        String playerName = Objects.requireNonNull(source.getPlayer()).getScoreboardName();
        if (source.getServer().getScoreboard().getPlayerTeam(playerName) == null){
            source.getServer().getScoreboard().addPlayerTeam(playerName);
        }
        PlayerTeam playerTeam = source.getServer().getScoreboard().getPlayerTeam(playerName);
        if (playerTeam == null){
            return 1;
        }
        source.getServer().getScoreboard().addPlayerToTeam(playerName, playerTeam);
        playerTeam.setPlayerPrefix(Component.literal("["+status+"] ").withColor(formatting));
        source.sendSuccess(() -> Component.literal(config.getCommandResponse("status.set")).append(Component.literal("["+status+"]").withColor(formatting)), false);
        return Command.SINGLE_SUCCESS;
    }
    public static int resetStatus(CommandSourceStack source){
        ServerPlayer player = source.getPlayer();
        if (!source.isPlayer() || player == null){
            source.sendSuccess(() -> Component.literal(config.getCommandResponse("feedback.console")), false);
            return 0;
        }
        if (is_blacklisted_for(player.getName().getString(), "status")){
            source.sendSuccess(() -> Component.literal(config.getCommandResponse("feedback.forbidden")), true);
            return 0;
        }
        //String playerName = Objects.requireNonNull(source.getPlayer()).getName().getString();
        String playerName = Objects.requireNonNull(source.getPlayer()).getScoreboardName();
        if (source.getServer().getScoreboard().getPlayerTeam(playerName) == null){
            source.getServer().getScoreboard().addPlayerTeam(playerName);
        }
        PlayerTeam playerTeam = source.getServer().getScoreboard().getPlayerTeam(playerName);
        if (playerTeam == null){
            return 2;
        }
        source.getServer().getScoreboard().addPlayerToTeam(playerName, playerTeam);
        playerTeam.setPlayerPrefix(Component.empty().withStyle(ChatFormatting.RESET));
        source.sendSuccess(() -> Component.literal(config.getCommandResponse("status.reset")), false);
        return Command.SINGLE_SUCCESS;
    }
    public static int reloadConfig(CommandSourceStack source){
        ConfigManager.reloadServerConfig();
        config = ConfigManager.getServerConfig();
        for (ServerPlayer player : source.getLevel().players()) {
            player.connection.send(new ClientboundCustomPayloadPacket(new BrandPayload(config.customBrand)));
        }
        source.sendSuccess(() -> Component.literal("Successfully reloaded config!"), true);
        return Command.SINGLE_SUCCESS;
    }

    public static class BlacklistSuggestionProvider implements SuggestionProvider<CommandSourceStack> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> commandContext, SuggestionsBuilder suggestionsBuilder) {

            for (String s : new String[] {"cmd", "status", "maul"}) {
                suggestionsBuilder.suggest(s);
            }

            return suggestionsBuilder.buildFuture();
        }
    }
}
