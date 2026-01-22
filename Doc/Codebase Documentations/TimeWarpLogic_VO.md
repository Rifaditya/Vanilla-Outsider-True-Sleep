# Time Warp Logic (Core Mechanic)

## Overview

The `TimeWarpManager` is the brain of the operation. It monitors the "sleep status" of the server and dynamically adjusts the global tick rate.

## State Machine

The logic follows a simple implicit state machine:

1. **Normal Play (Idle)**
    * **Tick Rate**: 20.0 TPS (Default)
    * **Condition**: Nobody is sleeping OR not enough people are sleeping.
    * **Action**: None.

2. **Warping (Active)**
    * **Trigger**: `ServerLevelMixin` reports that `areEnoughSleeping` is TRUE.
    * **Tick Rate**: `warpSpeed` (Default 100.0 TPS).
    * **Action**: The server game loop runs at 5x speed (or configured speed). World simulation happens rapidly.

3. **Waking Up (Termination)**
    * **Trigger**: The world time reaches "Morning" (checked via `ServerLevel.isBrightOutside()`).
    * **Action**:
        * Reset Tick Rate to 20.0 TPS.
        * Manually invoke `wakeUpAllPlayers()` to eject players from beds and reset sleep timers.
        * Reset Weather (Thunder/Rain) to clear.

## Technical Implementation (26.1 API Compatibility)

Due to breaking changes in Minecraft 26.1, standard time-checking methods were removed. We use the following Logic Adapters:

### 1. Checking for Morning

**Old Method:** `level.isDay()` or `level.getDayTime() % 24000`
**New Method (26.1):**

```java
if (level.isBrightOutside()) {
    // It is morning or day
}
```

`isBrightOutside()` checks the skylight level, which directly correlates to the sun's position, making it a robust proxy for "Is it morning yet?".

### 2. Checking Time

**Old Method:** `level.getDayTime()`
**New Method (26.1):**

```java
level.getLevelData().getGameTime()
```

We access `GameTime` via the `LevelData` interface. While `GameTime` is typically total world ticks, in a vanilla context (without explicit `/time set` abuse), modulo 24000 provides an accurate enough day-cycle clock for this purpose.

## Safety Mechanisms

- **Sleep Percentage**: Uses the standard vanilla game rule `playersSleepingPercentage` to respect server settings.
* **Fail-Safe**: If `isBrightOutside()` fails to trigger (e.g., in a dimension with fixed time), the warp might persist indefinitely unless players leave beds. *Note: Logic verifies dimension type before warping.*
