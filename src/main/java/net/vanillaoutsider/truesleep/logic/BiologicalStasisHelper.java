// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.logic;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.vanillaoutsider.truesleep.MobEffectInstanceExtensions;

public class BiologicalStasisHelper {

    public static void applyBiologicalAging(Mob mob, int ticks, boolean isFrozen, ServerLevel level) {
        if (ticks <= 0) return;

        // Check if biological aging is enabled
        if (TimeWarpManager.get().shouldAgeBiological()) {
            // 1. Age AgeableMob (baby growth and breeding cooldown)
            if (mob instanceof AgeableMob ageable) {
                int currentAge = ageable.getAge();
                if (ageable.canAgeUp()) {
                    ageable.setAge(Math.min(0, currentAge + ticks));
                } else if (currentAge > 0) {
                    ageable.setAge(Math.max(0, currentAge - ticks));
                }
            }

            // 2. Age Chicken Egg Laying timer
            if (mob instanceof Chicken chicken) {
                chicken.eggTime = Math.max(1, chicken.eggTime - ticks);
            }

            // 3. Sheep Wool Regrowth (Natural Grazing Simulation)
            if (mob instanceof Sheep sheep && sheep.isSheared()) {
                BlockPos below = sheep.blockPosition().below();
                BlockState state = level.getBlockState(below);
                if (state.is(Blocks.GRASS_BLOCK)) {
                    // Average sheep eats grass once per ~1000 ticks.
                    // Probability scaled by stride ticks:
                    float grazeChance = Math.min(1.0f, ticks / 1000.0f);
                    if (mob.getRandom().nextFloat() < grazeChance) {
                        level.setBlockAndUpdate(below, Blocks.DIRT.defaultBlockState());
                        sheep.setSheared(false);
                    }
                }
            }
        }

        // 4. Age potion effects if frozen (active mobs are handled by LivingEntityMixin)
        if (isFrozen) {
            var effects = mob.getActiveEffects();
            if (!effects.isEmpty()) {
                for (net.minecraft.world.effect.MobEffectInstance effect : effects) {
                    ((MobEffectInstanceExtensions) effect).truesleep$ageEffect(ticks);
                }
            }
        }
    }
}
