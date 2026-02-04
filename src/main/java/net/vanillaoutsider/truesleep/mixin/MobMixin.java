package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.entity.Mob;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void freezeDuringWarp(CallbackInfo ci) {
        // Stasis Field: Mobs are completely frozen in time during the warp
        // This prevents pathfinding lag, metabolic death (drowning/starving), and chaotic movement.
        // It allows the engine to run at much higher TPS for Redstone/Furnace speed.
        
        Mob mob = (Mob) (Object) this;
        if (TimeWarpManager.get().isWarping() && mob.level() instanceof net.minecraft.server.level.ServerLevel serverLevel && serverLevel.getGameRules().get(TrueSleepRules.MOBS_FROZEN)) {
            ci.cancel();
        }
    }
}
