# Changelog

## [1.3.21+26.2] - 2026-08-03

### Added
- **Data-Driven Machine Tag**: Registered `#truesleep:accelerated_machines` (`TagKey<BlockEntityType<?>>`) in `TrueSleepTags.java` and updated `LevelMixin.java` so third-party mod machines can be accelerated during sleep warp via data-driven tag definitions.
- **Passive Mob Stasis Aging**: Updated `MobMixin.java` to decrement chicken `eggTime` during time warp stasis, preserving egg-laying farm productivity during sleep.

## [1.3.20+26.2] - 2026-07-22

### ⚠️ Version Guard Notice
- Includes zero-dependency ModVersionGuard pre-release protection. Halts startup with an explicit warning banner if run on incompatible Minecraft drops or missing core dependencies to prevent world save corruption.

### Fixed
- **ModVersionGuard Protection Banner**: Updated ModVersionGuard.java to use Knot ClassLoader resolution (Thread.currentThread().getContextClassLoader()) and display explicit pre-release protection warnings upon an API mismatch.

All notable changes to this project will be documented in this file.

## [1.3.19+26.2] - 2026-07-22

### Added
- **Forward Compatibility & Version Guard**: Configured `fabric.mod.json` with `"minecraft": ">=26.2-"` for open-ended forward compatibility. Added zero-dependency `ModVersionGuard` check on startup to display human-readable guidance if an incompatible Minecraft API version is encountered.

## [1.3.18-26.2] - 2026-07-21

### Added

- **Sculk Sensor & Vibration Stasis Guard**: Created `VibrationSystemListenerMixin.java` targeting `VibrationSystem$Listener.handleGameEvent`. Automatically suppresses all game event vibrations while time warp is active, preventing accelerated machines, hoppers, doors, or crops from triggering Sculk Sensors, Sculk Shriekers, or spawning Wardens during sleep.

## [1.3.17-26.2] - 2026-07-21

### Added

- **Smooth LERP Deceleration Tapering**: Implemented smooth LERP deceleration tapering in `TimeWarpManager.java`. Naturally tapers simulation speed and tick rate down to native 20 TPS as morning approaches (`dist [200 -> 20]`), and provides a 15-tick smooth LERP wind-down if sleep is interrupted mid-warp. Eliminates visual sky rotation micro-stutters and sudden tick-rate drops.

## [1.3.16-26.2] - 2026-07-20

### Maintenance & Code Quality

- **Code Quality Audit Remediation**: Performed full audit remediation according to the Council's Auditor standard.
- **Dead Code Cleanup**: Deleted 6 obsolete, unregistered mixin source files (`AgeableMobMixin.java`, `CatMixin.java`, `ClockInstanceMixin.java`, `GameRulesInvoker.java`, `ServerClockManagerMixin.java`, `SocialCoreMixin.java`).
- **License Header Standardization**: Replaced verbose 16-line GPL blocks with mandatory 1-line simplified headers (`// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3`) and added missing `// Verified against: ...` citation comments across all active Java files.
- **JSON Spacing**: Formatted all resource JSON files (`fabric.mod.json`, `en_us.json`, `truesleep.mixins.json`, `truesleep.client.mixins.json`) to standard 2-space indentation.

## [1.3.15-26.2] - 2026-07-20

### Added

- **Weather & Atmosphere Fast-Forwarding**: Integrated fast-forwarding of `rainTime`, `thunderTime`, and `clearWeatherTime` via `ServerLevel`'s `WeatherData` in `TimeWarpManager.java`. Rain and thunder storms now count down proportionally during sleep warp (`stride - 1` ticks per warp tick), allowing storms to clear naturally by morning.

## [1.3.14-26.2] - 2026-07-20

### Added

- **Machine Acceleration Hot-Loop Cache**: Implemented high-performance `ConcurrentHashMap` caching for machine and hopper block entity type evaluations in `LevelMixin.java`. Replaced string regex allocations and registry lookups in the sleeping hot loop with $O(1)$ constant time lookups, bringing machine ticking CPU overhead during sleep down to near zero.

