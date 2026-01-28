package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.entity.LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void drownGuard(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (TimeWarpManager.get().isWarping()) {
            if (source.is(DamageTypes.DROWN)) {
                // Dreamweaver: Check ifimmunity is enabled
                if (level.getGameRules().get(net.vanillaoutsider.truesleep.config.TrueSleepRules.DROWN_IMMUNITY)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
