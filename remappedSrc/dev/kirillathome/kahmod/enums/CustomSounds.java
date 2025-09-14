package dev.kirillathome.kahmod.enums;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum CustomSounds {
    DISAPPEAR(Identifier.of("kahmod", "disappear")),

    BONE(Identifier.of("kahmod", "horn.bone")),
    DISAPPOINTED(Identifier.of("kahmod", "horn.disappointed")),
    FAIL(Identifier.of("kahmod", "horn.fail")),
    FOGHORN(Identifier.of("kahmod", "horn.foghorn")),
    GAMBLING(Identifier.of("kahmod", "horn.gambling")),
    GET_OUT(Identifier.of("kahmod", "horn.get_out")),
    GONG(Identifier.of("kahmod", "horn.gong")),
    STEVE(Identifier.of("kahmod", "horn.steve")),
    SUS(Identifier.of("kahmod", "horn.sus")),
    VINE_BOOM(Identifier.of("kahmod", "horn.vine_boom")),
    YIPPIE(Identifier.of("kahmod", "horn.yippie"));

    CustomSounds(Identifier id) {
        this.id = id;
    }

    private final Identifier id;

    public SoundEvent toSoundEvent() {
        return SoundEvent.of(id);
    }
}
