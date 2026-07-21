# History

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

- **Bug (ServerLevelMixin)**: Added a second `@Redirect` on `SleepStatus.areEnoughDeepSleeping()` in `ServerLevelMixin` to provide belt-and-suspenders insurance and guarantee vanilla can never snap the clock to dawn.

## [1.3.2+build.7] - 2026-02-22

### Fixed

- **Critical Bug (MobMixin)**: Fixed dynamic mob GameRule toggles (`ts_unfreeze_*`) having no effect. Root cause: `getDynamicRules()` cache was not reliably populated. Fix: query `BuiltInRegistries.GAME_RULE.getValue(ruleId)` directly.

## [1.3.2+build.6] - 2026-02-22

### Fixed

- **Zenith Compliance**: Added `truesleep$` prefix to all 10 private Mixin injection methods across `MobMixin`, `EntityMixin`, `AgeableMobMixin`, `ServerLevelMixin`, `SocialCoreMixin`, `BedRuleMixin`, `CatMixin`, `GuiMixin`, and `SkyRendererMixin`.
- **Localization**: Added missing `gamerule.category.truesleep.mob_settings` key to `en_us.json`.
- **Documentation**: Removed stray junk text in CurseForge platform page.

## v1.3.2+build.5 - 2026-02-21

- **Feature**: Added dynamic GameRule toggles for *every* individual entity type in the game under a new "True Sleep Mobs" category, allowing players to explicitly unfrozen specific mobs (like Villagers, Iron Golems, or modded creatures) during the time warp to preserve redstone mechanisms.

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

## [1.3.2-26.1] - 2026-02-05

### Fixed

- Finalized stability for Snapshot 6.

## [1.3.1-26.1] - 2026-02-05

### Fixed

- Critical Startup Crash: Removed redundant and malformed `LivingEntityMixin`.

## [1.3.0-26.1] - 2026-02-05

### Added (Worker Freedom)

- **Worker Exemption**: Added `truesleep:worker_mobs` tag for entities that should NOT freeze during time warp (Default: Allay, Villager, Copper Golem).
- **Worker Configuration**: Added `truesleep_freeze_workers` Gamerule (Default: `false`). Set to `true` to freeze them like everyone else.
- **Tag Flexibility**: Users can now add any mob to `truesleep:worker_mobs` via datapack to let them move during sleep.

## [1.2.0-26.1] - 2026-02-04

### Added

- **Cryogenic Stasis**: Mobs are now completely frozen (stasis) during sleep warp. This prevents pathfinding lag, drowning, and starvation.
- **Configurable Stasis**: Added "Truesleep Freeze Mobs" gamerule (`truesleep_freeze_mobs`, Default: true). Disable to return to "hyper speed" mob movement (risky!).
- **Redstone Fidelity**: We now prioritize raw Engine TPS over virtual tick skipping, ensuring Redstone and Hoppers run at true accelerated speed without breaking clocks.
