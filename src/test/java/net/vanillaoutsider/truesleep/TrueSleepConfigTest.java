// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.vanillaoutsider.truesleep.config.TrueSleepConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrueSleepConfigTest {

    @Test
    void testConfigDefaults() {
        TrueSleepConfig config = new TrueSleepConfig();
        config.resetDefaults();

        assertEquals(50.0f, config.engineTps);
        assertEquals(1000.0f, config.virtualTps);
        assertEquals(12542, config.sleepThreshold);
        assertEquals(0, config.wakeTime);
        assertTrue(config.drownImmunity);
        assertTrue(config.freezeMobs);
        assertTrue(config.accelerateMachines);
        assertTrue(config.accelerateHoppers);
        assertTrue(config.biologicalAging);
        assertTrue(config.sleepHunger);
    }
}
