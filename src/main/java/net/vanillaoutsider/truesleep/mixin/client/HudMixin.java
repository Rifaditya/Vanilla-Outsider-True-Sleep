// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin.client;

// Verified against: Hud.java (26.2+)

import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Verified against: Hud.java (26.2+) (e:\Minecraft Project\Minecraft Decomplide code for reference only\26.2 Release Decompile\client\src\net\minecraft\client\gui\Hud.java)
@Mixin(Hud.class)
public class HudMixin {

    @Inject(method = "extractSleepOverlay", at = @At("HEAD"), cancellable = true)
    private void truesleep$removeSleepDarkening(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        ci.cancel();
    }
}
