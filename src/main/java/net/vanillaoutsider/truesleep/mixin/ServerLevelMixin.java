/*
 * This file is part of True Sleep.
 *
 * True Sleep is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * True Sleep is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with True Sleep.  If not, see <https://www.gnu.org/licenses/>.
 */
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
