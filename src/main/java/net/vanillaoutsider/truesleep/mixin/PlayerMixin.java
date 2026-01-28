package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "stopSleepInBed", at = @At("HEAD"))
    private void debugStopSleep(boolean wakeImmediatly, boolean updateLevelForSleepingPlayers, CallbackInfo ci) {
        // Log removed for performance and cleanliness
    }

    @Inject(method = "startSleepInBed", at = @At("HEAD"))
    private void debugStartSleep(BlockPos pos, CallbackInfoReturnable<?> cir) {
        // Log removed for performance and cleanliness
    }

    // Dreamweaver: Custom sleep check moved to BedRuleMixin
    // due to vanilla logic shift from Level.isDay() to BedRule.test().
}
