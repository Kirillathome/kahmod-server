package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.CustomItems;
import dev.kirillathome.kahmod.mixin.accessor.EntityAccessor;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.server.level.ChunkMap$TrackedEntity")
public abstract class ChunkMapMixin {

    @Shadow
    @Final
    private Entity entity;

    @Redirect(method = "sendToTrackingPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerConnection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void redirectSend(ServerPlayerConnection instance, Packet<? super ClientGamePacketListener> packet) {
        if (packet instanceof ClientboundSetEntityDataPacket dataPacket && this.entity.getType().equals(EntityTypes.ITEM_FRAME)) {
            assert EntityAccessor.getSharedFlagsID() != null;
            int location = -1;
            byte value = entity.getEntityData().get(EntityAccessor.getSharedFlagsID());

            for (int i = 0; i < dataPacket.packedItems().size(); i++) {
                if (dataPacket.packedItems().get(i).id() == EntityAccessor.getSharedFlagsID().id()) {
                    location = i;
                    break;
                }
            }

            SynchedEntityData.DataValue<Byte> dataValue = SynchedEntityData.DataValue.create(
                    EntityAccessor.getSharedFlagsID(),
                    instance.getPlayer().getItemHeldByArm(HumanoidArm.LEFT).is(CustomItems.INVISIBLE_ITEM_FRAME) ||
                            instance.getPlayer().getItemHeldByArm(HumanoidArm.RIGHT).is(CustomItems.INVISIBLE_ITEM_FRAME) ?
                    (byte) (value | (1 << EntityAccessor.getFlagGlowing())) : value
            );

            if (location == -1) {
                dataPacket.packedItems().add(dataValue);
            } else {
                dataPacket.packedItems().set(
                        location,
                        dataValue
                );
            }
        }
        instance.send(packet);
    }
}