## [1.3.13-26.2] - 2026-07-03

### Added

- **Minecraft 26.2 Port**: Ported the entire codebase to target the official, stable Minecraft 26.2 release.
- **Project Structure Split**: Restructured the project into a multi-version layout (similar to Better Dogs), isolating the 26.1.2 build and the new 26.2 build in their respective directories.

### Changed

- **Client Mixin Target Shift**: Relocated and refactored the client sleep overlay mixin `GuiMixin` to `HudMixin` to target `net.minecraft.client.gui.Hud`, resolving a critical startup crash caused by Mojang's internal class reorganization in 26.2.

## [1.3.13+R-26.1.2] - 2026-06-13

### Fixed

- **GameRule Config Defaults Alignment**: Configured the `truesleep:freeze_mobs` GameRule registration to correctly load its default state from the global template configuration file (`truesleep.json`) rather than using a hardcoded `true` value.

## [1.3.12+R-26.1.2] - 2026-06-13

### Added

- **Production Block Entity Acceleration**: Implemented accelerated ticking (by warp `stride`) for furnaces, blast furnaces, smokers, brewing stands, campfires, and modded generators/crushers/smelters during sleep warp to match the fast-forward speed.
- **Granular GameRule Control**: Added two new independent GameRules `truesleep:accelerate_machines` and `truesleep:accelerate_hoppers` (with matching ModMenu/Cloth Config options) to toggle acceleration for machines and hoppers separately.

### Changed

- **Coupled-Hopper Check**: Accelerated hoppers are restricted only to those directly connected to a machine (facing a machine, or directly below one to drain it). General hoppers (item sorters, hopper clocks, loader lines) are left ticking at normal physical speed to prevent desynchronization of automatic redstone builds.
- **Redstone Lock Protection**: Added an active check on the hopper block state property `ENABLED`. If a hopper is powered/locked by redstone, it refuses to accelerate, guaranteeing that custom redstone filters and sorting logic are completely safe.
- **Create Mod & Kinetic Safety**: Verified complete safety with the Create mod's kinetic networks. Windmills, shafts, and cogs continue ticking at physical speed to maintain torque/stress balance. Replaced target keyword `"mill"` with `"grinder"` to prevent matching Create's windmill bearings. Need more feedback on this, you can send me an issue report for me to check. Thank you.

## [1.3.11+A-26.1.2] - 2026-06-13

### Changed

- **GameRule Naming & Tooltip Clarification**: Renamed confusing TPS-related GameRules to standard, user-friendly names (`Engine TPS` -> `Performance Limit`, `Virtual TPS` -> `Time Speed`) and rewrote all GameRule tooltips to explain exactly how they impact performance and fast-forward speeds in plain language.

## [1.3.10+A-26.1.2] - 2026-06-13

### Fixed

- **Classloader Startup Crash**: Relocated the `MobEffectInstanceExtensions` interface to the root `net.vanillaoutsider.truesleep` package to prevent SpongePowered Mixin classloader from interpreting it as a mixin class and throwing `IllegalClassLoadError`.

## [1.3.9+A-26.1.2] - 2026-06-13

### Changed

- **Hybrid Stride/Ticking Warp Engine**: Refactored TimeWarpManager to cap physical engine tick rate at `ENGINE_TPS` and skip ticks by a calculated `stride` multiplier to deliver a lag-free visual time-lapse.
- **Crop Growth Acceleration**: Scaled chunk random ticking (`randomTickSpeed` GameRule) by the warp stride (capped at a safe limit of 500) to ensure crops, copper, and leaves grow/age proportionally with the time warp.
- **Entity Aging & Potion Decays**: Implemented recursive potion effect duration decay for all living entities (players and mobs) and age increments/decrements for `AgeableMob` baby growth/breeding cooldowns.
- **Agrarian Reform Compatibility**: Verified complete compatibility with Agrarian Reform's offline growth catch-up (The Continuum) and polyculture/rain boosts.

## [1.3.8+A-26.1.2] - 2026-06-13

### Fixed

