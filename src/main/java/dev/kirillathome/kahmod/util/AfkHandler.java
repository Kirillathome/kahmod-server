package dev.kirillathome.kahmod.util;

import dev.kirillathome.kahmod.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.TeamColor;

import java.util.Objects;
import java.util.Optional;

public class AfkHandler {

    public static void setAfk(ServerLevel level, ServerPlayer player) {
       level.getScoreboard().addPlayerToTeam(player.getName().getString(), getOrCreateAfkTeam(level, player));
        if (ConfigManager.serverConfig.afkNotify && !player.gameMode().equals(GameType.SPECTATOR)) {
            for (ServerPlayer p : level.players()) {
                p.sendSystemMessage(Component.literal(ConfigManager.serverConfig.getCommandResponse("afk.set").formatted(player.getDisplayName().getString())).withStyle(ChatFormatting.GRAY));
            }
        }
    }

//    public static void unsetAfk(ServerPlayer player) {
//        unsetAfk(player, false);
//    }
    
    public static void unsetAfk(ServerLevel level, ServerPlayer player, boolean silent) {
        level.getScoreboard().addPlayerToTeam(player.getName().getString(), getOrCreateDefaultTeam(level, player));

        if (ConfigManager.serverConfig.afkNotify && !player.gameMode().equals(GameType.SPECTATOR) && !silent) {
            for (ServerPlayer p : level.players()) {
                p.sendSystemMessage(Component.literal(ConfigManager.serverConfig.getCommandResponse("afk.unset").formatted(player.getDisplayName().getString())).withStyle(ChatFormatting.GRAY));
            }
        }
    }
    
    private static PlayerTeam getOrCreateDefaultTeam(ServerLevel level, ServerPlayer player) {
        String teamName = player.getScoreboardName();

        if (level.getScoreboard().getPlayerTeam(teamName) == null){
            level.getScoreboard().addPlayerTeam(teamName);
        }

        return level.getScoreboard().getPlayerTeam(teamName);
    }

    private static PlayerTeam getOrCreateAfkTeam(ServerLevel level, ServerPlayer player) {
        String teamName = player.getScoreboardName().concat("_afk");

        if (level.getScoreboard().getPlayerTeam(teamName) == null){
            level.getScoreboard().addPlayerTeam(teamName);
            Objects.requireNonNull(level.getScoreboard().getPlayerTeam(teamName)).setColor(Optional.of(TeamColor.GRAY));
        }

        PlayerTeam team = level.getScoreboard().getPlayerTeam(teamName);
        assert team != null;
        team.setPlayerPrefix(getOrCreateDefaultTeam(level, player).getPlayerPrefix().copy().withStyle(ChatFormatting.GRAY));

        return team;
    }
}
