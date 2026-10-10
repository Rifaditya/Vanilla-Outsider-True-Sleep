// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Verified against: Gui.java (26.1.2)
@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "extractSleepOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V", at = @At("HEAD"), cancellable = true)
    private void truesleep$removeSleepDarkening(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        ci.cancel();
    }
}
