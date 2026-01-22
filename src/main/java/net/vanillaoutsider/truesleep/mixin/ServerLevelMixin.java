package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.world.level.gamerules.GameRules;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Shadow
    private SleepStatus sleepStatus;

    @Shadow
    private void wakeUpAllPlayers() {
    }

    @Shadow
    public abstract GameRules getGameRules();

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/SleepStatus;areEnoughSleeping(I)Z"))
    private boolean disableVanillaSleepSkip(SleepStatus instance, int percentage) {
        System.out.println("TrueSleep: Redirect hit. Suppressing vanilla sleep skip.");
        return false;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void manageTimeWarp(BooleanSupplier haveTime, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        // Optimization: Don't process time warp for levels with no players (e.g.,
        // ticking Nether in singleplayer)
        if (level.players().isEmpty()) {
            return;
        }

        int percentage = this.getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
        boolean enough = this.sleepStatus.areEnoughSleeping(percentage);

        // System.out.println("TrueSleep: Tick. Enough Sleepers? " + enough + " (Percent
        // rule: " + percentage + ")");
        if (enough || TimeWarpManager.get().isWarping()) {
            System.out.println("TrueSleep: Status - Sleeper Count: " + this.sleepStatus.amountSleeping() + ", Needed: "
                    + this.sleepStatus.sleepersNeeded(percentage) + ", Enough: " + enough);
        }

        TimeWarpManager.get().tick((ServerLevel) (Object) this, enough, this::wakeUpAllPlayers);
    }
}
