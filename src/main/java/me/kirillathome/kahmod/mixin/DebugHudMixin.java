package me.kirillathome.kahmod.mixin;

import net.minecraft.client.gui.hud.DebugHud;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
@ClientOnly
public class DebugHudMixin {
	@Inject(at = @At("RETURN"), method = "getRightText")
	protected void getRightText(CallbackInfoReturnable<List<String>> info) {
		info.getReturnValue().add("Kahmod is active on this client!");
	}
}
