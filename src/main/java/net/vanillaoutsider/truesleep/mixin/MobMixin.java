// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.entity.Mob;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import net.vanillaoutsider.truesleep.logic.BiologicalStasisHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.vanillaoutsider.truesleep.TrueSleepTags;

// Verified against: Mob.java (26.3+)
@Mixin(Mob.class)
public abstract class MobMixin {

    @Unique
    private static final Object NO_CUSTOM_RULE = new Object();
    @Unique
    private static final java.util.concurrent.ConcurrentHashMap<net.minecraft.world.entity.EntityType<?>, Object> truesleep$unfreezeCache = new java.util.concurrent.ConcurrentHashMap<>();
    @Unique
    private static final java.util.concurrent.ConcurrentHashMap<net.minecraft.world.entity.EntityType<?>, Boolean> truesleep$WORKER_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void truesleep$freezeDuringWarp(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (TimeWarpManager.get().isWarping()
                && mob.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {

            net.minecraft.world.entity.EntityType<?> type = mob.getType();

            // 1. Check Dynamic Unfreeze Rule for this specific Mob Type (Zero Allocation)
            Object cached = truesleep$unfreezeCache.get(type);
            if (cached == null) {
                Identifier entityId = BuiltInRegistries.ENTITY_TYPE.getKey(type);
                if (entityId != null) {
                    String ruleName = "truesleep:unfreeze_" + entityId.getNamespace() + "_" + entityId.getPath();
                    Identifier ruleId = Identifier.parse(ruleName);
                    @SuppressWarnings("unchecked")
                    GameRule<Boolean> rule = (GameRule<Boolean>) BuiltInRegistries.GAME_RULE.getValue(ruleId);
                    cached = rule != null ? rule : NO_CUSTOM_RULE;
                } else {
                    cached = NO_CUSTOM_RULE;
                }
                truesleep$unfreezeCache.put(type, cached);
            }

            boolean isCustomUnfrozen = false;
            if (cached instanceof GameRule<?> gameRule) {
                @SuppressWarnings("unchecked")
                GameRule<Boolean> boolRule = (GameRule<Boolean>) gameRule;
                isCustomUnfrozen = serverLevel.getGameRules().get(boolRule);
            }

            boolean shouldFreeze = false;
            if (!isCustomUnfrozen) {
                // 2. Fallback to Cached Tags and Global Settings
                boolean isWorker = truesleep$WORKER_CACHE.computeIfAbsent(type, t -> t.builtInRegistryHolder().is(TrueSleepTags.WORKER_MOBS));
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
                BiologicalStasisHelper.applyBiologicalAging(mob, stride, true, serverLevel);
                ci.cancel();
            } else {
                if (stride > 1) {
                    BiologicalStasisHelper.applyBiologicalAging(mob, stride - 1, false, serverLevel);
                }
            }
        }
    }
}
