package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.entity.Mob;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin {

    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void suppressAiDuringWarp(CallbackInfo ci) {
        if (TimeWarpManager.get().isWarping()) {
            Mob mob = (Mob) (Object) this;
            
            // Survival Buoyancy / Drown Prevention
            if (mob.isInWater() || mob.isInLava()) {
                // Land animals bob every 10 ticks; lava is always dangerous
                if (!mob.canBreatheUnderwater() || mob.isInLava()) {
                    if (mob.tickCount % 10 == 0) {
                        if (mob.getFluidHeight(net.minecraft.tags.FluidTags.WATER) > mob.getFluidJumpThreshold() || mob.isInLava()) {
                            mob.getJumpControl().jump();
                        }
                    }
                }
            } else if (mob.getDeltaMovement().y < 0) {
                // Stasis Padding: Suppress fall distance accumulation
                mob.fallDistance = 0;
            }

            // FULL LOBOTOMY: Always cancel vanilla AI during warp (Fixes Sky Lag)
            ci.cancel();
        }
    }
}
