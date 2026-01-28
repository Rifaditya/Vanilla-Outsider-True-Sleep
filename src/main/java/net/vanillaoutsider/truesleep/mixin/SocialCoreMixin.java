package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.server.level.ServerLevel;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The Shadow Governor: Externally regulates the Social Hive-Mind pulse
 * during a TimeWarp to prevent performance collapse at 1000 TPS.
 */
@Mixin(targets = "net.vanillaoutsider.social.core.GlobalSocialSystem", remap = false)
public class SocialCoreMixin {

    @Inject(method = "pulse", at = @At("HEAD"), cancellable = true)
    private static void throttleSocialPulse(ServerLevel level, CallbackInfo ci) {
        if (TimeWarpManager.get().isWarping()) {
            // Throttle to 1/20 ticks (effective 50 TPS for Social logic at 1000 TPS)
            if (level.getGameTime() % 20 != 0) {
                ci.cancel();
            }
        }
    }
}
