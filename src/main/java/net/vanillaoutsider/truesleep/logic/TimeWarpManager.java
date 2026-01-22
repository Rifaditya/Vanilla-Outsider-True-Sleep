package net.vanillaoutsider.truesleep.logic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.TickRateManager;
import net.vanillaoutsider.truesleep.config.TrueSleepConfig;

public class TimeWarpManager {
    private static final TimeWarpManager INSTANCE = new TimeWarpManager();
    private boolean isWarping = false;
    private long lastWarpTime = 0;

    public boolean hasRecentWarp(long currentWorldTime) {
        return isWarping || (currentWorldTime - lastWarpTime) < 5; // Valid for 5 ticks after warp
    }

    public static TimeWarpManager get() {
        return INSTANCE;
    }

    public boolean isWarping() {
        return isWarping;
    }

    public void tick(ServerLevel level, boolean allPlayersSleeping, Runnable wakeUpCallback) {
        if (allPlayersSleeping) {
            if (!isWarping) {
                System.out.println("TrueSleep: STARTING WARP.");
                startWarp(level);
            } else {
                // Continue warping
                checkMorning(level, wakeUpCallback);
            }
        } else {
            if (isWarping) {
                System.out.println("TrueSleep: Warp Aborted - Not enough sleepers.");
                stopWarp(level);
            }
        }
    }

    private void startWarp(ServerLevel level) {
        isWarping = true;
        // float speed = TrueSleepConfig.get().warpSpeed;
        float speed = 500.0f; // User requested "fast as you can"
        System.out.println("TrueSleep: Setting Tick Rate to " + speed);
        setTickRate(level, speed);
    }

    private void stopWarp(ServerLevel level) {
        isWarping = false;
        lastWarpTime = level.getGameTime();
        System.out.println("TrueSleep: Stopping Warp. Resetting to 20.0");
        setTickRate(level, 20.0f);
    }

    private void checkMorning(ServerLevel level, Runnable wakeUpCallback) {
        // level.isDay() checks if sun is up.
        // level.isBrightOutside() checks light level.
        if (level.isBrightOutside()) {
            System.out.println("TrueSleep: Morning detected (isBrightOutside=true). Stopping warp and waking players.");
            stopWarp(level);
            wakeUpCallback.run();
            // Reset weather if needed
            level.resetWeatherCycle();
        }
    }

    private void setTickRate(ServerLevel level, float rate) {
        // Decompiled source: this.tickRateManager() returns TickRateManager
        // tickRateManager.setTickRate(float)
        level.tickRateManager().setTickRate(rate);
    }
}
