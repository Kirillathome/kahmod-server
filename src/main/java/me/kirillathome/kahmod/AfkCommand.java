package me.kirillathome.kahmod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.EntityGroup;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static net.minecraft.server.command.CommandManager.literal;

public class AfkCommand {

    private static boolean afk;
    private static EntityGroup player_group;
    private static Map<String, Boolean> afk_players = new HashMap<>();
    private static Map<String, EntityGroup> afk_player_groups = new HashMap<>();
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("afk").executes(context -> {return afk(context.getSource());}));
    }

    public static int afk(ServerCommandSource source){
        player_group = null;
        if (afk_players.get(Objects.requireNonNull(source.getPlayer()).getUuidAsString()) != null){
            afk = afk_players.get(source.getPlayer().getUuidAsString());
        } else {
            afk_players.put(source.getPlayer().getUuidAsString(), false);
        }
        if (!afk && source.getPlayer().getGroup() != null) {
            if (afk_player_groups.get(Objects.requireNonNull(source.getPlayer()).getUuidAsString()) != null){
                player_group = afk_player_groups.get(source.getPlayer().getUuidAsString());
            } else {
                afk_player_groups.put(source.getPlayer().getUuidAsString(), source.getPlayer().getGroup());
            }
        }
        if (afk) {
            source.getServer().getPlayerManager().broadcastSystemMessage(Text.literal(Objects.requireNonNull(source.getPlayer()).getEntityName() + " is no longer afk.").formatted(Formatting.GRAY).formatted(Formatting.ITALIC), false);
            afk_players.put(source.getPlayer().getUuidAsString(), false);
            if (player_group != null){
            }
        } else {
            source.getServer().getPlayerManager().broadcastSystemMessage(Text.literal(Objects.requireNonNull(source.getPlayer()).getEntityName() + " is now afk.").formatted(Formatting.GRAY).formatted(Formatting.ITALIC), false);
            afk_players.put(source.getPlayer().getUuidAsString(), true);
        }
        return Command.SINGLE_SUCCESS;
    }
}
