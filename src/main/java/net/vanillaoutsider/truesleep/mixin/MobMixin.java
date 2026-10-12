// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.vanillaoutsider.truesleep.TrueSleepTags;
import net.vanillaoutsider.truesleep.logic.BiologicalStasisHelper;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ConcurrentHashMap;

// Verified against: Mob.java (1.20.1)
@Mixin(Mob.class)
public abstract class MobMixin {

    @Unique
    private static final ConcurrentHashMap<EntityType<?>, String> truesleep$unfreezeCache = new ConcurrentHashMap<>();
    @Unique
    private static final ConcurrentHashMap<EntityType<?>, Boolean> truesleep$WORKER_CACHE = new ConcurrentHashMap<>();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void truesleep$freezeDuringWarp(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (TimeWarpManager.get().isWarping()
                && mob.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {

            EntityType<?> type = mob.getType();

            // 1. Check Dynamic Unfreeze Rule for this specific Mob Type (Zero Allocation)
            String ruleName = truesleep$unfreezeCache.computeIfAbsent(type, t -> {
                ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(t);
                return id != null ? "truesleep:unfreeze_" + id.getNamespace() + "_" + id.getPath() : "";
            });

            boolean isCustomUnfrozen = !ruleName.isEmpty() && net.dasik.social.api.gamerule.DynamicGameRuleManager.getBoolean(serverLevel, ruleName);

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
