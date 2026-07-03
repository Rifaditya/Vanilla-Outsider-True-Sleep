/*
 * This file is part of True Sleep.
 *
 * True Sleep is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * True Sleep is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with True Sleep.  If not, see <https://www.gnu.org/licenses/>.
 */
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
