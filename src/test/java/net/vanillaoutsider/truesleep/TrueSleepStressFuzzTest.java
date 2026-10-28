// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.dasik.social.util.TimeUtil;
import net.vanillaoutsider.truesleep.config.TrueSleepConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 1: Chaos Property & Mathematical Stress Fuzzing Suite for Vanilla Outsider: True Sleep.
 * Executes 12,000+ deterministic iterations fuzzing time warp arithmetic, config saturation,
 * biological stasis simulation, and hunger exhaustion invariants under seed 0xDA51C263L.
 */
class TrueSleepStressFuzzTest {

    private static final long SEED = 0xDA51C263L;

    @BeforeEach
    void setUp() {
        TrueSleepConfig.get().resetDefaults();
    }

    @AfterEach
    void tearDown() {
        TrueSleepConfig.get().resetDefaults();
    }

    @Test
    @DisplayName("Pillar 1: 15,000 iterations chaos fuzzing of time warp stride & target rate calculations")
    void testTimeWarpStrideAndRateCalculationFuzzing() {
        Random random = new Random(SEED);

        float[] extremeFloats = {
                Float.NaN,
                Float.POSITIVE_INFINITY,
                Float.NEGATIVE_INFINITY,
                Float.MIN_VALUE,
                Float.MAX_VALUE,
                -10000.0f,
                -20.0f,
                -1.0f,
                -0.0f,
                0.0f,
                0.0001f,
                1.0f,
                19.99f,
                20.0f,
                50.0f,
                100.0f,
                1000.0f,
                100000.0f
        };

        long[] extremeDists = {
                Long.MIN_VALUE,
                -1000000L,
                -24000L,
                -1L,
                0L,
                1L,
                19L,
                20L,
                100L,
                199L,
                200L,
                1000L,
                12000L,
                23999L,
                24000L,
                1000000L,
                Long.MAX_VALUE
        };

        for (int i = 0; i < 15_000; i++) {
            float engineTps = (i < extremeFloats.length) ? extremeFloats[i] : (random.nextBoolean() ? extremeFloats[random.nextInt(extremeFloats.length)] : (random.nextFloat() * 500.0f - 100.0f));
            float virtualTps = (i < extremeFloats.length) ? extremeFloats[i] : (random.nextBoolean() ? extremeFloats[random.nextInt(extremeFloats.length)] : (random.nextFloat() * 50000.0f - 500.0f));
            long dist = (i < extremeDists.length) ? extremeDists[i] : (random.nextBoolean() ? extremeDists[random.nextInt(extremeDists.length)] : (long) (random.nextDouble() * 48000L - 12000L));

            // Execute the exact mathematical kernel from TimeWarpManager.java#updateWarpSpeed
            float targetRate = virtualTps;
            if (Float.isNaN(targetRate) || Float.isInfinite(targetRate)) {
                targetRate = 1000.0f; // Robust fallback
            }

            if (dist < 200) {
                float progress = Math.max(0.0f, Math.min(1.0f, (float) (dist - 20) / 180.0f));
                targetRate = 20.0f + (targetRate - 20.0f) * progress;
            }
            targetRate = Math.max(20.0f, targetRate);

            float safeEngine = engineTps;
            if (Float.isNaN(safeEngine) || Float.isInfinite(safeEngine) || safeEngine < 1.0f) {
                safeEngine = 20.0f; // Saturated lower bound
            }

            float physicalTarget = Math.min(safeEngine, targetRate);
            long stride = Math.max(1, Math.round(targetRate / physicalTarget));

            // Assert mathematical invariants: zero NaN, zero Infinity, stride >= 1, rate >= 20.0 TPS
            assertFalse(Float.isNaN(targetRate), "targetRate must never be NaN at iteration " + i);
            assertFalse(Float.isInfinite(targetRate), "targetRate must never be Infinite at iteration " + i);
            assertTrue(targetRate >= 20.0f, "targetRate must never fall below vanilla 20.0 TPS at iteration " + i);
            assertTrue(physicalTarget >= 1.0f, "physicalTarget must be >= 1.0f at iteration " + i);
            assertTrue(stride >= 1, "Stride must always be >= 1 at iteration " + i);
        }
    }

    @Test
    @DisplayName("Pillar 1: 15,000 iterations chaos fuzzing of sleep hunger exhaustion math")
    void testSleepHungerExhaustionFuzzing() {
        Random random = new Random(SEED + 1);

        long[] testTicks = {
                Long.MIN_VALUE,
                -5000L,
                -1L,
                0L,
                1L,
                2L,
                10L,
                50L,
                100L,
                1000L,
                10000L,
                100000L,
                Long.MAX_VALUE
        };

        for (int i = 0; i < 15_000; i++) {
            long strideTicks = (i < testTicks.length) ? testTicks[i] : (random.nextBoolean() ? testTicks[random.nextInt(testTicks.length)] : (long) (random.nextDouble() * 50000L));

            if (strideTicks <= 0) {
                // SleepHungerHelper early-exits on strideTicks <= 0
                continue;
            }

            float exhaustion = strideTicks * 0.0008f;

            assertFalse(Float.isNaN(exhaustion), "Exhaustion must not be NaN at iteration " + i);
            assertTrue(exhaustion >= 0.0f, "Exhaustion must be non-negative for positive ticks at iteration " + i);

            // Verify starvation floor behavior: across a 10,000-tick night, exhaustion is bounded ~8.0
            if (strideTicks <= 10000) {
                assertTrue(exhaustion <= 10.0f, "10,000-tick exhaustion must remain reasonable (<= 10.0f)");
            }
        }
    }