- **Time Synchronization (TimeWarpManager)**: Resolved a critical time desync bug on waking up where the server time snapped to morning but the client sky rendering/screen remained stuck at night. Now explicitly sets and broadcasts the snaps through the dimension's default `WorldClock` using the native 26.1.2 `ServerClockManager`.

## [1.3.7+A-26.1.2] - 2026-06-13

### Fixed

- **Client Crash (GuiMixin)**: Corrected the Mixin target from legacy `renderSleepOverlay` to the valid `extractSleepOverlay` method, fixing a critical `InvalidInjectionException` startup crash on Minecraft 26.1.2.

## [1.3.6+A-26.1.2] - 2026-06-13

### Changed

- **Build Configuration**: Removed the snapshot naming suffix from `archives_base_name` to produce clean, release-ready jar filenames.

## [1.3.5+A-26.1.2] - 2026-06-13

### Changed

- **Performance Optimization (MobMixin / TimeWarpManager)**: Cached `MOBS_FROZEN` and `WORKER_MOBS_FROZEN` GameRules once per tick to eliminate GameRules engine lookup overhead in the entity ticking hot path.

## [1.3.4+B-26.1.2] - 2026-06-13

### Changed

- **Engine Refactor (TimeWarpManager)**: Replaced the time-skipping stride engine with native `TickRateManager` TPS acceleration.
- **Tapering Deceleration**: Added smooth LERP deceleration tapering down to 20 TPS within 200 ticks of waking up.
- **Client Celestial Smoothing**: Restructured `SkyRendererMixin` to query level tickrate (> 20 TPS) to support multiplayer.

### Added

- **Optional GUI (Cloth Config)**: Implemented ModMenu and Cloth Config configuration screens under a client-side optional Hybrid Pattern.
- **Library Updates**: Updated `fabric-api` to `0.145.4+26.1.2` and `dasik-library` to `1.8.2` to resolve compilation errors.

### Removed

- **Legacy Engine Cleanup**: Deleted 7 obsolete classes (`AgeableMobMixin`, `CatMixin`, `ClockInstanceMixin`, `ServerClockManagerMixin`, `SocialCoreMixin`, `QuietClockInstance`, `QuietClockManager`) made redundant by native ticking.

## [1.3.3+B-26.1.2] - 2026-06-12

### Fixed

- **Performance (MobMixin)**: Added a static cache for unfreeze GameRules to prevent string concatenation and `Identifier.parse` allocations in the entity tick hot path.
- **Localization (en_us.json)**: Corrected GameRule translation keys to use the correct `gamerule.truesleep` namespace prefix instead of `gamerule.minecraft` prefix.
- **Verification Standards**: Added GPLv3 license headers and verification citations to all Java/Mixin source files.
- **Platform Polish**: Converted CurseForge platform description to raw HTML rendering to meet CurseForge rendering specifications.
- **Build & CI**: Enabled local JDK 25 path configuration and added missing `Doc/Marketing/` entry to `.gitignore`.

## [1.3.2+build.11] - 2026-02-23

### Fixed

- **Bug (ServerLevelMixin)**: Added a second `@Redirect` on `SleepStatus.areEnoughDeepSleeping()` in `ServerLevelMixin`. Vanilla's wake-up block gates on `areEnoughSleeping(pct) && areEnoughDeepSleeping(pct, players)`. The first redirect (on `areEnoughSleeping`) was already suppressing the block, but the new redirect on `areEnoughDeepSleeping` provides belt-and-suspenders insurance. This guarantees vanilla can **never** call `moveToTimeMarker(WAKE_UP_FROM_SLEEP)` (which snaps the clock to vanilla dawn) or `wakeUpAllPlayers()` — ensuring the mod's `truesleep_wake_time` GameRule is always the sole authority on when players wake.

## [1.3.2+build.10] - 2026-02-22


### Changed

- **Tooltips**: Added real-time tip to both Engine TPS and Virtual TPS game rule descriptions — "Set both values to the same number for a truly real-time night (no time dilation, 1:1 real speed)."

## [1.3.2+build.9] - 2026-02-22


