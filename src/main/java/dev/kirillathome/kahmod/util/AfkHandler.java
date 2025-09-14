package dev.kirillathome.kahmod.util;

import dev.kirillathome.kahmod.config.ConfigManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

import java.util.Objects;

public class AfkHandler {

    public static void setAfk(ServerPlayerEntity player) {
        if (player.getServer() == null) {
            return; // GET OUT
        }

        //Kahmod.LOGGER.info(player.getName().getString().concat(" is now afk."));
        player.getServer().getScoreboard().addScoreHolderToTeam(player.getName().getString(), getOrCreateAfkTeam(player));

        if (ConfigManager.serverConfig.afkNotify && !player.getGameMode().equals(GameMode.SPECTATOR)) {
            for (ServerPlayerEntity p : player.getServer().getPlayerManager().getPlayerList()) {
                p.sendMessage(Text.literal(ConfigManager.serverConfig.getCommandResponse("afk.set").formatted(player.getStyledDisplayName().getString())).formatted(Formatting.GRAY));
            }
        }
    }

    public static void unsetAfk(ServerPlayerEntity player) {
        unsetAfk(player, false);
    }
    public static void unsetAfk(ServerPlayerEntity player, boolean silent) {
        if (player.getServer() == null) {
            return; // GET OUT
        }

        //Kahmod.LOGGER.info(player.getName().getString().concat(" is no longer afk."));
        player.getServer().getScoreboard().addScoreHolderToTeam(player.getName().getString(), getOrCreateDefaultTeam(player));

        if (ConfigManager.serverConfig.afkNotify && !player.getGameMode().equals(GameMode.SPECTATOR) && !silent) {
            for (ServerPlayerEntity p : player.getServer().getPlayerManager().getPlayerList()) {
                p.sendMessage(Text.literal(ConfigManager.serverConfig.getCommandResponse("afk.unset").formatted(player.getStyledDisplayName().getString())).formatted(Formatting.GRAY));
            }
        }
    }
    
    private static Team getOrCreateDefaultTeam(ServerPlayerEntity player) {
        if (player.getServer() == null) {
            return null; // zamn
        }

//        String team_name = player.getName().getString();
        String team_name = player.getNameForScoreboard();
        
        if (player.getServer().getScoreboard().getTeam(team_name) == null){
            player.getServer().getScoreboard().addTeam(team_name);
        }
        return player.getServer().getScoreboard().getTeam(team_name);
    }

    private static Team getOrCreateAfkTeam(ServerPlayerEntity player) {
        if (player.getServer() == null) {
            return null; // zamn
        }

        //String team_name = player.getName().getString().concat("_afk");
        String team_name = player.getNameForScoreboard().concat("_afk");

        if (player.getServer().getScoreboard().getTeam(team_name) == null){
            player.getServer().getScoreboard().addTeam(team_name);
            Objects.requireNonNull(player.getServer().getScoreboard().getTeam(team_name)).setColor(Formatting.GRAY);
        }

        Team team = player.getServer().getScoreboard().getTeam(team_name);
        team.setPrefix(getOrCreateDefaultTeam(player).getPrefix().copy().formatted(Formatting.GRAY));

        return team;
    }
}
