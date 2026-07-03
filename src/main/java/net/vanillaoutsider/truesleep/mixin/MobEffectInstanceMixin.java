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

import net.minecraft.world.effect.MobEffectInstance;
import net.vanillaoutsider.truesleep.MobEffectInstanceExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

// Verified against: MobEffectInstance.java (26.2+)
@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements MobEffectInstanceExtensions {

    @Shadow
    private int duration;

    @Shadow
    private @org.jspecify.annotations.Nullable MobEffectInstance hiddenEffect;

    @Shadow
    abstract void setDetailsFrom(MobEffectInstance other);

    @Override
    public MobEffectInstance truesleep$getHiddenEffect() {
        return this.hiddenEffect;
    }

    @Override
    public void truesleep$ageEffect(int ticks) {
        if (this.duration == -1) {
            return; // Infinite duration effects do not age
        }

        if (this.hiddenEffect != null) {
            ((MobEffectInstanceExtensions) this.hiddenEffect).truesleep$ageEffect(ticks);
        }

        this.duration = Math.max(0, this.duration - ticks);

        if (this.duration == 0 && this.hiddenEffect != null) {
            this.setDetailsFrom(this.hiddenEffect);
            this.hiddenEffect = ((MobEffectInstanceExtensions) this.hiddenEffect).truesleep$getHiddenEffect();
        }
    }
}
