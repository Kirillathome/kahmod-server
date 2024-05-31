package me.kirillathome.kahmod.util;

import me.kirillathome.kahmod.enums.CustomSounds;
import net.minecraft.network.packet.s2c.play.SoundPlayS2CPacket;
import net.minecraft.registry.Holder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Objects;

public class SoundHelper {

    public static void playSound(CustomSounds customSoundEvent, SoundCategory soundCategory, World world, double x, double y, double z) {
        playSound(customSoundEvent, soundCategory, world, x, y, z, 1f, 1f);
    }

    public static void playSound(CustomSounds customSoundEvent, SoundCategory soundCategory, World world, double x, double y, double z, float volume, float pitch) {
        SoundEvent vanillaSoundEvent = customSoundEvent.toSoundEvent();
        if (world.getServer() != null) {
            for (ServerPlayerEntity serverPlayer : world.getEntitiesByType(TypeFilter.instanceOf(ServerPlayerEntity.class), new Box(x, y, z, x, y, z).expand(vanillaSoundEvent.getRange(volume)/2), Objects::nonNull)){
                serverPlayer.networkHandler.send(new SoundPlayS2CPacket(
                        Holder.createDirect(vanillaSoundEvent),
                        soundCategory,
                        x,
                        y,
                        z,
                        volume,
                        pitch,
                        world.random.nextLong()
                ));
            }
        }
        else {
            world.playSound(null, x, y, z, vanillaSoundEvent, soundCategory, volume, pitch);
        }
    }
}
