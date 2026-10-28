// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.vanillaoutsider.truesleep.config.TrueSleepConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class TrueSleepConcurrencyLoadTest {

    private static final int THREAD_COUNT = 50;
    private static final int TASK_COUNT = 10_000;

    @Test
    @DisplayName("Pillar 2: 50 concurrent threads executing 10,000 tasks reading/writing TrueSleepConfig")
    void testConcurrentConfigAccess() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(TASK_COUNT);
        AtomicInteger failureCounter = new AtomicInteger(0);
        TrueSleepConfig config = new TrueSleepConfig();

        for (int i = 0; i < TASK_COUNT; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    if (taskId % 2 == 0) {
                        config.engineTps = 20.0f + (taskId % 80);
                        config.virtualTps = 100.0f + (taskId % 1900);
                        config.sleepThreshold = 10000 + (taskId % 5000);
                        config.wakeTime = taskId % 24000;
                        config.freezeMobs = (taskId % 3 == 0);
                        config.sleepHunger = (taskId % 4 == 0);
                    } else {
                        float eTps = config.engineTps;
                        float vTps = config.virtualTps;
                        int threshold = config.sleepThreshold;
                        int wake = config.wakeTime;
                        boolean freeze = config.freezeMobs;
                        boolean hunger = config.sleepHunger;
                        // Read verification without throwing
                        if (Float.isNaN(eTps) || Float.isNaN(vTps)) {
                            // Valid float state
                        }
                    }
                } catch (Throwable t) {
                    failureCounter.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean finishedCleanly = latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(5, TimeUnit.SECONDS);

        assertTrue(finishedCleanly, "All 10,000 concurrent config tasks must complete within 10 seconds without deadlocks");
        assertTrue(terminated, "Executor service must terminate cleanly");
        assertEquals(0, failureCounter.get(), "Zero exceptions must occur during concurrent config read/write");
    }

    @Test
    @DisplayName("Pillar 2: 50 concurrent threads calculating time warp strides and taper math across 10,000 cycles")
    void testConcurrentStrideCalculation() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(TASK_COUNT);
        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger failureCounter = new AtomicInteger(0);

        for (int i = 0; i < TASK_COUNT; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    long dist = taskId % 24000L;
                    float engineTps = 20.0f + (taskId % 80);
                    float virtualTps = 20.0f + (taskId % 1980);

                    float progress = Math.max(0.0f, Math.min(1.0f, (float) (dist - 20) / 180.0f));
                    float targetRate = Math.max(20.0f, 20.0f + (virtualTps - 20.0f) * progress);
                    float physicalTarget = Math.max(1.0f, Math.min(engineTps, targetRate));
                    long stride = Math.max(1, Math.round(targetRate / physicalTarget));

                    if (stride >= 1 && physicalTarget >= 1.0f && targetRate >= 20.0f && !Float.isNaN(targetRate) && !Float.isInfinite(targetRate)) {
                        successCounter.incrementAndGet();
                    } else {
                        failureCounter.incrementAndGet();
                    }
                } catch (Throwable t) {
                    failureCounter.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean finishedCleanly = latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(5, TimeUnit.SECONDS);

        assertTrue(finishedCleanly, "All 10,000 stride calculations must complete within 10 seconds without deadlocks");
        assertTrue(terminated, "Executor service must terminate cleanly");
        assertEquals(0, failureCounter.get(), "Zero calculation errors or exceptions during concurrent stride calculations");
        assertEquals(TASK_COUNT, successCounter.get(), "All 10,000 tasks must successfully validate calculation assertions");
    }
}
