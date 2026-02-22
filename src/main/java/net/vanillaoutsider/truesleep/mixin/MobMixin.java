package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.entity.Mob;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.vanillaoutsider.truesleep.TrueSleepTags;

@Mixin(Mob.class)
public abstract class MobMixin {

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
            // Query the registry directly to avoid cache timing issues.
            Identifier entityId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType());
            if (entityId != null) {
                // Rule is stored under the raw name e.g. "ts_unfreeze_minecraft_allay"
                // which Identifier.parse() maps to "minecraft:ts_unfreeze_minecraft_allay"
                String ruleName = "ts_unfreeze_" + entityId.getNamespace() + "_" + entityId.getPath();
                Identifier ruleId = Identifier.parse(ruleName);
                @SuppressWarnings("unchecked")
                GameRule<Boolean> ruleObj = (GameRule<Boolean>) BuiltInRegistries.GAME_RULE.getValue(ruleId);
                if (ruleObj != null) {
                    boolean isCustomUnfrozen = serverLevel.getGameRules().get(ruleObj);
                    if (isCustomUnfrozen) {
                        return; // Mob is allowed to tick normally
                    }
                }
            }

            // 2. Fallback to Tags and Global Settings
            boolean isWorker = mob.getType().builtInRegistryHolder().is(TrueSleepTags.WORKER_MOBS);
            boolean freezeWorkers = serverLevel.getGameRules().get(TrueSleepRules.WORKER_MOBS_FROZEN);
            boolean freezeAll = serverLevel.getGameRules().get(TrueSleepRules.MOBS_FROZEN);

            if (isWorker) {
                if (freezeWorkers) {
                    ci.cancel();
                }
            } else {
                if (freezeAll) {
                    ci.cancel();
                }
            }
        }
    }
}
