// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin;

// Verified against: VibrationSystem.java (26.2+)

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.gameevent.vibrations.VibrationSystem$Listener")
public class VibrationSystemListenerMixin {

    @Inject(method = "handleGameEvent", at = @At("HEAD"), cancellable = true)
    private void truesleep$suppressVibrations(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition, CallbackInfoReturnable<Boolean> cir) {
        if (TimeWarpManager.get().isWarping()) {
            cir.setReturnValue(false);
        }
    }
}
