package me.kirillathome.kahmod.entities;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.mixin.accessors.DisplayEntityAccessor;
import eu.pb4.polymer.virtualentity.mixin.accessors.ItemDisplayEntityAccessor;
import me.kirillathome.kahmod.CustomEntities;
import me.kirillathome.kahmod.CustomItems;
import me.kirillathome.kahmod.CustomPaintingVariants;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomPaintingEntity extends AbstractDecorationEntity implements PolymerEntity {
    private CustomPaintingVariants VARIANT = CustomPaintingVariants.DUMB_CAT;
    private List<PaintingEntity> paintingEntities = new ArrayList<>();

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
        return vec3d.subtract(Vec3d.ZERO.relative(this.facing, 0.46875));
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
        PaintingEntity paintingEntity = new PaintingEntity(getWorld(), attachmentPos, this.facing, Registries.PAINTING_VARIANT.getHolderOrThrow(PaintingVariants.KEBAB));
        getWorld().spawnEntity(paintingEntity);
        this.paintingEntities.add(paintingEntity);
        //this.setBoundingBox(new Box(attachmentPos, attachmentPos.add(2, 2, 2)));
        /*ElementHolder holder = new ElementHolder();
        InteractionElement interaction = new InteractionElement();
        holder.addElement(interaction);

        EntityAttachment.ofTicking(holder, this);*/
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        writeVariant(nbt, this.getVariant());
        nbt.putByte("facing", (byte)this.facing.getHorizontal());
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        CustomPaintingVariants variant = parse(nbt);
        this.setVariant(variant);
        this.facing = Direction.fromHorizontal(nbt.getByte("facing"));
        super.readCustomDataFromNbt(nbt);
        this.setFacing(this.facing);
    }
    public static void writeVariant(NbtCompound nbt, CustomPaintingVariants variant) {
        nbt.putString("variant", variant.toString());
    }
    public static CustomPaintingVariants parse(NbtCompound nbt) {
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
        paintingEntity.setVariant(Util.getRandom(CustomPaintingVariants.values(), paintingEntity.random));
        paintingEntity.setFacing(direction);
        return Optional.of(paintingEntity);
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

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setFacing(Direction.byId(packet.getEntityData()));
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(CustomItems.CUSTOM_PAINTING);
    }
    @Override
    public void tick(){
        boolean alive = true;
        for (PaintingEntity entity : paintingEntities){
            if (!entity.isAlive()){
                alive = false;
                PlayerEntity nearestPlayer = getWorld().getClosestPlayer(this, 10);
                for (var item : getWorld().getOtherEntities(this, new Box(getBlockPos()).expand(0.5))){
                    if (item instanceof ItemEntity){
                        if (((ItemEntity) item).getStack().getItem().equals(Items.PAINTING) && nearestPlayer != null && !nearestPlayer.isCreative()){
                            ((ItemEntity) item).getStack().decrement(1);
                            break;
                        }
                    }
                }
                break;
            }
        }
        if (!alive){
            for (PaintingEntity entity : paintingEntities){
                entity.kill();
            }
            paintingEntities.clear();
            onBreak(getWorld().getClosestPlayer(this, 10));
            kill();
        }
        super.tick();
    }
}
