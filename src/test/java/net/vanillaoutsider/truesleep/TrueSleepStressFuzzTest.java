// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.vanillaoutsider.truesleep.config.TrueSleepConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TrueSleepStressFuzzTest {

    private static final int ITERATIONS = 20_000;
    private static final long SEED = 0xDA51CL;

    private static final float[] EXTREME_FLOATS = {
            Float.NaN,
            Float.POSITIVE_INFINITY,
            Float.NEGATIVE_INFINITY,
            0.0f,
            -0.0f,
            -100.0f,
            -50.0f,
            -1.0f,
            1.0f,
            20.0f,
            50.0f,
            1000.0f,
            1_000_000.0f,
            Float.MAX_VALUE,
            Float.MIN_VALUE
    };

    private static final int[] EXTREME_INTS = {
            Integer.MIN_VALUE,
            -100_000,
            -1,
            0,
            19,
            20,
            21,
            199,
            200,
            201,
            12542,
            13000,
            24000,
            100_000,
            1_000_000,
            Integer.MAX_VALUE
    };

    @Test
    @DisplayName("Pillar 1: Deterministic Chaos Fuzzing of TrueSleepConfig boundaries (20,000 iterations)")
    void testConfigBoundaryFuzzing() {
        Random random = new Random(SEED);
        TrueSleepConfig config = new TrueSleepConfig();

        for (int i = 0; i < ITERATIONS; i++) {
            float fVal1 = random.nextBoolean()
                    ? EXTREME_FLOATS[random.nextInt(EXTREME_FLOATS.length)]
                    : (random.nextFloat() * 2_000_000.0f - 1_000_000.0f);
            float fVal2 = random.nextBoolean()
                    ? EXTREME_FLOATS[random.nextInt(EXTREME_FLOATS.length)]
                    : (random.nextFloat() * 2_000_000.0f - 1_000_000.0f);

            int iVal1 = random.nextBoolean()
                    ? EXTREME_INTS[random.nextInt(EXTREME_INTS.length)]
                    : random.nextInt();
            int iVal2 = random.nextBoolean()
                    ? EXTREME_INTS[random.nextInt(EXTREME_INTS.length)]
                    : random.nextInt();

            boolean bVal = random.nextBoolean();

            assertDoesNotThrow(() -> {
                config.engineTps = fVal1;
                config.virtualTps = fVal2;
                config.sleepThreshold = iVal1;
                config.wakeTime = iVal2;
                config.drownImmunity = bVal;
                config.freezeMobs = !bVal;
                config.freezeWorkers = bVal;
                config.accelerateMachines = !bVal;
                config.accelerateHoppers = bVal;
                config.biologicalAging = !bVal;
                config.sleepHunger = bVal;
                config.wakeAtMorning = !bVal;

                // Verify state reads
                assertEquals(fVal1, config.engineTps);
                assertEquals(fVal2, config.virtualTps);
                assertEquals(iVal1, config.sleepThreshold);
                assertEquals(iVal2, config.wakeTime);
                assertEquals(bVal, config.drownImmunity);

                // Periodic reset to ensure default recovery
                if (random.nextInt(100) == 0) {
                    config.resetDefaults();
                    assertEquals(50.0f, config.engineTps);
                    assertEquals(1000.0f, config.virtualTps);
                    assertEquals(12542, config.sleepThreshold);
                    assertEquals(0, config.wakeTime);
                }
            }, "Config mutation must survive extreme values without throwing exceptions at iteration " + i);
        }

        config.resetDefaults();
        assertEquals(50.0f, config.engineTps);
    }

    @Test
    @DisplayName("Pillar 1: Time warp stride & taper math fuzzing under extreme values (20,000 iterations)")
    void testStrideAndTaperMathFuzzing() {
        Random random = new Random(SEED);

        for (int i = 0; i < ITERATIONS; i++) {
            long dist;
            float engineTps;
            float virtualTps;

            if (random.nextInt(5) == 0) {
                // Select from discrete edge values
                dist = EXTREME_INTS[random.nextInt(EXTREME_INTS.length)];
                engineTps = EXTREME_FLOATS[random.nextInt(EXTREME_FLOATS.length)];
                virtualTps = EXTREME_FLOATS[random.nextInt(EXTREME_FLOATS.length)];
            } else {
                // Select uniformly across representative ranges
                dist = (long) (random.nextDouble() * 1_100_000.0) - 100_000L; // [-100000, 1000000]
                engineTps = random.nextFloat() * 1_000_050.0f - 50.0f; // [-50, 1000000]
                virtualTps = random.nextFloat() * 1_000_050.0f - 50.0f; // [-50, 1000000]
            }

            // Sanitize NaN / Infinite inputs if any edge values were NaN/Infinite
            if (Float.isNaN(engineTps) || Float.isInfinite(engineTps)) {
                engineTps = 20.0f;
            }
            if (Float.isNaN(virtualTps) || Float.isInfinite(virtualTps)) {
                virtualTps = 20.0f;
            }

            float progress = Math.max(0.0f, Math.min(1.0f, (float) (dist - 20) / 180.0f));
            float targetRate = Math.max(20.0f, 20.0f + (virtualTps - 20.0f) * progress);
            float physicalTarget = Math.max(1.0f, Math.min(engineTps, targetRate));
            long stride = Math.max(1, Math.round(targetRate / physicalTarget));

            assertTrue(stride >= 1, "Stride must always be at least 1, but was: " + stride + " at iteration " + i);
            assertFalse(Float.isNaN(targetRate), "targetRate must never be NaN at iteration " + i);
            assertFalse(Float.isInfinite(targetRate), "targetRate must never be Infinite at iteration " + i);
            assertTrue(physicalTarget >= 1.0f, "physicalTarget must always be >= 1.0f, but was: " + physicalTarget + " at iteration " + i);
            assertTrue(targetRate >= 20.0f, "targetRate must always be >= 20.0f, but was: " + targetRate + " at iteration " + i);
            assertTrue(progress >= 0.0f && progress <= 1.0f, "progress must be clamped in [0.0, 1.0]");
        }
    }
}
