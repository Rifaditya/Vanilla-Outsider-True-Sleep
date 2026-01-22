# Moderator Audit Helper: True Sleep

**Mod Name:** True Sleep
**Mod ID:** `truesleep` (Fabric)
**Version:** 1.0.0 (Targeting Minecraft 26.1 Snapshot 4)

## 🛡️ Safety & Compliance Statement

To assist Platform Moderators (Modrinth/CurseForge) in auditing this project, I certify the following:

1. **No External Network Connections**: This mod does **NOT** make any HTTP/Web requests. It runs entirely offline within the Minecraft game loop.
2. **No Data Collection**: This mod does **NOT** collect, store, or transmit any user data, telemetry, or analytics.
3. **No Binary Execution**: This mod does **NOT** execute any external binaries or OS-level commands.

## 📂 Codebase Overview for Reviewers

The codebase is small and focused on a single gameplay mechanic: changing how "Sleep" works.

| Feature | Source File | Description |
| :--- | :--- | :--- |
| **Main Logic** | `net.vanillaoutsider.truesleep.logic.TimeWarpManager` | Manages the State Machine (Sleeping -> Accelerating -> Waking). Interacts with `TickRateManager`. |
| **Mixin Hook** | `net.vanillaoutsider.truesleep.mixin.ServerLevelMixin` | **Critical implementation detail**. Redirects vanilla sleep check to `false`. |
| **Config** | `net.vanillaoutsider.truesleep.config.TrueSleepConfig` | Simple POJO for GSON. Reads `config/truesleep.json`. |

## 🔍 Specific "Suspicious" Patterns Explained

Moderators looking at Mixins might flag the following patterns. Here is the justification:

### 1. `ServerLevelMixin` returning `false` for sleep check

* **Location**: `ServerLevelMixin.java` -> `disableVanillaSleepSkip`
* **Code**: returns `false` when vanilla asks `areEnoughSleeping`.
* **Context**: This is INTENTIONAL gameplay logic.
* **Reason**: I must prevent the vanilla game from "skipping" the night instantly (fade to black) so that my mod can instead "accelerate" the night (tick warp). If I allowed vanilla to return `true`, the night would skip before my logic runs.

### 2. Manipulation of Tick Rate

* **Location**: `TimeWarpManager.java`
* **Reason**: This is the core feature of the mod ("Time Warp"). It speeds up world simulation when players are sleeping. It resets to 20.0 TPS immediately upon waking.

### 3. CatMixin manipulating Sleep Timer

* **Location**: `CatMixin.java` -> `bypassSleepTimerForGifts`
* **Code**: Returns `100` instead of actual sleep timer.
* **Reason**: **Gameplay Parity.** Time Warp makes the night pass too quickly for the vanilla "5 second minimum sleep" check to trigger. This mixin ensures players still get cat gifts (phantom membrane, rabbit foot, etc.) even if the night passed instantly.

## 🛠️ Build & dependencies

* **Loader**: Fabric Loader

* **Mappings**: Official Minecraft Mappings (Mojang) - *Note: Source may use direct names like `ServerLevel` instead of `class_XYZ`.*
* **External Libs**: None (Uses Fabric API).
