package net.vanillaoutsider.truesleep.mixin.client;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "renderSleepOverlay", at = @At("HEAD"), cancellable = true)
    private void removeSleepDarkening(CallbackInfo ci) {
        ci.cancel();
    }
}
