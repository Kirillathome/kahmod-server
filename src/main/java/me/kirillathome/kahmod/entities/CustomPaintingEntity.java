package me.kirillathome.kahmod.entities;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.mixin.accessors.DisplayEntityAccessor;
import eu.pb4.polymer.virtualentity.mixin.accessors.ItemDisplayEntityAccessor;
import me.kirillathome.kahmod.CustomEntities;
import me.kirillathome.kahmod.CustomItems;
import me.kirillathome.kahmod.CustomPaintingVariants;
import me.kirillathome.kahmod.KahMod;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class CustomPaintingEntity extends AbstractDecorationEntity implements PolymerEntity {
    private CustomPaintingVariants VARIANT = CustomPaintingVariants.DUMB_CAT;
    private List<PolymerPaintingHitbox> hitboxes = new ArrayList<>();

    public CustomPaintingEntity(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }
    private CustomPaintingEntity(World world, BlockPos pos) {
        super(CustomEntities.CUSTOM_PAINTING, world, pos);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.ITEM_DISPLAY;
    }

    @Override
    public Vec3d getClientSidePosition(Vec3d vec3d) {
        return vec3d.subtract(Vec3d.ZERO.relative(this.facing, 0.46875)).subtract(Vec3d.ZERO.relative(this.facing.rotateYCounterclockwise(), offset(getWidthPixels()))).subtract(0, offset(getHeightPixels()), 0);
    }
    @Override
    protected void updateAttachmentPosition(){
        if (this.facing != null) {
            double d = (double)this.attachmentPos.getX() + 0.5;
            double e = (double)this.attachmentPos.getY() + 0.5;
            double f = (double)this.attachmentPos.getZ() + 0.5;
            double h = this.offset(this.getWidthPixels());
            double i = this.offset(this.getHeightPixels());
            d -= (double)this.facing.getOffsetX() * 0.46875;
            f -= (double)this.facing.getOffsetZ() * 0.46875;
            e += i;
            Direction direction = this.facing.rotateYCounterclockwise();
            d += h * (double)direction.getOffsetX();
            f += h * (double)direction.getOffsetZ();
            this.setPos(d, e, f);
        }
    }
    private double offset(int offset) {
        return offset % 32 == 0 ? 0.5 : 0.0;
    }
    private static int getVariantArea(CustomPaintingVariants variant){
        return variant.width * variant.height;
    }
    @Override
    public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial){
        ItemStack itemStack = new ItemStack(Items.PAPER);
        NbtCompound nbt = itemStack.getOrCreateNbt();
        nbt.putInt("CustomModelData", getVariant().model);
        data.add(DataTracker.SerializedEntry.create(ItemDisplayEntityAccessor.getITEM(), itemStack));
        data.add(DataTracker.SerializedEntry.create(ItemDisplayEntityAccessor.getITEM_DISPLAY(), ModelTransformationMode.FIXED.getValue()));
        data.add(DataTracker.SerializedEntry.create(DisplayEntityAccessor.getBRIGHTNESS(), 255));
    }
    @Override
    public int getWidthPixels() {
        return getVariant().width;
    }

    @Override
    public int getHeightPixels() {
        return getVariant().height;
    }

    @Override
    public void onBreak(@Nullable Entity entity) {
        if (this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);
            if (entity instanceof PlayerEntity playerEntity && playerEntity.getAbilities().creativeMode) {
                return;
            }
            this.dropItem(CustomItems.CUSTOM_PAINTING);
        }
    }
    @Override
    public void onPlace() {
        this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
        for (int offset_width = 0; offset_width < getWidthPixels() / 16; offset_width++){
            for (int offset_height = 0; offset_height < getHeightPixels() / 16; offset_height++){
                PolymerPaintingHitbox hitbox = new PolymerPaintingHitbox(
                        getWorld(),
                        attachmentPos.offset(this.facing.rotateYCounterclockwise(), offset_width).offset(Direction.UP, offset_height),
                        this.facing,
                        this
                );
                getWorld().spawnEntity(hitbox);
            }
        }
    }
    private void getHitboxes(){
        for (int offset_width = 0; offset_width < getWidthPixels() / 16; offset_width++){
            for (int offset_height = 0; offset_height < getHeightPixels() / 16; offset_height++){
                Box searchbox = new Box(attachmentPos.offset(this.facing.rotateYCounterclockwise(), offset_width).offset(Direction.UP, offset_height));
                for (AbstractDecorationEntity entity : getWorld().getEntitiesByType(TypeFilter.instanceOf(AbstractDecorationEntity.class), searchbox, Objects::nonNull)){
                    //KahMod.LOGGER.info("Found Hitbox: %s".formatted(entity));
                    if (entity instanceof PolymerPaintingHitbox) {
                        this.hitboxes.add((PolymerPaintingHitbox) entity);
                        ((PolymerPaintingHitbox) entity).setOwner(this);
                        break;
                    }
                }
            }
        }
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        writeVariant(nbt, this.getVariant());
        nbt.putByte("facing", (byte)this.facing.getHorizontal());
        super.writeCustomDataToNbt(nbt);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        CustomPaintingVariants variant = parseVariant(nbt);
        this.setVariant(variant);
        this.facing = Direction.fromHorizontal(nbt.getByte("facing"));
        super.readCustomDataFromNbt(nbt);
        this.setFacing(this.facing);
    }
    public static void writeVariant(NbtCompound nbt, CustomPaintingVariants variant) {
        nbt.putString("variant", variant.toString());
    }
    public static CustomPaintingVariants parseVariant(NbtCompound nbt) {
        String variant_key = nbt.getString("variant");
        CustomPaintingVariants variant = CustomPaintingVariants.DUMB_CAT;
        for (CustomPaintingVariants key : CustomPaintingVariants.values()){
            if (key.toString().equals(variant_key)){
                variant = key;
                break;
            }
        }
        return variant;
    }

    public void setVariant(CustomPaintingVariants variant){
        this.VARIANT = variant;
    }
    public CustomPaintingVariants getVariant(){
        return VARIANT;
    }
    public static Optional<CustomPaintingEntity> getPaintingForLocation(World world, BlockPos pos, Direction direction) {
        CustomPaintingEntity paintingEntity = new CustomPaintingEntity(world, pos);
        List<CustomPaintingVariants> list = new ArrayList<>();
        Collections.addAll(list, CustomPaintingVariants.values());
        if (list.isEmpty()){
            return Optional.empty();
        } else {
            paintingEntity.setFacing(direction);
            paintingEntity.updateAttachmentPosition();
            list.removeIf(painting -> {
                paintingEntity.setVariant(painting);
                return paintingEntity.isClipping();
            });
            //KahMod.LOGGER.info(String.valueOf(list));
            if (list.isEmpty()){
                //KahMod.LOGGER.info("failed cause list is empty");
                return Optional.empty();
            } else {
                int i = list.stream().mapToInt(CustomPaintingEntity::getVariantArea).max().orElse(1);
                list.removeIf(variant -> getVariantArea(variant) < i);
                Optional<CustomPaintingVariants> optional = Util.getRandomOrEmpty(list, paintingEntity.random);
                if (optional.isEmpty()){
                    //KahMod.LOGGER.info("failed cause didn't get random??");
                    return Optional.empty();
                } else {
                    //KahMod.LOGGER.info("didn't fail???");
                    paintingEntity.setVariant(optional.get());
                    paintingEntity.setFacing(direction);
                    paintingEntity.updateAttachmentPosition();
                    return Optional.of(paintingEntity);
                }
            }
        }
    }

    public boolean isClipping(){
        //updateAttachmentPosition();
        for (int offset_width = 0; offset_width < getWidthPixels() / 16; offset_width++){
            for (int offset_height = 0; offset_height < getHeightPixels() / 16; offset_height++){
                BlockPos currentBlock = attachmentPos.offset(this.facing.rotateYCounterclockwise(), offset_width).offset(Direction.UP, offset_height);
                if (!getWorld().getBlockState(currentBlock).isAir() || !getWorld().getOtherEntities(this, new Box(currentBlock), entity -> entity instanceof PolymerPaintingHitbox).isEmpty() || getWorld().getBlockState(currentBlock.offset(this.facing.getOpposite())).isAir()){
                    //KahMod.LOGGER.info("FAILED!! Variant: %s, Block: %s".formatted(this.VARIANT, currentBlock));
                    return true;
                }
            }
        }
        return false;
    }

    public void handleBreak(@Nullable Entity entity){
        for (PolymerPaintingHitbox paintingEntity : hitboxes){
            paintingEntity.discard();
        }
        onBreak(entity);
        discard();
    }
    @Override
    public void refreshPositionAndAngles(double x, double y, double z, float yaw, float pitch) {
        this.setPosition(x, y, z);
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.setPosition(x, y, z);
    }

    @Override
    public Vec3d getAttachmentPos() {
        return Vec3d.of(this.attachmentPos);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, this.facing.getId(), this.getDecorationBlockPos());
    }

    /*@Override
    public void tick(){
        super.tick();
        if (hitboxes.isEmpty()){
            getHitboxes();
        }
    }*/

    @Override
    public void onEntityPacketSent(Consumer<Packet<?>> consumer, Packet<?> packet) {
        PolymerEntity.super.onEntityPacketSent(consumer, packet);
        if (this.hitboxes.isEmpty()){
            getHitboxes();
        }
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setFacing(Direction.byId(packet.getEntityData()));
    }
}
