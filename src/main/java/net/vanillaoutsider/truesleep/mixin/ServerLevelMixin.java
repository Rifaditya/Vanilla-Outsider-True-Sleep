// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.world.level.gamerules.GameRules;
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

// Verified against: ServerLevel.java (26.2+)
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

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
        // Mirror vanilla's exact two-condition gate: enough sleeping AND sleeping long enough.
        boolean enough = this.sleepStatus.areEnoughSleeping(percentage)
                && this.sleepStatus.areEnoughDeepSleeping(percentage, this.players);

        TimeWarpManager.get().tick(level, enough, this::wakeUpAllPlayers);
    }
}