### Changed

- **Engine TPS** (`truesleep_engine_tps`): Max cap raised from 1,000 to unlimited (`Integer.MAX_VALUE`). Tooltip updated with safe-ceiling warning — recommended max on a normal system is **100**; beyond **200** risks instability.
- **Virtual TPS** (`truesleep_virtual_tps`): Max cap raised from 10,000 to unlimited (`Integer.MAX_VALUE`). Tooltip notes the practical ceiling is ~**20,000** (night in ~1 real second); higher values offer no additional benefit.
- **Wake Time** (`truesleep_wake_time`): Max corrected from 24,000 to **23,999**. A full day is 24,000 ticks — setting 24,000 equals 0 (Dawn) and is a meaningless target. Tooltip now includes a full time-of-day reference guide.
- **Sleep Threshold** (`truesleep_sleep_threshold`): Max corrected from 24,000 to **23,999** for the same reason.

### Removed

- **Stability Clamp**: The hidden guard that silently forced Engine TPS back to 50 on every world load (if the config value was ≥ 99) has been removed. Users now own their configuration choices completely.

## [1.3.2+build.8] - 2026-02-22


### Fixed

- **Bug (TimeWarpManager)**: Fixed dead-code bug where players would wake up at the wrong time. The clock-snap correction was correctly calculated but never applied. The warp now calls `setGameTime(snappedTime)` to land players on exactly the configured `truesleep_wake_time` tick before calling `wakeUpAllPlayers`.
- **Bug (ServerLevelMixin)**: True Sleep now fully respects the vanilla `playersSleepingPercentage` game rule. Previously, the warp could start the instant a player entered a bed, bypassing vanilla's "sleeping long enough" deep-sleep gate. The warp now requires both `areEnoughSleeping(percentage)` **and** `areEnoughDeepSleeping(percentage, players)` — exactly mirroring vanilla's condition — so 25% means 25% of online players must be fully asleep before the warp engages.

## [1.3.2+build.7] - 2026-02-22


### Fixed

- **Critical Bug (MobMixin)**: Fixed dynamic mob GameRule toggles having no effect. The `MobMixin` was querying the `DynamicGameRuleManager.getDynamicRules()` cache which could be empty due to initialization timing. The lookup now queries `BuiltInRegistries.GAME_RULE.getValue(ruleId)` directly, which is always reliable. This means `/gamerule ts_unfreeze_minecraft_zombie true` now correctly allows Zombies to tick during sleep.

## [1.3.2+build.6] - 2026-02-22

### Fixed

- **Zenith Compliance**: Added missing `truesleep$` namespace prefix to all 10 private Mixin injection methods (`MobMixin`, `EntityMixin`, `AgeableMobMixin`, `ServerLevelMixin`, `SocialCoreMixin`, `BedRuleMixin`, `CatMixin`, `GuiMixin`, `SkyRendererMixin`) to satisfy Zenith modid-prefix rule.
- **Localization**: Added missing translation key `gamerule.category.truesleep.mob_settings` (`"True Sleep Mobs"`) to `en_us.json` — the \"True Sleep Mobs\" game rule category was previously unlocalised.
- **Documentation**: Fixed stray junk text in `Description Curseforge.md` (CurseForge platform page).

## [1.3.2+build.5] - 2026-02-21

### Added

- **Dynamic Mob Unfreeze Toggles**: Added a new in-game GameRule for *every* individual entity type (e.g., `ts_unfreeze_minecraft_villager`, `ts_unfreeze_minecraft_iron_golem`, including modded ones) to allow players to completely customize which mobs are allowed to tick during the True Sleep time warp. Perfect for preventing redstone contraptions and farms from breaking.
- **Dedicated Category**: Created a new "True Sleep Mobs" GameRule category to organize all the individual mob toggles in the in-game UI.

### Recommended

- **Collapsible Game Rules**: Highly recommended to install this mod, as True Sleep now registers ~150+ new GameRules (one for each mob).

## [1.3.2+build.4] - 2026-02-21

### Fixed

