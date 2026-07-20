// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.entity.Mob;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.vanillaoutsider.truesleep.TrueSleepTags;
import net.vanillaoutsider.truesleep.MobEffectInstanceExtensions;

// Verified against: Mob.java (26.2+)
@Mixin(Mob.class)
public abstract class MobMixin {
    private static final java.util.Map<net.minecraft.world.entity.EntityType<?>, java.util.Optional<GameRule<Boolean>>> truesleep$unfreezeCache = new java.util.concurrent.ConcurrentHashMap<>();


    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void truesleep$freezeDuringWarp(CallbackInfo ci) {
        // Stasis Field: Mobs are completely frozen in time during the warp
        // This prevents pathfinding lag, metabolic death (drowning/starving), and
        // chaotic movement.
        // It allows the engine to run at much higher TPS for Redstone/Furnace speed.

        Mob mob = (Mob) (Object) this;
        if (TimeWarpManager.get().isWarping()
                && mob.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {

            // 1. Check Dynamic Unfreeze Rule for this specific Mob Type
            net.minecraft.world.entity.EntityType<?> type = mob.getType();
            java.util.Optional<GameRule<Boolean>> ruleOpt = truesleep$unfreezeCache.get(type);
            if (ruleOpt == null) {
                Identifier entityId = BuiltInRegistries.ENTITY_TYPE.getKey(type);
                if (entityId != null) {
                    String ruleName = "truesleep:unfreeze_" + entityId.getNamespace() + "_" + entityId.getPath();
                    Identifier ruleId = Identifier.parse(ruleName);
                    @SuppressWarnings("unchecked")
                    GameRule<Boolean> rule = (GameRule<Boolean>) BuiltInRegistries.GAME_RULE.getValue(ruleId);
                    ruleOpt = java.util.Optional.ofNullable(rule);
                } else {
                    ruleOpt = java.util.Optional.empty();
                }
                truesleep$unfreezeCache.put(type, ruleOpt);
            }

            boolean isCustomUnfrozen = false;
            if (ruleOpt.isPresent()) {
                isCustomUnfrozen = serverLevel.getGameRules().get(ruleOpt.get());
            }

            boolean shouldFreeze = false;
            if (!isCustomUnfrozen) {
                // 2. Fallback to Tags and Global Settings
                boolean isWorker = mob.getType().builtInRegistryHolder().is(TrueSleepTags.WORKER_MOBS);
                boolean freezeWorkers = TimeWarpManager.get().shouldFreezeWorkers();
                boolean freezeAll = TimeWarpManager.get().shouldFreezeMobs();

                if (isWorker) {
                    if (freezeWorkers) {
                        shouldFreeze = true;
                    }
                } else {
                    if (freezeAll) {
                        shouldFreeze = true;
                    }
                }
            }

            int stride = (int) TimeWarpManager.get().getStride();
            if (shouldFreeze) {
                truesleep$applyWarpAging(mob, stride, true);
                ci.cancel();
            } else {
                if (stride > 1) {
                    truesleep$applyWarpAging(mob, stride - 1, false);
                }
            }
        }
    }

    @Unique
    private void truesleep$applyWarpAging(Mob mob, int ticks, boolean isFrozen) {
        if (ticks <= 0) return;

        // 1. Age AgeableMob
        if (mob instanceof net.minecraft.world.entity.AgeableMob ageable) {
            int currentAge = ageable.getAge();
            if (ageable.canAgeUp()) {
                ageable.setAge(Math.min(0, currentAge + ticks));
            } else if (currentAge > 0) {
                ageable.setAge(Math.max(0, currentAge - ticks));
            }
        }

        // 2. Age potion effects if frozen (active mobs are handled by LivingEntityMixin)
        if (isFrozen) {
            for (net.minecraft.world.effect.MobEffectInstance effect : mob.getActiveEffects()) {
                ((MobEffectInstanceExtensions) effect).truesleep$ageEffect(ticks);
            }
        }
    }
}
