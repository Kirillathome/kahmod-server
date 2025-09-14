package dev.kirillathome.kahmod.util;

import dev.kirillathome.kahmod.enums.CustomSounds;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class SoundHelper {
    public static void playSound(CustomSounds customSoundEvent, SoundCategory soundCategory, World world, double x, double y, double z) {
        playSound(customSoundEvent, soundCategory, world, x, y, z, 1f, 1f);
    }

    public static void playSound(CustomSounds customSoundEvent, SoundCategory soundCategory, World world, double x, double y, double z, float volume, float pitch) {
        SoundEvent vanillaSoundEvent = customSoundEvent.toSoundEvent();
        world.playSound(null, x, y, z, vanillaSoundEvent, soundCategory, volume, pitch);
    }
}
