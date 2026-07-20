// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import net.vanillaoutsider.truesleep.MobEffectInstanceExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Collection;

// Verified against: LivingEntity.java (26.2+)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract Collection<MobEffectInstance> getActiveEffects();

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void truesleep$ageEffectsDuringWarp(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!self.level().isClientSide() && TimeWarpManager.get().isWarping()) {
            long stride = TimeWarpManager.get().getStride();
            if (stride > 1) {
                int skipTicks = (int) (stride - 1);
                for (MobEffectInstance effect : this.getActiveEffects()) {
                    ((MobEffectInstanceExtensions) effect).truesleep$ageEffect(skipTicks);
                }
            }
        }
    }
}
