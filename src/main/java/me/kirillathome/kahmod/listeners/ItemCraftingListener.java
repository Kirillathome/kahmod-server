package me.kirillathome.kahmod.listeners;

import me.kirillathome.kahmod.CustomItems;
import me.kirillathome.kahmod.KahMod;
import me.kirillathome.kahmod.items.CopperHornItem;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.quiltmc.qsl.entity.event.api.ServerEntityTickCallback;

import java.util.Objects;
import java.util.function.Predicate;

public class ItemCraftingListener {
    public static void registerListener(){
        KahMod.LOGGER.info("Registering the ItemCraftingListener");
        ServerEntityTickCallback.EVENT.register(ItemCraftingListener::onItemEntityTick);
    }


    private static void onItemEntityTick(Entity entity, boolean b){
        if (entity instanceof ItemEntity itemEntity){
            if (entity.getWorld().getBlockState(new BlockPos(itemEntity.getBlockX(), itemEntity.getBlockY()-1, itemEntity.getBlockZ())).getBlock().equals(Blocks.JUKEBOX)){
                Box searchBox = new Box(itemEntity.getBlockPos()).expand(1f, 0f, 1f);
                ItemStack item1 = itemEntity.getStack();
                entity.getWorld().getEntitiesByClass(ItemEntity.class, searchBox, Predicate.not(Objects::isNull))
                        .forEach(nearbyItemEntity -> {
                            ItemStack item2 = nearbyItemEntity.getStack();
                            if (canCraftItems(item1, item2) && nearbyItemEntity.age >= 20 && itemEntity.age >= 40){
                                ItemStack result = craftItems(item1, item2);
                                item1.decrement(1);
                                item2.decrement(1);
                                ItemEntity craftedEntity = new ItemEntity(entity.getWorld(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), result);
                                entity.getWorld().spawnEntity(craftedEntity);
                                craftedEntity.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 1, entity.getWorld().getRandom().nextFloat());
                                if (entity.getServer() != null) {
                                    for (ServerPlayerEntity player : entity.getServer().getPlayerManager().getPlayerList()) {
                                        entity.getServer().getWorld(entity.getWorld().getRegistryKey()).spawnParticles(player, ParticleTypes.CRIT, false, entity.getX(), entity.getY(), entity.getZ(), 6, 0.0, 0.0, 0.0, 1);
                                    }
                                }
                            }
                        });
            }
        }
    }
    private static boolean canCraftItems(ItemStack item1, ItemStack item2){
        if (item1.getItem().equals(CustomItems.COPPER_HORN)){
            if (item2.getItem().equals(Items.TNT)){
                return true;
            }
            if (item2.getItem().equals(Items.IRON_SWORD)){
                return true;
            }
            if (item2.getItem().equals(Items.EMERALD)){
                return true;
            }
        }
        return false;
    }
    private static ItemStack craftItems(ItemStack item1, ItemStack item2){
        if (item1.getItem().equals(CustomItems.COPPER_HORN)){
            if (item2.getItem().equals(Items.TNT)){
                ItemStack craftedStack = new ItemStack(CustomItems.COPPER_HORN);
                CopperHornItem.setSound(craftedStack, new Identifier("kahmod", "vine_boom"));
                return craftedStack;
            }
            if (item2.getItem().equals(Items.IRON_SWORD)){
                ItemStack craftedStack = new ItemStack(CustomItems.COPPER_HORN);
                CopperHornItem.setSound(craftedStack, new Identifier("kahmod", "sus"));
                return craftedStack;
            }
            if (item2.getItem().equals(Items.EMERALD)){
                ItemStack craftedStack = new ItemStack(CustomItems.COPPER_HORN);
                CopperHornItem.setSound(craftedStack, new Identifier("kahmod", "yippie"));
                return craftedStack;
            }
        }
        return ItemStack.EMPTY;
    }
}
