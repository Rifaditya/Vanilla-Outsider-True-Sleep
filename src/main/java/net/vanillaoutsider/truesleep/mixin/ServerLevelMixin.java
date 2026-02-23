package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ServerLevelData;
import net.vanillaoutsider.truesleep.logic.QuietClockManager;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Shadow
    @Final
    private ServerLevelData serverLevelData;

    @Shadow
    @Final
    private SleepStatus sleepStatus;

    @Shadow
    @Final
    private List<ServerPlayer> players;

    @Shadow
    private void wakeUpAllPlayers() {
    }

    @Shadow
    public abstract GameRules getGameRules();

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/SleepStatus;areEnoughSleeping(I)Z"))
    private boolean truesleep$silentSleepSuppression(SleepStatus instance, int percentage) {
        return false;
    }

    // Belt-and-suspenders: also suppress the second arm of the vanilla AND check.
    // Vanilla: areEnoughSleeping(pct) && areEnoughDeepSleeping(pct, players)
    // Both must return false so vanilla never calls
    // moveToTimeMarker(WAKE_UP_FROM_SLEEP)
    // or wakeUpAllPlayers(), which would snap the clock to vanilla dawn.
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/SleepStatus;areEnoughDeepSleeping(ILjava/util/List;)Z"))
    private boolean truesleep$silentDeepSleepSuppression(SleepStatus instance, int percentage,
            List<ServerPlayer> players) {
        return false;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void truesleep$manageTimeWarp(BooleanSupplier haveTime, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        if (level.players().isEmpty()) {
            return;
        }

        int percentage = this.getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
        // Mirror vanilla's exact two-condition gate: enough sleeping AND sleeping long
        // enough.
        // This ensures playersSleepingPercentage is fully respected (e.g. 25% = 25% of
        // online players).
        boolean enough = this.sleepStatus.areEnoughSleeping(percentage)
                && this.sleepStatus.areEnoughDeepSleeping(percentage, this.players);

        TimeWarpManager.get().tick(level, enough, this::wakeUpAllPlayers);

        if (TimeWarpManager.get().isWarping()) {
            long stride = TimeWarpManager.get().getStride();
            if (stride > 1) {
                long currentTime = level.getGameTime();
                this.serverLevelData.setGameTime(currentTime + (stride - 1));

                // Solar Sync (Quiet Mode)
                // 1. Advance all dimension clocks silently (no packets)
                net.minecraft.world.clock.ServerClockManager clockManager = level.clockManager();
                if (clockManager instanceof QuietClockManager quietManager) {
                    quietManager.truesleep$quietAdvanceAll(stride - 1);
                }

                // 2. Broadcast a SINGLE sync packet to update client sky (Smooth Visuals)
                // Only do this in the Overworld to avoid redundant packets if multiple levels
                // tick?
                // Actually ServerLevel.tick runs for EACH level.
                // Clocks are dimension-specific?
                // ServerClockManager seems to be global per server? Line 19: "private final
                // MinecraftServer server".
                // Line 20: "clocks" map.
                // ServerClockManager manages ALL clocks for the server.
                // So if we advance it in Overworld tick, we advance it for everyone.
                // We should ONLY do this once per server tick, i.e., in Overworld.

                if (level.dimension() == net.minecraft.world.level.Level.OVERWORLD) {
                    level.getServer().getPlayerList().broadcastAll(clockManager.createFullSyncPacket());
                }
            }
        }
    }
}
