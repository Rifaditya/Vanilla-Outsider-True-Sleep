// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Verified against: BedRule.java (1.21.11)
@Mixin(BedRule.Rule.class)
public class BedRuleMixin {

    @Inject(method = "test", at = @At("HEAD"), cancellable = true)
    private void truesleep$checkCustomSleepThreshold(Level level, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == BedRule.Rule.WHEN_DARK) {
            if (level instanceof ServerLevel serverLevel) {
                int threshold = serverLevel.getGameRules().get(TrueSleepRules.SLEEP_THRESHOLD);
                long time = serverLevel.getDayTime() % 24000L;

                if (time >= threshold) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
