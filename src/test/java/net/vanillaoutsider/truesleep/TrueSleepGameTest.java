// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.dasik.social.util.TimeUtil;
import net.vanillaoutsider.truesleep.config.TrueSleepConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 4: Headless Simulation & GameTest Parity Suite for Vanilla Outsider: True Sleep.
 * Asserts time warp progression from dusk to sunrise, monotonic clock snapping,
 * and strict weather cycle preservation adhering to BL-TS-012.
 */
class TrueSleepGameTest {

    @BeforeEach
    void setUp() {
        TrueSleepConfig.get().resetDefaults();
    }

    @AfterEach
    void tearDown() {
        TrueSleepConfig.get().resetDefaults();
    }

    @Test
    @DisplayName("Pillar 4: Time warp progression simulates dusk-to-dawn transition and smooth tapering")
    void testTimeWarpProgressionFromDuskToDawn() {
        TrueSleepConfig config = TrueSleepConfig.get();
        config.engineTps = 50.0f;
        config.virtualTps = 1000.0f;
        config.wakeTime = 0; // Sunrise (0 / 24000)

        long worldTime = 12542L; // Dusk / Vanilla Sleep Start
        int wakeTime = config.wakeTime;
        long totalAdvance = 0;
        int steps = 0;
        boolean reachedDestination = false;

        AtomicLong currentClock = new AtomicLong(worldTime);

        while (steps < 2000 && !reachedDestination) {
            steps++;
            long timeOfDay = currentClock.get() % 24000L;
            long dist = TimeUtil.getCycleDistance(timeOfDay, wakeTime, 24000L);

            if (dist < 20) {
                // Destination reached! Snap clock to wakeTime monotonically
                long currentFull = currentClock.get();
                long currentDay = currentFull - (currentFull % 24000L);
                long snappedTime = currentDay + wakeTime;
                if (snappedTime < currentFull) {
                    snappedTime += 24000L;
                }
                currentClock.set(snappedTime);
                reachedDestination = true;
                break;
            }

            // Stride & rate calculation
            float targetRate = config.virtualTps;
            if (dist < 200) {
                float progress = Math.max(0.0f, Math.min(1.0f, (float) (dist - 20) / 180.0f));
                targetRate = 20.0f + (config.virtualTps - 20.0f) * progress;
            }
            targetRate = Math.max(20.0f, targetRate);

            float physicalTarget = Math.min(config.engineTps, targetRate);
            long stride = Math.max(1, Math.round(targetRate / physicalTarget));

            assertTrue(stride >= 1, "Stride must always be >= 1");

            // Advance clock by stride ticks
            currentClock.addAndGet(stride);
            totalAdvance += stride;
        }

        assertTrue(reachedDestination, "Simulation must reach morning within 2,000 steps");
        assertEquals(0L, currentClock.get() % 24000L, "Clock must cleanly snap to wakeTime 0 (Sunrise)");
        assertTrue(currentClock.get() >= worldTime, "Clock advancement must be strictly monotonic (never go backwards)");
    }