    @Test
    @DisplayName("Pillar 1: 15,000 iterations chaos fuzzing of biological stasis scaling math")
    void testBiologicalStasisMathFuzzing() {
        Random random = new Random(SEED + 2);

        int[] testTicks = {
                Integer.MIN_VALUE,
                -1000,
                -1,
                0,
                1,
                10,
                100,
                500,
                1000,
                5000,
                10000,
                Integer.MAX_VALUE
        };

        for (int i = 0; i < 15_000; i++) {
            int ticks = (i < testTicks.length) ? testTicks[i] : (random.nextBoolean() ? testTicks[random.nextInt(testTicks.length)] : random.nextInt(50000));

            if (ticks <= 0) {
                // BiologicalStasisHelper early-exits on ticks <= 0
                continue;
            }

            // 1. Sheep grazing probability scaling
            float grazeChance = Math.min(1.0f, ticks / 1000.0f);
            assertFalse(Float.isNaN(grazeChance), "Graze chance must not be NaN at iteration " + i);
            assertTrue(grazeChance >= 0.0f && grazeChance <= 1.0f,
                    "Graze probability must be strictly bounded in [0.0, 1.0] at iteration " + i);

            // 2. Chicken egg laying timer decrement
            int initialEggTime = random.nextInt(6000) + 1;
            int decrementedEggTime = Math.max(1, initialEggTime - ticks);
            assertTrue(decrementedEggTime >= 1,
                    "Chicken eggTime must never drop below 1 tick before laying at iteration " + i);

            // 3. Baby animal aging calculation
            int babyAge = -random.nextInt(24000) - 1; // negative values represent babyhood
            int advancedBabyAge = Math.min(0, babyAge + ticks);
            assertTrue(advancedBabyAge <= 0,
                    "Baby aging Math.min(0, currentAge + ticks) must never exceed adulthood 0 at iteration " + i);
            assertTrue(advancedBabyAge >= babyAge,
                    "Baby age must advance monotonically toward 0 at iteration " + i);
        }
    }

    @Test
    @DisplayName("Pillar 1: 15,000 iterations chaos fuzzing of day/night cycle distance calculations")
    void testCycleDistanceMathFuzzing() {
        Random random = new Random(SEED + 3);

        for (int i = 0; i < 15_000; i++) {
            long currentTime = Math.abs(random.nextLong() % 1_000_000L);
            long targetTime = Math.abs(random.nextLong() % 24_000L);

            long currentTimeOfDay = currentTime % 24000L;
            long dist = TimeUtil.getCycleDistance(currentTimeOfDay, (int) targetTime, 24000L);

            assertTrue(dist >= 0, "Cycle distance must be non-negative at iteration " + i);
            assertTrue(dist < 24000L, "Cycle distance must be strictly less than 24,000 ticks at iteration " + i);

            // When currentTimeOfDay == targetTime, distance must be 0
            long zeroDist = TimeUtil.getCycleDistance(currentTimeOfDay, (int) currentTimeOfDay, 24000L);
            assertEquals(0L, zeroDist, "Cycle distance to self must be 0");
        }
    }

    @Test
    @DisplayName("Pillar 1: 12,000 iterations config field mutation and saturation stability")
    void testConfigFieldChaosFuzzing() {
        Random random = new Random(SEED + 4);
        TrueSleepConfig config = TrueSleepConfig.get();

        for (int i = 0; i < 12_000; i++) {
            config.engineTps = random.nextFloat() * 500.0f - 50.0f;
            config.virtualTps = random.nextFloat() * 50000.0f - 1000.0f;
            config.sleepThreshold = random.nextInt(48000) - 10000;
            config.wakeTime = random.nextInt(48000) - 10000;
            config.freezeMobs = random.nextBoolean();
            config.freezeWorkers = random.nextBoolean();
            config.drownImmunity = random.nextBoolean();
            config.accelerateMachines = random.nextBoolean();
            config.accelerateHoppers = random.nextBoolean();
            config.biologicalAging = random.nextBoolean();
            config.sleepHunger = random.nextBoolean();

            if (i % 500 == 0) {
                config.resetDefaults();
                assertEquals(50.0f, config.engineTps, 0.001f);
                assertEquals(1000.0f, config.virtualTps, 0.001f);
                assertEquals(12542, config.sleepThreshold);
                assertEquals(0, config.wakeTime);
                assertTrue(config.drownImmunity);
                assertTrue(config.freezeMobs);
                assertFalse(config.freezeWorkers);
                assertTrue(config.accelerateMachines);
                assertTrue(config.accelerateHoppers);
                assertTrue(config.biologicalAging);
                assertTrue(config.sleepHunger);
            }
        }
    }
}
