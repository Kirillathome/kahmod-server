package me.kirillathome.kahmod;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum CustomSounds {
    MUSIC_DISC_AMOGUS(new Identifier("kahmod", "music_disc.amogus")),
    MUSIC_DISC_MRBIGSHOT(new Identifier("kahmod", "music_disc.mrbigshot")),
    DISAPPEAR(new Identifier("kahmod", "disappear"));

    public final Identifier id;

    public SoundEvent toSoundEvent() {
        return SoundEvent.createVariableRangeEvent(id);
    }

    CustomSounds(Identifier id) {
        this.id = id;
    }
}
