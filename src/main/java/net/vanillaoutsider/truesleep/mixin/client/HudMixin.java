// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Verified against: Gui.java (1.21.11)
@Mixin(Gui.class)
public class HudMixin {

    @Inject(method = "renderSleepOverlay", at = @At("HEAD"), cancellable = true)
    private void truesleep$removeSleepDarkening(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        ci.cancel();
    }
}
