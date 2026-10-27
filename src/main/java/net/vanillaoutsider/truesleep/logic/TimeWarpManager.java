// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.logic;

// Verified against: ServerLevel.java (1.21.11)

import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ServerLevelData;
import net.vanillaoutsider.truesleep.TrueSleep;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import net.dasik.social.util.TimeUtil;
import net.dasik.social.core.GlobalSocialSystem;

public class TimeWarpManager {
    private static final TimeWarpManager INSTANCE = new TimeWarpManager();
    private ResourceKey<Level> activeLevelKey = null;
    private boolean isWarping = false;
    private long lastWarpTime = 0;
    private long stride = 1;
    private int originalRandomTickSpeed = 3;

    // Cache fields to optimize hot entity tick loops
    private boolean freezeMobsCached = true;
    private boolean freezeWorkersCached = false;
    private boolean accelerateMachinesCached = true;
    private boolean accelerateHoppersCached = true;
    private boolean biologicalAgingCached = true;
    private boolean sleepHungerCached = true;

    private long lastWarpStride = 1;
    private float lastWarpRate = 20.0f;
    private int decelerateTicks = 0;

    public boolean hasRecentWarp(long currentWorldTime) {
        return isWarping || decelerateTicks > 0 || (currentWorldTime - lastWarpTime) < 5;
    }

    public static TimeWarpManager get() {
        return INSTANCE;
    }

    public boolean isWarping() {
        return isWarping || decelerateTicks > 0;
    }

