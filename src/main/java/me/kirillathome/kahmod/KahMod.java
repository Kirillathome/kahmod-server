package me.kirillathome.kahmod;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KahMod implements ModInitializer {

	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
		PolymerResourcePackUtils.addModAssets("kahmod");
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> AfkCommand.register(dispatcher));
	}
	public static final Logger LOGGER = LoggerFactory.getLogger("Kahmod");

	public static final Item INVISIBLE_ITEM_FRAME_ITEM = registerItem("invisible_item_frame",
			new PolymerDecorationItem(KahMod.INVISIBLE_ITEM_FRAME_ENTITY, Items.ITEM_FRAME, new QuiltItemSettings(), "invisible_item_frame"));

	public static final Item INVISIBLE_GLOW_ITEM_FRAME_ITEM = registerItem("invisible_glow_item_frame",
			new PolymerDecorationItem(KahMod.INVISIBLE_GLOW_ITEM_FRAME_ENTITY, Items.GLOW_ITEM_FRAME, new QuiltItemSettings(), "invisible_glow_item_frame"));

	public static final Item BLUE_CRYSTAL = registerItem("blue_crystal", new BlueCrystalItem(new QuiltItemSettings().food(new FoodComponent.Builder().hunger(2).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 2), 100).statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 400, 2), 100).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 500, 3), 100).statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 600), 100).build())));
	public static final EntityType<InvisibleItemFrameEntity> INVISIBLE_ITEM_FRAME_ENTITY = registerEntity( new Identifier("kahmod", "invisible_item_frame"),
			QuiltEntityTypeBuilder.<InvisibleItemFrameEntity>create(SpawnGroup.MISC, (InvisibleItemFrameEntity::new)).setDimensions(EntityDimensions.fixed(0.5f,0.5f)).build()
	);

	public static final EntityType<InvisibleGlowItemFrameEntity> INVISIBLE_GLOW_ITEM_FRAME_ENTITY = registerEntity( new Identifier("kahmod", "invisible_glow_item_frame"),
			QuiltEntityTypeBuilder.<InvisibleGlowItemFrameEntity>create(SpawnGroup.MISC, (InvisibleGlowItemFrameEntity::new)).setDimensions(EntityDimensions.fixed(0.5f,0.5f)).build()
	);

	public static SoundEvent MUSIC_DISC_AMOGUS = registerSoundEvent("music_disc.amogus");
	//public static final Item AMOGUS_DISC = registerItem("music_disc_amogus", new PolymerMusicDiscItem(Items.MUSIC_DISC_CAT, 7, KahMod.MUSIC_DISC_AMOGUS, new Item.Settings().rarity(Rarity.RARE).maxCount(1), 72));


	private static <T extends Entity> EntityType<T> registerEntity(Identifier provoker, EntityType<T> build) {
		Registry.register(Registries.ENTITY_TYPE, provoker, build);
		PolymerEntityUtils.registerType(build);
		return build;
	}
	private static Item registerItem(String name, Item item) {
		Registry.register(Registries.ITEM, new Identifier("kahmod", name), item);

		//PolymerResourcePackUtils.requestModel(item, new Identifier("kahmod", "item/"+ name));
		return item;
	}
	private static SoundEvent registerSoundEvent(String name) {
		Identifier id = new Identifier("kahmod", name);
		return Registry.register(Registries.SOUND_EVENT, id, new PolymerSoundEvent(id, 500f, false, SoundEvents.MUSIC_DISC_OTHERSIDE));
	}
}
