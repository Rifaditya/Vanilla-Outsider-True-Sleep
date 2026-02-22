# Project Architecture: True Sleep

## Philosophy: Vanilla Outsider

This mod adheres to the "One Click, One Action" philosophy. Sleeping does not skip the night; instead, it accelerates the passage of time. This ensures that:

- **World Simulation Preserved**: Furnaces smelt, crops grow, and mobs move during the night.
- **Multiplayer Fairness**: One player sleeping accelerates time for everyone, but doesn't force a skip unless a majority agrees (configurable via vanilla game rules, though this mod overrides the skip itself).
- **Diegetic Feedback**: Players see the sun/moon move across the sky rapidly rather than a simplified "fade to black".

## Component Structure

The project is organized into the following packages:

### `net.vanillaoutsider.truesleep` (Root)

- **`TrueSleep.java`**: The main mod entry point. Responsible for loading configuration and initializing the mod.

### `net.vanillaoutsider.truesleep.config`

- **`TrueSleepConfig.java`**: A simple POJO (Plain Old Java Object) used to serialize/deserialize configuration options using GSON.
- **Settings**:
  - `engineTps` (float): The actual server tick rate during warp (Default: 50.0).
  - `virtualTps` (float): The target simulated tick rate (Default: 1000.0).

### `net.vanillaoutsider.truesleep.logic`

- **`TimeWarpManager.java`**: The core logic engine.
  - Implements a singleton pattern.
  - Manages the state machine (Sleeping -> Warping -> Waking).
  - Interacts with the vanilla `TickRateManager` to safely accelerate the game loop.
  - Handles the "Wake Up" trigger when morning arrives.

### `net.vanillaoutsider.truesleep.mixin`

- **`ServerLevelMixin.java`**: The bridge between vanilla code and mod logic.
  - **Redirects**: Intercepts `areEnoughSleeping` via `truesleep$silentSleepSuppression` to prevent native time-skip.
  - **Injects**: Hooks into `tick` via `truesleep$manageTimeWarp` to feed data to the Manager.
  - **`CatMixin.java`**: Logic patch for gameplay parity.
    - **Redirects**: Bypasses the requirement via `truesleep$bypassSleepTimerForGifts`.
  - **`MobMixin.java`**: Implements the cryogenic stasis and the dynamic unfreeze system (`truesleep$freezeDuringWarp`).

### `net.vanillaoutsider.truesleep.mixin.client`

- **`GuiMixin.java`**: Client-side visual adjustments.
  - **Injects**: Cancels `renderSleepOverlay` in `Gui` to prevent the screen from darkening, allowing players to watch the time warp.

## Dependencies

- **Fabric Loader**: Modding platform.
- **Fabric API**: Standard compatibility layer.
- **Mixin**: For modifying vanilla behavior.
