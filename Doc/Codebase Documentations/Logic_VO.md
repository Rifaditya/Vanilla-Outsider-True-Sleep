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
        * Call `server.tickRateManager().setTickRate(50.0f)`.
        * **Quantum Stride**: The logic simulates multiple game ticks per server tick to achieve **1000 Virtual TPS**.

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

### 2. The Mob Unfreeze (Stasis) Logic

* **Core Engine**: To prevent pathfinding lag and server jitter during high-speed warping, mobs are conventionally frozen in stasis via `MobMixin.truesleep$freezeDuringWarp`.
* **Dynamic Unfreezing**: Build.6 introduced a dynamic unfreeze system.
    * Every `EntityType` in the game (including modded ones) is assigned a `ts_unfreeze_<namespace>_<path>` GameRule.
    * If this rule is `true`, the mob is exempt from stasis and continues to tick at the accelerated rate. This is essential for **redstone contraptions and automated farms** (e.g., iron farms, villager-based sorters) that require specific entity logic to remain active during the Warp.
* **Worker Mobs**: The internal `truesleep:worker_mobs` tag acts as a default filter for entities that should always move (Villagers, etc.), but this can be overridden per-entity type via GameRules.

### 3. The Cat Gift Fix

* **Problem**: Vanilla Cats (`Cat.java`) have a hardcoded check:

    ```java
    if (player.getSleepTimer() >= 100) { giveGift(); }
    ```

    This requires 100 ticks (5 seconds) of sleeping.
* **Conflict**: At 500 TPS, a full night (12000 ticks) passes in `12000 / 500 = 24 ticks` (1.2 seconds).
* **Result**: The player never reaches `sleepTimer = 100`. Cats never give gifts.
* **Solution**: `CatMixin` intercepts the check.
  * If `TimeWarpManager.hasRecentWarp()` is true (meaning we just finished a super-fast night), it forces the check to pass by returning `100`.
