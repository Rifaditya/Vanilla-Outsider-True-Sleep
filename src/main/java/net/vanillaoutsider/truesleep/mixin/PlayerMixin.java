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
        System.out.println("TrueSleep: Player.stopSleepInBed called! WakeImmediately: " + wakeImmediatly
                + ", UpdateLevel: " + updateLevelForSleepingPlayers);
        // Print stack trace to see WHO called it
        new Throwable("TrueSleep Stack Trace").printStackTrace();
    }

    @Inject(method = "startSleepInBed", at = @At("HEAD"))
    private void debugStartSleep(BlockPos pos, CallbackInfoReturnable<?> cir) {
        System.out.println("TrueSleep: Player.startSleepInBed called at " + pos);
    }
}