- **Compatibility**: Reverted Mixin compatibility level from `JAVA_25` to `JAVA_22` to resolve warning.
- **Cleanup**: Removed stale `refmap` entries from `truesleep.mixins.json` and `truesleep.client.mixins.json`.

## [1.3.2+build.3] - 2026-02-21

### Fixed

- **Build**: Updated Minecraft target from `26.1-snapshot-4` to `26.1-snapshot-8`.
- **Build**: Updated DasikLibrary to `1.6.9+build.5`.
- **Bug**: `EntityMixin` now respects the `DROWN_IMMUNITY` GameRule (was unconditionally resetting air supply).
- **Data**: Removed invalid `minecraft:copper_golem` from `worker_mobs` entity tag.
- **Localization**: Added missing `en_us.json` entries for `truesleep_freeze_mobs` and `truesleep_freeze_workers` GameRules.

### Removed

- **Dead Code**: Deleted `PlayerMixin.java` (contained only empty debug injection methods).

## [1.3.2+build.2] - 2026-02-19

### Changed

- **Code Refactor**: Migrated cycle-distance and day calculation logic to `DasikLibrary.TimeUtil`.
- **Performance**: Switched to native `GlobalSocialSystem.setThrottle` API for regulated performance during Time Warping.
- **Dependencies**: Updated `DasikLibrary` to `1.6.9+build.3`.

### Fixed

- **Social Throttling**: Corrected Mixin target package for `GlobalSocialSystem`.

### Fixed

- Finalized stability for Snapshot 6.

## [1.3.1-26.1] - 2026-02-05

### Fixed

- Critical Startup Crash: Removed redundant and malformed `LivingEntityMixin`.

## [1.3.0-26.1] - 2026-02-04

### Added (Worker Freedom)

- **Worker Exemption**: Added `truesleep:worker_mobs` tag for entities that should NOT freeze during time warp (Default: Allay, Villager, Copper Golem).
- **Worker Configuration**: Added `truesleep_freeze_workers` Gamerule (Default: `false`). Set to `true` to freeze them like everyone else.
- **Tag Flexibility**: Users can now add any mob to `truesleep:worker_mobs` via datapack to let them move during sleep.

## [1.2.0-26.1] - 2026-02-04

### Added

- **Cryogenic Stasis**: Mobs are now completely frozen (stasis) during sleep warp. This prevents pathfinding lag, drowning, and starvation.
- **Configurable Stasis**: Added "Truesleep Freeze Mobs" gamerule (`truesleep_freeze_mobs`, Default: true). Disable to return to "hyper speed" mob movement (risky!).
- **Redstone Fidelity**: We now prioritize raw Engine TPS over virtual tick skipping, ensuring Redstone and Hoppers run at true accelerated speed without breaking clocks.

## [1.1.4-26.1] - 2026-01-28

### Changed (Polish)

- **Localization**: Rewrote Game Rule tooltips (`en_us.json`) to be highly descriptive. Added detailed explanations for Engine TPS, Virtual TPS, and Sleep Thresholds to help users configure the mod safely.

## [1.1.3-26.1] - 2026-01-28

### Fixed (Integration)

- **Golden Dandelion**: Mobs with age-locked status (via Golden Dandelion) no longer age rapidly during Time Warp.

## [1.1.2-26.1] - 2026-01-27

### Fixed (Emergency Patch)

- **Integrated Server Crash**: Resolved `IllegalClassLoadError` by correctly relocating bridge interfaces out of the mixin package.

## [1.1.1-26.1] - 2026-01-27

### Fixed (Stability & Visual Polish)

- **Aquatic Breaching**: Prevented fish and other water-breathing entities from leaping out of water during time warp.
- **Deep-Sea AI Lobotomy**: Resolved "Sky Lag" (server jitter) by enforcing complete AI suppression for submerged entities.
- **Throttled Buoyancy**: Optimized survival buoyancy for land animals to once every 10 ticks, preventing physics compounding at high TPS.
- **Celestial Smoothing**: Implemented client-side interpolation for the sun, moon, and stars to ensure perfectly smooth movement during time warp.

