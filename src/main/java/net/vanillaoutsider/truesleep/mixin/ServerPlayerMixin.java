// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Verified against: ServerPlayer.java (1.20.1)
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Redirect(
        method = "startSleepInBed",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isDay()Z")
    )
    private boolean truesleep$overrideIsDay(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            int threshold = serverLevel.getGameRules().getInt(TrueSleepRules.SLEEP_THRESHOLD);
            long time = serverLevel.getDayTime() % 24000L;
            if (time >= threshold) {
                return false; // Time threshold met, allow sleep
            }
        }
        return level.isDay();
    }
}
