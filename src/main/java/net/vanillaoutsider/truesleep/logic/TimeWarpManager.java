package net.vanillaoutsider.truesleep.logic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRules;
import net.vanillaoutsider.truesleep.TrueSleep;
// import net.vanillaoutsider.truesleep.config.TrueSleepConfig;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;

public class TimeWarpManager {
    private static final TimeWarpManager INSTANCE = new TimeWarpManager();
    private boolean isWarping = false;
    private long lastWarpTime = 0;
    private int originalRandomTickSpeed = 3;
    private long stride = 1;

    public boolean hasRecentWarp(long currentWorldTime) {
        return isWarping || (currentWorldTime - lastWarpTime) < 5;
    }

    public static TimeWarpManager get() {
        return INSTANCE;
    }

    public boolean isWarping() {
        return isWarping;
    }

    public long getStride() {
        return isWarping ? stride : 1;
    }

    public void tick(ServerLevel level, boolean allPlayersSleeping, Runnable wakeUpCallback) {
        if (allPlayersSleeping) {
            if (!isWarping) {
                TrueSleep.LOGGER.info("TrueSleep: Quantum Warp ENGAGED. Initiating Stride Engine.");
                startWarp(level);
            } else {
                updateWarpSpeed(level);
                checkMorning(level, wakeUpCallback);
            }
        } else {
            if (isWarping) {
                TrueSleep.LOGGER.info("TrueSleep: Warp Aborted - Sleep interrupted.");
                stopWarp(level);
            }
        }
    }

    private void startWarp(ServerLevel level) {
        isWarping = true;
        this.originalRandomTickSpeed = level.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
        updateWarpSpeed(level);
    }

    private void updateWarpSpeed(ServerLevel level) {
        float engineTps = level.getGameRules().get(TrueSleepRules.ENGINE_TPS);
        float virtualTps = level.getGameRules().get(TrueSleepRules.VIRTUAL_TPS_TARGET);

        int wakeTime = level.getGameRules().get(TrueSleepRules.WAKE_TIME);
        
        long timeOfDay = level.getDefaultClockTime() % 24000L;
        // Check if we are currently "in the night" relative to the target wake time.
        // If we slept at 13000 and target is 14000, we just warp 1000 ticks.
        // If we slept at 13000 and target is 0 (Morning), we warp ~11000 ticks.
        
        // This warp detection logic (Time > 23000 || Time < 1000) was for Morning wake-up check tapering.
        // We should adjust it to be relative to "wakeTime".
        // Tapering logic: If we are close to wakeTime, slow down.
        
        long dist = (wakeTime - timeOfDay); // e.g. 0 - 23500 = -23500. 
        if (dist < 0) dist += 24000L; // 500 ticks left.
        
        if (dist < 1000) { 
             // Slow down near arrival
            engineTps = 20.0f;
            virtualTps = 20.0f;
        }

        this.stride = Math.max(1, (long)(virtualTps / engineTps));
        
        int scaledRandomTick = (int)(originalRandomTickSpeed * stride);
        level.getGameRules().set(GameRules.RANDOM_TICK_SPEED, scaledRandomTick, level.getServer());

        setTickRate(level, engineTps);
    }

    private void stopWarp(ServerLevel level) {
        isWarping = false;
        lastWarpTime = level.getGameTime();
        this.stride = 1;
        
        level.getGameRules().set(GameRules.RANDOM_TICK_SPEED, originalRandomTickSpeed, level.getServer());
        
        setTickRate(level, 20.0f);
    }

    private void checkMorning(ServerLevel level, Runnable wakeUpCallback) {
        int wakeTime = level.getGameRules().get(TrueSleepRules.WAKE_TIME);
        
        // "Is Bright Outside" check is vanilla hardcoded for sunrise.
        // We need to check if we hit our target time.
        // Vanilla sleeping wakes at time = 0.
        
        long timeOfDay = level.getDefaultClockTime() % 24000L;
        
        // We stop if we just passed the wakeTime. 
        // Since we jump by 'stride', we might overshoot slightly. 
        // We check if current time is within [wakeTime, wakeTime + stride + buffer].
        
        // Simplified check: If we are effectively "at" the target time.
        // However, standard sleep logic sets time to x000.
        // Actually, vanilla sleep sets time to 'next morning'. our warp just runs time forward.
        // We stop if we are "close enough" or past it compared to when we started?
        // No, simplest: use distance check from updateWarpSpeed logic.
        
        long dist = (wakeTime - timeOfDay);
        if (dist < 0) dist += 24000L;
        
        // If distance is extremely small (we caught up) or very large (we just passed it)
        // Wait, if we just passed it, dist would be 23990 (ish).
        // Let's rely on the fact that updateWarpSpeed slows us down to 1:1 near the end.
        // So checking if dist < 10 is safe.
        
        if (dist < 20) {
             TrueSleep.LOGGER.info("TrueSleep: Destination reached: Target Time {}. Stopping Hyperspace.", wakeTime);
             stopWarp(level);
             
             // Ensure we land EXACTLY on the target time for cleanliness
             long currentFull = level.getDefaultClockTime();
             long correction = wakeTime - (currentFull % 24000L);
             // If positive, add. If negative (we passed it), subtract? 
             // Level time is monotonic usually.
             
             wakeUpCallback.run();
             level.resetWeatherCycle();
        }
    }

    private void setTickRate(ServerLevel level, float rate) {
        ((net.minecraft.world.level.Level)level).tickRateManager().setTickRate(rate);
    }
}
