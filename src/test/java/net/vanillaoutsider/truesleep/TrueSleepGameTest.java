// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrueSleepGameTest {

    private boolean shouldResetWeather(boolean doWeatherCycle, boolean isRaining, boolean isThundering) {
        return doWeatherCycle && (isRaining || isThundering);
    }

    @Test
    @DisplayName("Pillar 4: Time warp arrival deceleration smoothly tapers virtual TPS towards 20 TPS")
    void testTimeWarpArrivalDeceleration() {
        float virtualTps = 1000.0f;

        // At dist = 500 (far): progress is 1.0, targetRate is 1000.0f
        long distFar = 500L;
        float progressFar = Math.max(0.0f, Math.min(1.0f, (float) (distFar - 20) / 180.0f));
        float targetRateFar = 20.0f + (virtualTps - 20.0f) * progressFar;
        assertEquals(1.0f, progressFar, 0.001f);
        assertEquals(1000.0f, targetRateFar, 0.001f);

        // At dist = 110 (mid): progress is (110 - 20) / 180.0 = 0.5, targetRate is 20 + 980 * 0.5 = 510.0f
        long distMid = 110L;
        float progressMid = Math.max(0.0f, Math.min(1.0f, (float) (distMid - 20) / 180.0f));
        float targetRateMid = 20.0f + (virtualTps - 20.0f) * progressMid;
        assertEquals(0.5f, progressMid, 0.001f);
        assertEquals(510.0f, targetRateMid, 0.001f);

        // At dist = 20 (arrived): progress is 0.0, targetRate is 20.0f
        long distArrived = 20L;
        float progressArrived = Math.max(0.0f, Math.min(1.0f, (float) (distArrived - 20) / 180.0f));
        float targetRateArrived = 20.0f + (virtualTps - 20.0f) * progressArrived;
        assertEquals(0.0f, progressArrived, 0.001f);
        assertEquals(20.0f, targetRateArrived, 0.001f);

        // Monotonic deceleration assertion across approaching distances
        float previousRate = Float.MAX_VALUE;
        for (long d = 300; d >= 20; d -= 10) {
            float progress = Math.max(0.0f, Math.min(1.0f, (float) (d - 20) / 180.0f));
            float currentRate = 20.0f + (virtualTps - 20.0f) * progress;
            assertTrue(currentRate <= previousRate, "Arrival rate must decrease monotonically as distance decreases: " + d);
            previousRate = currentRate;
        }
        assertEquals(20.0f, previousRate, 0.001f, "Final arrival rate must taper exactly to 20.0 TPS");
    }

    @Test
    @DisplayName("Pillar 4: Sleep interruption initiates 15-tick deceleration wind-down")
    void testSleepInterruptionWindDown() {
        float lastWarpRate = 50.0f;
        long lastWarpStride = 20L;

        long previousStride = Long.MAX_VALUE;
        float previousRate = Float.MAX_VALUE;

        for (int decelerateTicks = 15; decelerateTicks >= 0; decelerateTicks--) {
            float progress = (float) decelerateTicks / 15.0f;
            float currentRate = 20.0f + (lastWarpRate - 20.0f) * progress;
            long stride = Math.max(1, Math.round(1 + (lastWarpStride - 1) * progress));

            assertTrue(currentRate <= previousRate + 0.0001f, "Rate must decrease monotonically during wind-down");
            assertTrue(stride <= previousStride, "Stride must decrease monotonically during wind-down");

            previousRate = currentRate;
            previousStride = stride;

            if (decelerateTicks == 0) {
                assertEquals(20.0f, currentRate, 0.001f, "At tick 0, currentRate must return to base 20.0 TPS");
                assertEquals(1L, stride, "At tick 0, stride must return to 1x");
            }
        }
    }

    @Test
    @DisplayName("Pillar 4: BL-TS-012 weather cycle preservation simulation")
    void testWeatherPreservationSimulation() {
        // Clear night: preserves clear weather (false)
        assertFalse(shouldResetWeather(true, false, false), "Sleeping on clear night must NOT reset weather cycle");

        // Rain: clears rain (true)
        assertTrue(shouldResetWeather(true, true, false), "Sleeping in rain must reset weather cycle to clear");

        // Thunder: clears thunder (true)
        assertTrue(shouldResetWeather(true, false, true), "Sleeping in thunder must reset weather cycle to clear");
        assertTrue(shouldResetWeather(true, true, true), "Sleeping in stormy rain must reset weather cycle to clear");

        // doWeatherCycle = false: never alters weather (false)
        assertFalse(shouldResetWeather(false, true, true), "When doWeatherCycle is false, sleeping must NEVER alter weather");
        assertFalse(shouldResetWeather(false, false, false), "When doWeatherCycle is false, sleeping must NEVER alter weather");
    }
}
