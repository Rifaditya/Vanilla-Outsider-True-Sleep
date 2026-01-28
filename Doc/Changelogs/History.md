# Historical Changelog (True Sleep)

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
