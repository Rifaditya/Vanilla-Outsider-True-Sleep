// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
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
