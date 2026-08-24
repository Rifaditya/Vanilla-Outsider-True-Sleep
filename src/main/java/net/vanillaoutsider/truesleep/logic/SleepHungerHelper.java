// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.logic;

import net.minecraft.server.level.ServerPlayer;

public class SleepHungerHelper {

    public static void applySleepHunger(ServerPlayer player, long strideTicks) {
        if (strideTicks <= 0 || player == null || !player.isSleeping()) return;
        if (!TimeWarpManager.get().shouldDrainSleepHunger()) return;

        // Starvation safety floor: Stop draining if at or below sprint threshold (6 food level / 3 drumsticks)
        if (player.getFoodData().getFoodLevel() <= 6) return;

        // Standard vanilla food exhaustion rate: ~3.0 - 4.0 hunger points across a standard 10,000-tick night
        float exhaustion = strideTicks * 0.0008f;
        player.causeFoodExhaustion(exhaustion);
    }
}
