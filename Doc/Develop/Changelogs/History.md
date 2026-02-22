# History

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
