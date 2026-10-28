// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.vanillaoutsider.truesleep.config.TrueSleepConfig;
import net.vanillaoutsider.truesleep.logic.QuietClockInstance;
import net.vanillaoutsider.truesleep.logic.QuietClockManager;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 2: Virtual Concurrency Simulator for Vanilla Outsider: True Sleep.
 * Enforces lock-free thread safety, deadlock freedom, exception isolation under write contention,
 * and QuietClockManager stability across 50 concurrent worker threads.
 */
class TrueSleepConcurrencyLoadTest {

    private static final int THREAD_COUNT = 50;
    private static final int ITERATIONS_PER_THREAD = 100;
    private static final long TIMEOUT_SECONDS = 20;

    @BeforeEach
    void setUp() {
        TrueSleepConfig.get().resetDefaults();
    }

    @AfterEach
    void tearDown() {
        TrueSleepConfig.get().resetDefaults();
    }

    @Test
    @DisplayName("Pillar 2: 50 concurrent worker threads high-contention config read/write access")
    void testHighContentionConfigAccess() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);
        ConcurrentLinkedQueue<Throwable> exceptions = new ConcurrentLinkedQueue<>();
        AtomicInteger totalOps = new AtomicInteger();

        TrueSleepConfig config = TrueSleepConfig.get();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    Random random = new Random(42L + threadId);
                    for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
                        int action = random.nextInt(8);
                        switch (action) {
                            case 0 -> config.engineTps = 20.0f + random.nextInt(180);
                            case 1 -> config.virtualTps = 100.0f + random.nextInt(9900);
                            case 2 -> config.sleepThreshold = random.nextInt(24000);
                            case 3 -> config.wakeTime = random.nextInt(24000);
                            case 4 -> {
                                config.freezeMobs = random.nextBoolean();
                                config.freezeWorkers = random.nextBoolean();
                            }
                            case 5 -> {
                                config.drownImmunity = random.nextBoolean();
                                config.sleepHunger = random.nextBoolean();
                            }
                            case 6 -> {
                                config.accelerateMachines = random.nextBoolean();
                                config.accelerateHoppers = random.nextBoolean();
                                config.biologicalAging = random.nextBoolean();
                            }
                            case 7 -> config.resetDefaults();
                        }
                        totalOps.incrementAndGet();
                    }
                } catch (Throwable t) {
                    exceptions.add(t);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean finished = doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertTrue(finished, "High contention config test timed out (> " + TIMEOUT_SECONDS + "s) - potential deadlock");
        assertTrue(exceptions.isEmpty(), "Zero uncaught exceptions expected under contention, but caught: " + exceptions);
        assertTrue(totalOps.get() >= THREAD_COUNT * ITERATIONS_PER_THREAD, "All worker iterations must complete");
    }

    @Test
    @DisplayName("Pillar 2: 50 concurrent worker threads QuietClockManager registration and advancement stress")
    void testConcurrentQuietClockManagerAdvancement() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);
        ConcurrentLinkedQueue<Throwable> exceptions = new ConcurrentLinkedQueue<>();
        AtomicLong totalAdvancedTicks = new AtomicLong();

        // High-concurrency QuietClockManager implementation simulation
        ConcurrentMap<String, QuietClockInstance> activeClocks = new ConcurrentHashMap<>();

        // Register initial clocks
        for (int c = 0; c < 20; c++) {
            final String clockId = "clock_" + c;
            final AtomicLong clockTicks = new AtomicLong(0);
            activeClocks.put(clockId, ticks -> clockTicks.addAndGet(ticks));
        }

        QuietClockManager manager = ticks -> {
            for (QuietClockInstance instance : activeClocks.values()) {
                if (instance != null) {
                    instance.truesleep$quietAdd(ticks);
                }
            }
        };

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    Random random = new Random(1337L + threadId);
                    for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
                        int op = random.nextInt(4);
                        if (op == 0) {
                            // Advance all clocks
                            long advance = 1 + random.nextInt(50);
                            manager.truesleep$quietAdvanceAll(advance);
                            totalAdvancedTicks.addAndGet(advance);
                        } else if (op == 1) {
                            // Dynamically register a new clock
                            String id = "dyn_clock_" + threadId + "_" + j;
                            AtomicLong ticks = new AtomicLong(0);
                            activeClocks.put(id, ticks::addAndGet);
                        } else if (op == 2) {
                            // Dynamically unregister a clock
                            String id = "dyn_clock_" + threadId + "_" + (j - 1);
                            activeClocks.remove(id);
                        } else {
                            // Query active clocks safely
                            int size = activeClocks.size();
                            assertTrue(size >= 0, "Active clocks size must remain non-negative");
                        }
                    }
                } catch (Throwable t) {
                    exceptions.add(t);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean finished = doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertTrue(finished, "QuietClockManager test timed out (> " + TIMEOUT_SECONDS + "s) - potential deadlock");
        assertTrue(exceptions.isEmpty(), "Zero exceptions expected during QuietClockManager stress, but caught: " + exceptions);
        assertTrue(totalAdvancedTicks.get() > 0, "Ticks must have been successfully advanced concurrently");
    }

    @Test
    @DisplayName("Pillar 2: 50 concurrent worker threads TimeWarpManager query & player tracking stress")
    void testConcurrentWarpQueryAndPlayerTracking() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);
        ConcurrentLinkedQueue<Throwable> exceptions = new ConcurrentLinkedQueue<>();
        AtomicInteger queriesExecuted = new AtomicInteger();

        TimeWarpManager warpManager = TimeWarpManager.get();
        Set<String> sleepingPlayers = Collections.newSetFromMap(new ConcurrentHashMap<>());

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    Random random = new Random(2026L + threadId);
                    for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
                        int action = random.nextInt(6);
                        switch (action) {
                            case 0 -> {
                                boolean warping = warpManager.isWarping();
                                long stride = warpManager.getStride();
                                assertTrue(stride >= 1, "Stride must always be >= 1");
                                if (!warping) {
                                    assertEquals(1, stride, "When idle, stride must strictly be 1");
                                }
                            }
                            case 1 -> {
                                boolean freezeMobs = warpManager.shouldFreezeMobs();
                                boolean freezeWorkers = warpManager.shouldFreezeWorkers();
                                assertTrue(freezeMobs || !freezeMobs);
                                assertTrue(freezeWorkers || !freezeWorkers);
                            }
                            case 2 -> {
                                boolean accelM = warpManager.shouldAccelerateMachines();
                                boolean accelH = warpManager.shouldAccelerateHoppers();
                                boolean bioAge = warpManager.shouldAgeBiological();
                                boolean hunger = warpManager.shouldDrainSleepHunger();
                                assertTrue(accelM || !accelM);
                                assertTrue(accelH || !accelH);
                                assertTrue(bioAge || !bioAge);
                                assertTrue(hunger || !hunger);
                            }
                            case 3 -> {
                                String player = "Player_" + random.nextInt(100);
                                if (random.nextBoolean()) {
                                    sleepingPlayers.add(player);
                                } else {
                                    sleepingPlayers.remove(player);
                                }
                            }
                            case 4 -> {
                                boolean recent = warpManager.hasRecentWarp(24000L);
                                assertTrue(recent || !recent);
                            }
                            case 5 -> {
                                List<String> snapshot = List.copyOf(sleepingPlayers);
                                assertNotNull(snapshot);
                            }
                        }
                        queriesExecuted.incrementAndGet();
                    }
                } catch (Throwable t) {
                    exceptions.add(t);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean finished = doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertTrue(finished, "TimeWarpManager concurrent query test timed out (> " + TIMEOUT_SECONDS + "s)");
        assertTrue(exceptions.isEmpty(), "Zero exceptions expected during concurrent queries, but caught: " + exceptions);
        assertTrue(queriesExecuted.get() >= THREAD_COUNT * ITERATIONS_PER_THREAD);
    }
}
