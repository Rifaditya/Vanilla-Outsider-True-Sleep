// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.logic;

// Verified against: ServerLevel.java (26.2+)

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.gamerules.GameRules;
import java.util.Optional;
import net.vanillaoutsider.truesleep.TrueSleep;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import net.dasik.social.util.TimeUtil;
import net.dasik.social.core.GlobalSocialSystem;

public class TimeWarpManager {
    private static final TimeWarpManager INSTANCE = new TimeWarpManager();
    private boolean isWarping = false;
    private long lastWarpTime = 0;
    private long stride = 1;
    private int originalRandomTickSpeed = 3;

    // Cache fields to optimize hot entity tick loops
    private boolean freezeMobsCached = true;
    private boolean freezeWorkersCached = false;
    private boolean accelerateMachinesCached = true;
    private boolean accelerateHoppersCached = true;

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

    public boolean shouldFreezeMobs() {
        return isWarping && freezeMobsCached;
    }

    public boolean shouldFreezeWorkers() {
        return isWarping && freezeWorkersCached;
    }

    public boolean shouldAccelerateMachines() {
        return isWarping && accelerateMachinesCached;
    }

    public boolean shouldAccelerateHoppers() {
        return isWarping && accelerateHoppersCached;
    }

    public void tick(ServerLevel level, boolean allPlayersSleeping, Runnable wakeUpCallback) {
        if (allPlayersSleeping) {
            if (!isWarping) {
                TrueSleep.LOGGER.info("TrueSleep: Quantum Warp ENGAGED. Initiating TickRateManager Acceleration.");
                startWarp(level);
            } else {
                updateWarpSpeed(level);
                if (this.stride > 1) {
                    advanceTimeForStride(level, this.stride - 1);
                }
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

        // Regulate Social Hive-Mind during intensive simulation
        GlobalSocialSystem.setThrottle(20);

        updateWarpSpeed(level);
    }

    private void updateWarpSpeed(ServerLevel level) {
        float engineTps = level.getGameRules().get(TrueSleepRules.ENGINE_TPS);
        float virtualTps = level.getGameRules().get(TrueSleepRules.VIRTUAL_TPS_TARGET);
        int wakeTime = level.getGameRules().get(TrueSleepRules.WAKE_TIME);

        // Cache GameRule values once per tick to eliminate hot-loop lookup overhead
        this.freezeMobsCached = level.getGameRules().get(TrueSleepRules.MOBS_FROZEN);
        this.freezeWorkersCached = level.getGameRules().get(TrueSleepRules.WORKER_MOBS_FROZEN);
        this.accelerateMachinesCached = level.getGameRules().get(TrueSleepRules.ACCELERATE_MACHINES);
        this.accelerateHoppersCached = level.getGameRules().get(TrueSleepRules.ACCELERATE_HOPPERS);

        long timeOfDay = level.getDefaultClockTime() % 24000L;
        long dist = TimeUtil.getCycleDistance(timeOfDay, wakeTime, 24000L);

        // Smooth tapering near arrival: decelerate to 20 TPS when within 200 ticks
        float targetRate = virtualTps;
        if (dist < 200) {
            targetRate = 20.0f + (virtualTps - 20.0f) * ((float) dist / 200.0f);
        }
        targetRate = Math.max(20.0f, targetRate);

        // Calculate stride based on capped ENGINE_TPS
        float physicalTarget = Math.min(engineTps, targetRate);
        this.stride = Math.max(1, Math.round(targetRate / physicalTarget));

        setTickRate(level, physicalTarget);

        // Scale randomTickSpeed
        int newSpeed = Math.min(500, (int) (this.originalRandomTickSpeed * this.stride));
        int currentSpeed = level.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
        if (currentSpeed != newSpeed) {
            level.getGameRules().set(GameRules.RANDOM_TICK_SPEED, newSpeed, level.getServer());
        }
    }

    private void stopWarp(ServerLevel level) {
        isWarping = false;
        lastWarpTime = level.getGameTime();
        this.stride = 1;

        // Restore Social Hive-Mind to native 20 TPS
        GlobalSocialSystem.setThrottle(1);

        setTickRate(level, 20.0f);

        // Restore randomTickSpeed
        int currentSpeed = level.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
        if (currentSpeed != this.originalRandomTickSpeed) {
            level.getGameRules().set(GameRules.RANDOM_TICK_SPEED, this.originalRandomTickSpeed, level.getServer());
        }
    }

    private void advanceTimeForStride(ServerLevel level, long skipTicks) {
        if (skipTicks <= 0) return;

        long newTime = level.getGameTime() + skipTicks;
        ((net.minecraft.world.level.storage.ServerLevelData) level.getLevelData()).setGameTime(newTime);

        Optional<Holder<WorldClock>> defaultClock = level.dimensionType().defaultClock();
        if (defaultClock.isPresent()) {
            level.getServer().clockManager().addTicks(defaultClock.get(), (int) skipTicks);
        }

        advanceWeatherForStride(level, skipTicks);
    }

    private void advanceWeatherForStride(ServerLevel level, long skipTicks) {
        if (skipTicks <= 0) return;

        net.minecraft.world.level.saveddata.WeatherData weatherData = level.getWeatherData();
        if (weatherData != null) {
            int clearWeatherTime = weatherData.getClearWeatherTime();
            int rainTime = weatherData.getRainTime();
            int thunderTime = weatherData.getThunderTime();

            if (clearWeatherTime > 0) {
                weatherData.setClearWeatherTime((int) Math.max(0, clearWeatherTime - skipTicks));
            } else {
                if (rainTime > 0) {
                    weatherData.setRainTime((int) Math.max(0, rainTime - skipTicks));
                }
                if (thunderTime > 0) {
                    weatherData.setThunderTime((int) Math.max(0, thunderTime - skipTicks));
                }
            }
        }
    }

    private void checkMorning(ServerLevel level, Runnable wakeUpCallback) {
        int wakeTime = level.getGameRules().get(TrueSleepRules.WAKE_TIME);
        long currentTimeOfDay = level.getDefaultClockTime() % 24000L;
        long dist = TimeUtil.getCycleDistance(currentTimeOfDay, wakeTime, 24000L);

        if (dist < 20) {
            TrueSleep.LOGGER.info("TrueSleep: Destination reached: Target Time {}. Stopping Hyperspace.", wakeTime);
            stopWarp(level);

            // Snap the clock EXACTLY to wakeTime (monotonically increasing — never go backwards).
            long currentFull = level.getDefaultClockTime();
            long currentDay = currentFull - (currentFull % 24000L);
            long snappedTime = currentDay + wakeTime;
            if (snappedTime < currentFull) {
                snappedTime += 24000L; // Advance to next day if we overshot
            }
            
            // Update the level data game time
            ((net.minecraft.world.level.storage.ServerLevelData) level.getLevelData()).setGameTime(snappedTime);

            // Update and broadcast the dimension's default WorldClock time to prevent client desync
            Optional<Holder<WorldClock>> defaultClock = level.dimensionType().defaultClock();
            if (defaultClock.isPresent()) {
                level.getServer().clockManager().setTotalTicks(defaultClock.get(), snappedTime);
            }

            wakeUpCallback.run();
            level.resetWeatherCycle();
        }
    }

    private void setTickRate(ServerLevel level, float rate) {
        ((net.minecraft.world.level.Level) level).tickRateManager().setTickRate(rate);
    }
}
