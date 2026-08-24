// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.minecraft.world.effect.MobEffectInstance;

public interface MobEffectInstanceExtensions {
    void truesleep$ageEffect(int ticks);
    MobEffectInstance truesleep$getHiddenEffect();
}