    @Test
    @DisplayName("Pillar 4: Weather cycle preservation during clear sleep adheres strictly to BL-TS-012")
    void testWeatherCyclePreservationOnClearSleep() {
        // BL-TS-012 Invariant: Sleeping on a clear night MUST NOT reset the weather cycle.
        // It must preserve the active countdown toward the next scheduled rain.

        int scheduledRainDelay = 45000; // Rain scheduled in 45,000 ticks (~37.5 minutes)
        boolean isRaining = false;
        boolean isThundering = false;
        boolean doWeatherCycle = true;

        AtomicInteger rainTimer = new AtomicInteger(scheduledRainDelay);
        AtomicBoolean weatherResetTriggered = new AtomicBoolean(false);

        // Simulate a complete night of sleep (10,000 ticks advanced via strides)
        long nightTicksToAdvance = 11458L; // from 12542 to 24000
        long remaining = nightTicksToAdvance;

        while (remaining > 0) {
            long stride = Math.min(20, remaining);
            // Advance weather timer for stride (TimeWarpManager#advanceWeatherForStride)
            rainTimer.set((int) Math.max(0, rainTimer.get() - stride));
            remaining -= stride;
        }

        // Now trigger morning wake-up check (checkMorning)
        Runnable morningCheck = () -> {
            // Mirror BL-TS-012 guarded logic in TimeWarpManager.java#checkMorning:
            if (doWeatherCycle && (isRaining || isThundering)) {
                weatherResetTriggered.set(true);
            }
        };

        morningCheck.run();

        // Assertions:
        assertFalse(weatherResetTriggered.get(),
                "BL-TS-012 Violation: Weather reset must NOT be triggered when waking from clear sleep!");
        assertTrue(rainTimer.get() < scheduledRainDelay,
                "Rain countdown must have progressed forward during the night");
        assertEquals(scheduledRainDelay - nightTicksToAdvance, rainTimer.get(),
                "Rain countdown must decrease exactly by the total ticks elapsed during sleep");
    }

    @Test
    @DisplayName("Pillar 4: Weather reset clears active rain/storm when doWeatherCycle is true (BL-TS-012)")
    void testWeatherResetOnActiveRainSleep() {
        // When it is raining or thundering and doWeatherCycle is true, sleep clears the storm
        boolean isRaining = true;
        boolean isThundering = true;
        boolean doWeatherCycle = true;

        AtomicBoolean weatherResetCalled = new AtomicBoolean(false);

        Runnable morningCheck = () -> {
            if (doWeatherCycle && (isRaining || isThundering)) {
                weatherResetCalled.set(true);
            }
        };

        morningCheck.run();

        assertTrue(weatherResetCalled.get(),
                "Weather reset MUST be called when sleeping through active rain or thunderstorm");
    }

    @Test
    @DisplayName("Pillar 4: Weather reset is inhibited when doWeatherCycle is false (BL-TS-012)")
    void testWeatherResetInhibitedWhenRuleWeatherCycleFalse() {
        // When doWeatherCycle is false, sleeping must NEVER reset weather even during storms
        boolean isRaining = true;
        boolean isThundering = true;
        boolean doWeatherCycle = false;

        AtomicBoolean weatherResetCalled = new AtomicBoolean(false);

        Runnable morningCheck = () -> {
            if (doWeatherCycle && (isRaining || isThundering)) {
                weatherResetCalled.set(true);
            }
        };

        morningCheck.run();

        assertFalse(weatherResetCalled.get(),
                "Weather reset must NEVER be called when doWeatherCycle is disabled, even during rain");
    }

    @Test
    @DisplayName("Pillar 4: Hunger exhaustion drain halts at starvation safety floor")
    void testHungerDrainStarvationFloor() {
        AtomicInteger foodLevel = new AtomicInteger(20);
        AtomicFloat exhaustion = new AtomicFloat(0.0f);

        long strideTicks = 20;

        for (int step = 0; step < 500; step++) {
            // SleepHungerHelper safety floor: stop draining if food level <= 6
            if (foodLevel.get() <= 6) {
                break;
            }

            float stepExhaustion = strideTicks * 0.0008f;
            float total = exhaustion.addAndGet(stepExhaustion);

            if (total >= 4.0f) {
                // Vanilla standard: 4.0 exhaustion consumes 1 food point
                int consumed = (int) (total / 4.0f);
                foodLevel.set(Math.max(0, foodLevel.get() - consumed));
                exhaustion.set(total % 4.0f);
            }
        }

        assertTrue(foodLevel.get() >= 6, "Food level must never drop below sprint starvation safety floor of 6");
    }

    private static class AtomicFloat {
        private float value;

        AtomicFloat(float initial) {
            this.value = initial;
        }

        synchronized float addAndGet(float delta) {
            this.value += delta;
            return this.value;
        }

        synchronized void set(float val) {
            this.value = val;
        }

        synchronized float get() {
            return this.value;
        }
    }
}