## [1.1.0-26.1] - 2026-01-27

### Added (Dreamweaver Update)

- **Dreamweaver Engine**: Integrated advanced sleep/wake cycle controls.
- **Hybrid Config**: Implemented persistent global template logic with per-world Native Game Rule overrides.

## [1.0.7-26.1] - 2026-01-27

### Fixed (Community Harden)

- **Solar Desync**: Fixed "laggy sky" and wake-up failures by implementing "Quiet Sync" (advancing dimensions without packet spam).
- **Pulmonary Stasis**: Implemented biological stasis for entities during warp to prevent drowning.
- **Loyalty Bridge**: Migrated configuration to Native Game Rules (`/gamerule`) while preserving legacy JSON settings.

## [1.0.6-26.1] - 2026-01-27

### Added (Stability Hotfix)

- **Drown Guard (Biological Stasis)**: All entities are now immune to drowning damage during the Quantum Warp.
- **Submerged Mob AI**: Implemented forced jump logic for mobs in water to ensure they stay afloat during high-speed simulation.

### Changed (Optimization)

- **Golden Ratio Engine**: Lowered default `engineTps` to **50.0f**. This doubles the server's MSPT budget (20ms) and eliminates sky lag (client-side stutter).
- **High-Fidelity Stride**: Moved to a **20x Stride** to maintain the 1000 Virtual TPS target on optimized hardware settings.

## [1.0.5-26.1] - 2026-01-27

### Added (Quantum Stride)

- **Quantum Warp Engine**: Introduced Stride-based tick skipping to resolve server lag (0% MSPT overhead).
- **Simulation Injection**: Native catch-up for Entity Aging (`tickCount`), Child Growth (`age`), and Random Ticks (Crops/Blocks).
- **Smooth Visuals**: 100 Engine TPS ensures sky movement is 5x smoother than vanilla while warping.
- **Auto-Restoration**: Failsafe restoration of original `RandomTickSpeed` gamerule when warping ends.

### Changed in 1.0.5

- Renamed `warpSpeed` config to `virtualTps` and added `engineTps` (Default 100).
- Retired the legacy "Chronos Drive" in favor of the Quantum Stride architecture.

## [1.0.4-26.1] - 2026-01-27

### Added (Chronos Edition)

- **Chronos Engine**: Ultra-fast 1000 TPS "Hyperspace" sleep support.
- **AI Lobotomy**: Surgical AI suppression for mobs during warp to ensure server stability.
- **Shadow Governor**: Modular Hive-Mind regulation (Better Dogs compatible).
- **Survival Physics**: Mobs in fluids retain swimming/jumping capabilities during warp.
- **Stasis Padding**: Suppressed fall distance accumulation during high-speed AI suppression.
- **Sunrise Taper**: Smooth deceleration near morning for reliable wake-up triggers.

### Fixed in 1.0.4

- **Log Purge**: Completely removed all `System.out.println` and debug stack traces.
- **Silent Sleep**: Removed noisy redirect logs for vanilla sleep skip suppression.

## [1.0.3-26.1] - 2026-01-27

### Changed in 1.0.3

- Replaced mod icon with a new design.

## [1.0.2-26.1] - 2026-01-22

### Fixed in 1.0.2

- Added missing mod icon to the JAR file.

## [1.0.1-26.1] - 2026-01-22

### Changed in 1.0.1

- Removed the vanilla screen darkening overlay when sleeping. This ensures the "Time Warp" effect is clearly visible without obstruction.
- Updated documentation banners with new standardized artwork.

## [1.0.0-26.1] - 2026-01-21

### Added in 1.0.0

- Initial Release of **Vanilla Outsider: True Sleep**.
- **Time Warp Logic**: Accelerates world ticks during sleep instead of skipping the night.
- **Visual Feedback**: Players watch the celestial bodies move rapidly across the sky.
- **Cat Gift Parity**: Custom logic ensures cats still give gifts despite the shortened night duration.
- **Multiplayer Support**: One player sleeping accelerates time for everyone; configurable via game rules.
