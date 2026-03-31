package dev.kirillathome.kahmod.util;

import dev.kirillathome.kahmod.enums.CustomSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class SoundHelper {
    public static void playSound(CustomSounds customSoundEvent, SoundSource soundSource, Level world, double x, double y, double z) {
        playSound(customSoundEvent, soundSource, world, x, y, z, 1f, 1f);
    }

    public static void playSound(CustomSounds customSoundEvent, SoundSource soundSource, Level world, double x, double y, double z, float volume, float pitch) {
        SoundEvent vanillaSoundEvent = customSoundEvent.toSoundEvent();
        world.playSound(null, x, y, z, vanillaSoundEvent, soundSource, volume, pitch);
    }
}
