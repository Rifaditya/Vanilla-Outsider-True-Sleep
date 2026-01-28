package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedRule.Rule.class)
public class BedRuleMixin {

    @Inject(method = "test", at = @At("HEAD"), cancellable = true)
    private void checkCustomSleepThreshold(Level level, CallbackInfoReturnable<Boolean> cir) {
        // We only override the "WHEN_DARK" rule behavior. 
        // "ALWAYS" and "NEVER" should remain untouched (unless we want to force sleep even in NEVER?)
        // The cast to Enum is tricky inside Mixin.
        
        // BedRule.Rule is an Enum.
        // We can check if (Object)this == BedRule.Rule.WHEN_DARK
        
        if ((Object)this == BedRule.Rule.WHEN_DARK) {
             if (level instanceof ServerLevel serverLevel) {
                 int threshold = serverLevel.getGameRules().get(TrueSleepRules.SLEEP_THRESHOLD);
                 long time = serverLevel.getDefaultClockTime() % 24000L;
                 
                 // If time >= threshold, we allow sleep.
                 // Default threshold 12542.
                 
                 if (time >= threshold) {
                     cir.setReturnValue(true);
                 }
                 
                 // If threshold is 0, time >= 0 is always true. All day sleep.
             }
        }
    }
}
