# Logic Documentation: True Sleep

## Core Concept: The Time Warp

Instead of "skipping" time (setting the time to day), True Sleep **accelerates** time.
This preserves all world simulation (crops grow, entities move, furnaces smelt) but at a hyper-fast rate.

## State Machine (`TimeWarpManager.java`)

The manager operates as a singleton State Machine driven by the server tick loop.

### States

1. **IDLE (Normal Gameplay)**
    * **Condition**: `isWarping = false`
    * **Action**: Do nothing. Let vanilla run at 50ms/tick (20 TPS).

2. **WARP_START (Transition)**
    * **Trigger**: All players in the dimension are sleeping (`sleepStatus.areEnoughSleeping()`).
    * **Action**:
        * Set `isWarping = true`.
        * Call `server.tickRateManager().setTickRate(500.0f)`.
        * Server accelerates to ~500 TPS.

3. **WARPING (Active)**
    * **Condition**: `isWarping = true` **AND** `areEnoughSleeping = true`.
    * **Action**:
        * Monitor `level.isBrightOutside()`.
        * If it becomes bright (Morning), trigger **WARP_END**.

4. **WARP_ABORT (Safety)**
    * **Condition**: `isWarping = true` **BUT** `areEnoughSleeping = false`.
    * **Reason**: A player left bed or disconnected mid-warp.
    * **Action**: Immediate **WARP_END**.

5. **WARP_END (Reset)**
    * **Action**:
        * Set `isWarping = false`.
        * Reset tick rate to `20.0f`.
        * Record `lastWarpTime` (for Cat Gift logic).
        * Wake up all players.

## Specialized Logic

### 1. The Multi-Dimension Fix

* **Problem**: Use of `ServerLevel.tick` means the logic runs for Overworld, Nether, and End *independently*.
* **Bug**: The Nether usually has 0 players. `sleepStatus.areEnoughSleeping()` returns `false` for empty worlds.
* **Consequence**: The Nether tick loop sees "0 sleepers" and triggers **WARP_ABORT**, cancelling the warp for the Overworld player.
* **Solution**: `ServerLevelMixin` now explicitly **ignores** any level with `players().isEmpty()`. The Nether no longer votes on Time Warp.

### 2. The Cat Gift Fix

* **Problem**: Vanilla Cats (`Cat.java`) have a hardcoded check:

    ```java
    if (player.getSleepTimer() >= 100) { giveGift(); }
    ```

    This requires 100 ticks (5 seconds) of sleeping.
* **Conflict**: At 500 TPS, a full night (12000 ticks) passes in `12000 / 500 = 24 ticks` (1.2 seconds).
* **Result**: The player never reaches `sleepTimer = 100`. Cats never give gifts.
* **Solution**: `CatMixin` intercepts the check.
  * If `TimeWarpManager.hasRecentWarp()` is true (meaning we just finished a super-fast night), it forces the check to pass by returning `100`.
