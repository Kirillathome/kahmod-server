package dev.kirillathome.kahmod.enums;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public enum CustomSounds {
    DISAPPEAR(Identifier.fromNamespaceAndPath("kahmod", "disappear")),

    BONE(Identifier.fromNamespaceAndPath("kahmod", "horn.bone")),
    DAMN(Identifier.fromNamespaceAndPath("kahmod", "horn.damn")),
    DISAPPOINTED(Identifier.fromNamespaceAndPath("kahmod", "horn.disappointed")),
    FAIL(Identifier.fromNamespaceAndPath("kahmod", "horn.fail")),
    FOGHORN(Identifier.fromNamespaceAndPath("kahmod", "horn.foghorn")),
    GAMBLING(Identifier.fromNamespaceAndPath("kahmod", "horn.gambling")),
    GET_OUT(Identifier.fromNamespaceAndPath("kahmod", "horn.get_out")),
    GONG(Identifier.fromNamespaceAndPath("kahmod", "horn.gong")),
    STEVE(Identifier.fromNamespaceAndPath("kahmod", "horn.steve")),
    SUS(Identifier.fromNamespaceAndPath("kahmod", "horn.sus")),
    VINE_BOOM(Identifier.fromNamespaceAndPath("kahmod", "horn.vine_boom")),
    YIPPIE(Identifier.fromNamespaceAndPath("kahmod", "horn.yippie"));

    CustomSounds(Identifier id) {
        this.id = id;
    }

    private final Identifier id;

    public SoundEvent toSoundEvent() {
        return SoundEvent.createVariableRangeEvent(id);
    }

    public Identifier getId() {
        return id;
    }
}