    public long getStride() {
        return (isWarping || decelerateTicks > 0) ? stride : 1;
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

    public boolean shouldAgeBiological() {
        return isWarping && biologicalAgingCached;
    }

    public boolean shouldDrainSleepHunger() {
        return isWarping && sleepHungerCached;
    }

    public ResourceKey<Level> getActiveLevelKey() {
        return activeLevelKey;
    }

    public void tick(ServerLevel level, boolean allPlayersSleeping, Runnable wakeUpCallback) {
        if (activeLevelKey != null && !level.dimension().equals(activeLevelKey)) {
            return;
        }

        if (allPlayersSleeping) {
            decelerateTicks = 0; // Cancel any active wind-down if sleep resumes
            if (!isWarping) {
                TrueSleep.LOGGER.info("TrueSleep: Quantum Warp ENGAGED for dimension [{}]. Initiating TickRateManager Acceleration.", level.dimension().identifier());
                startWarp(level);
            } else {
                updateWarpSpeed(level);
                if (shouldDrainSleepHunger()) {
                    for (net.minecraft.server.level.ServerPlayer player : level.players()) {
                        SleepHungerHelper.applySleepHunger(player, 1);
                    }
                }
                if (this.stride > 1) {
                    advanceTimeForStride(level, this.stride - 1);
                }
                checkMorning(level, wakeUpCallback);
            }
        } else {
            if (isWarping) {
                TrueSleep.LOGGER.info("TrueSleep: Warp Aborted for dimension [{}] - Sleep interrupted. Initiating smooth 15-tick deceleration wind-down.", level.dimension().identifier());
                initiateWindDown(level);
            } else if (decelerateTicks > 0) {
                tickWindDown(level);
            }
        }
    }

    private void startWarp(ServerLevel level) {
        this.activeLevelKey = level.dimension();
        isWarping = true;
        decelerateTicks = 0;
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
        this.biologicalAgingCached = level.getGameRules().get(TrueSleepRules.BIOLOGICAL_AGING);
        this.sleepHungerCached = level.getGameRules().get(TrueSleepRules.SLEEP_HUNGER);

        long timeOfDay = level.getDayTime() % 24000L;
        long dist = TimeUtil.getCycleDistance(timeOfDay, wakeTime, 24000L);

        // Smooth tapering near arrival: decelerate targetRate from virtualTps down to 20.0 TPS over dist [200 -> 20]
        float targetRate = virtualTps;
        if (dist < 200) {
            float progress = Math.max(0.0f, Math.min(1.0f, (float) (dist - 20) / 180.0f));
            targetRate = 20.0f + (virtualTps - 20.0f) * progress;
        }
        targetRate = Math.max(20.0f, targetRate);

        // Calculate stride based on capped ENGINE_TPS
        float physicalTarget = Math.min(engineTps, targetRate);
        this.stride = Math.max(1, Math.round(targetRate / physicalTarget));
        this.lastWarpStride = this.stride;
        this.lastWarpRate = physicalTarget;

        setTickRate(level, physicalTarget);

        // Scale randomTickSpeed
        int newSpeed = Math.min(500, (int) (this.originalRandomTickSpeed * this.stride));
        int currentSpeed = level.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
        if (currentSpeed != newSpeed) {
            level.getGameRules().set(GameRules.RANDOM_TICK_SPEED, newSpeed, level.getServer());
        }
    }

    private void initiateWindDown(ServerLevel level) {
        isWarping = false;
        decelerateTicks = 15;
        tickWindDown(level);
    }

    private void tickWindDown(ServerLevel level) {
        if (decelerateTicks <= 0) {
            stopWarp(level);
            return;
        }

        float progress = (float) decelerateTicks / 15.0f;
        float currentRate = 20.0f + (this.lastWarpRate - 20.0f) * progress;
        this.stride = Math.max(1, Math.round(1 + (this.lastWarpStride - 1) * progress));

        setTickRate(level, currentRate);

        int targetRandomSpeed = (int) (this.originalRandomTickSpeed + (this.originalRandomTickSpeed * (this.stride - 1)));
        level.getGameRules().set(GameRules.RANDOM_TICK_SPEED, Math.min(500, targetRandomSpeed), level.getServer());

        decelerateTicks--;
        if (decelerateTicks <= 0) {
            stopWarp(level);
        }
    }

    private void stopWarp(ServerLevel level) {
        this.activeLevelKey = null;
        isWarping = false;
        decelerateTicks = 0;
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

        long newGameTime = level.getGameTime() + skipTicks;
        ((ServerLevelData) level.getLevelData()).setGameTime(newGameTime);
        long newDayTime = level.getDayTime() + skipTicks;
        level.setDayTime(newDayTime);
        level.getServer().getPlayerList().broadcastAll(
            new ClientboundSetTimePacket(
                level.getGameTime(), level.getDayTime(), level.getGameRules().get(GameRules.ADVANCE_TIME)
            )
        );

        advanceWeatherForStride(level, skipTicks);

        if (shouldDrainSleepHunger()) {
            for (net.minecraft.server.level.ServerPlayer player : level.players()) {
                SleepHungerHelper.applySleepHunger(player, skipTicks);
            }
        }
    }

    private void advanceWeatherForStride(ServerLevel level, long skipTicks) {
        if (skipTicks <= 0) return;

        ServerLevelData serverLevelData = (ServerLevelData) level.getLevelData();
        int clearWeatherTime = serverLevelData.getClearWeatherTime();
        int rainTime = serverLevelData.getRainTime();
        int thunderTime = serverLevelData.getThunderTime();

        if (clearWeatherTime > 0) {
            serverLevelData.setClearWeatherTime((int) Math.max(0, clearWeatherTime - skipTicks));
        } else {
            if (rainTime > 0) {
                serverLevelData.setRainTime((int) Math.max(0, rainTime - skipTicks));
            }
            if (thunderTime > 0) {
                serverLevelData.setThunderTime((int) Math.max(0, thunderTime - skipTicks));
            }
        }
    }

    private void checkMorning(ServerLevel level, Runnable wakeUpCallback) {
        int wakeTime = level.getGameRules().get(TrueSleepRules.WAKE_TIME);
        long currentTimeOfDay = level.getDayTime() % 24000L;
        long dist = TimeUtil.getCycleDistance(currentTimeOfDay, wakeTime, 24000L);

        if (dist < 20) {
            TrueSleep.LOGGER.info("TrueSleep: Destination reached: Target Time {} for dimension [{}]. Stopping Hyperspace.", wakeTime, level.dimension().identifier());
            stopWarp(level);

            // Snap the clock EXACTLY to wakeTime (monotonically increasing — never go backwards).
            long currentFull = level.getDayTime();
            long currentDay = currentFull - (currentFull % 24000L);
            long snappedTime = currentDay + wakeTime;
            if (snappedTime < currentFull) {
                snappedTime += 24000L; // Advance to next day if we overshot
            }

            // Update the level day time and broadcast packet
            level.setDayTime(snappedTime);
            level.getServer().getPlayerList().broadcastAll(
                new ClientboundSetTimePacket(
                    level.getGameTime(), level.getDayTime(), level.getGameRules().get(GameRules.ADVANCE_TIME)
                )
            );

            wakeUpCallback.run();
            if (level.getGameRules().get(GameRules.ADVANCE_WEATHER) && (level.isRaining() || level.isThundering())) {
                level.resetWeatherCycle();
            }
        }
    }

    private void setTickRate(ServerLevel level, float rate) {
        ((Level) level).tickRateManager().setTickRate(rate);
    }
}
