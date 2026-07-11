package dev.kirillathome.kahmod;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public enum CustomSoundEvents {
    DISAPPEAR("disappear"),

    BONE("horn.bone"),
    DAMN("horn.damn"),
    DISAPPOINTED("horn.disappointed"),
    FAIL("horn.fail"),
    FOGHORN("horn.foghorn"),
    GAMBLING("horn.gambling"),
    GET_OUT("horn.get_out"),
    GONG("horn.gong"),
    STEVE("horn.steve"),
    SUS("horn.sus"),
    VINE_BOOM("horn.vine_boom"),
    YIPPIE("horn.yippie");

    private final Identifier id;

    CustomSoundEvents(String path) {
        this.id = Kahmod.identifierOf(path);
    }

    public SoundEvent toSoundEvent() {
        return SoundEvent.createVariableRangeEvent(id);
    }

    public Identifier getId() {
        return id;
    }
}
