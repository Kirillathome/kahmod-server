package me.kirillathome.kahmod.enums;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public enum CustomSounds {
    MUSIC_DISC_AMOGUS(new Identifier("kahmod", "music_disc.amogus")),
    MUSIC_DISC_MRBIGSHOT(new Identifier("kahmod", "music_disc.mrbigshot")),

    HORN_YIPPIE(new Identifier("kahmod", "horns.yippie")),
    HORN_VINE_BOOM(new Identifier("kahmod", "horns.vine_boom")),
    HORN_SUS(new Identifier("kahmod", "horns.sus")),
    HORN_GASTER(new Identifier("kahmod", "horns.gaster")),
    HORN_BONE(new Identifier("kahmod", "horns.bone")),

    DISAPPEAR(new Identifier("kahmod", "disappear"));

    private final Identifier id;

    public Identifier getId() {
        return id;
    }

    public SoundEvent toSoundEvent() {
        return SoundEvent.createVariableRangeEvent(id);
    }

    public static CustomSounds fromId(Identifier id) {
        for (CustomSounds customSound : values()) {
            if (customSound.id.equals(id)) {
                return customSound;
            }
        }
        return null;
    }

    CustomSounds(Identifier id) {
        this.id = id;
    }
}
