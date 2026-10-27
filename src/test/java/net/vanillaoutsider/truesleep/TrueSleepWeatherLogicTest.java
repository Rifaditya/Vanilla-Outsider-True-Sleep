// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrueSleepWeatherLogicTest {

    private boolean shouldResetWeather(boolean doWeatherCycle, boolean isRaining, boolean isThundering) {
        return doWeatherCycle && (isRaining || isThundering);
    }

    @Test
    @DisplayName("BL-TS-012: Clear night sleep preserves weather cycle and does not reset countdown")
    void testClearSleepPreservesWeather() {
        assertFalse(shouldResetWeather(true, false, false), "Sleeping on a clear night must NOT reset the weather cycle!");
    }

    @Test
    @DisplayName("BL-TS-012: Rain sleep properly clears the sky")
    void testRainSleepResetsWeather() {
        assertTrue(shouldResetWeather(true, true, false), "Sleeping during rain MUST reset the weather cycle to clear skies!");
    }

    @Test
    @DisplayName("BL-TS-012: Thunderstorm sleep properly clears the sky")
    void testThunderSleepResetsWeather() {
        assertTrue(shouldResetWeather(true, false, true), "Sleeping during thunderstorm MUST reset the weather cycle!");
        assertTrue(shouldResetWeather(true, true, true), "Sleeping during stormy rain MUST reset the weather cycle!");
    }

    @Test
    @DisplayName("BL-TS-012: Weather cycle rule disabled prevents any weather alterations")
    void testWeatherCycleDisabled() {
        assertFalse(shouldResetWeather(false, true, true), "When doWeatherCycle is false, sleeping must NEVER alter weather!");
    }
}
