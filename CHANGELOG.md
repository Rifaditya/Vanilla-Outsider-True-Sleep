# Changelog

## [1.3.31+26.3] - 2026-10-11

### Changed
- Purged unremapped runtime reflection hazard (`ModVersionGuard`) to ensure 100% Knot ClassLoader runtime safety across production servers and development environments.
- Aligned project metadata in `fabric.mod.json` with official issue tracker links and Modrinth platform attributes under GPL-3.0-or-later.

---

## [1.3.30+26.3] - 2026-10-10

### Fixed
- Added dimension sleep capability check (`canSleepThroughNights()`) to prevent non-sleeping dimensions (Nether, End) from querying or interfering with sleep status.
- Restored dynamic version template token in `fabric.mod.json`.

---

## [1.3.29+26.3] - 2026-10-10

### Fixed
- **Dimension-Isolated Time Warp Engine (Issue #2)**: Fixed multiplayer sleep deadlocks where awake players in other dimensions (The Nether, The End, or custom dimensions) continuously aborted active Overworld time warp simulations. Simulation ticking, acceleration, and wind-down abort checks are now strictly bound to the sleeping dimension's level key.

---

## [1.3.28+26.3] - 2026-09-16

### Changed
- Removed redundant `minecraft:soul_campfire` from `#truesleep:accelerated_machines` tag (shared BlockEntityType).

---

## [1.3.25+26.3] - 2026-08-18

### Added
- **Sleep Hunger Reduction Setting**: Added `SleepHungerHelper.java` to simulate natural metabolic exhaustion when players sleep through the fast-forwarded night. Drains ~3-4 hunger points over a full night using standard vanilla saturation-first exhaustion (`player.causeFoodExhaustion()`).
- **Starvation Safety Floor**: Exhaustion automatically clamps at `6` food points (3 drumsticks), ensuring players never take starvation damage in bed and always retain the ability to sprint upon waking.
- **Granular Sleep Hunger Control**: Registered `truesleep:sleep_hunger` GameRule (Boolean, default: `true`), synced with `config/truesleep.json` and integrated into `/truesleep set/get/status sleep_hunger`.

## [1.3.24+26.3] - 2026-08-18

### Added
- **Expanded Biological & Farm Stasis Aging**: Introduced `BiologicalStasisHelper.java` to simulate natural grazing wool regrowth on grass for sheared sheep (`sheep.isSheared()`), chicken egg-laying timers, baby animal growth, breeding cooldowns, armadillo scute timers, and allay duplication cooldowns during time warp stasis.
- **Beehive & Bee Nest Production Acceleration**: Added `minecraft:beehive` to `#truesleep:accelerated_machines` tag and `LevelMixin.java` fast-path string checks, allowing internal bee honey accumulation to complete during sleep warp.
- **Granular Biological Aging Control**: Registered `truesleep:biological_aging` GameRule (Boolean, default: `true`), synced with `config/truesleep.json` and `/truesleep set/get/status biological_aging`.

## [1.3.23+26.3] - 2026-08-18

### Changed
- **Zero-Allocation Mob Stasis Hot Loop**: Refactored `MobMixin.java` with a static `EntityType<?>` worker tag cache and sentinel-based dynamic GameRule lookup, eliminating `Optional` allocations and registry holder sweeps during sleep warp.
- **Fast-Path Ticker Acceleration**: Refactored `LevelMixin.java` to pre-classify machine block entity types directly from ticker type strings, avoiding chunk block entity lookups for non-production blocks.
- **Empty Potion Effect Fast-Path**: Added `.isEmpty()` guard to `LivingEntityMixin.java` and `MobMixin.java` effect aging loops to eliminate iterator allocations on effectless entities.

## [1.3.22+26.3] - 2026-08-18

### Added
- **Minecraft 26.3 Modern Sovereign Target**: Ported codebase to target Minecraft 26.3 snapshots natively with open-ended forward bounds (`>=26.3-`) and ModVersionGuard protection.
- **Dedicated Brigadier Command Suite (`/truesleep`)**: Full in-game command suite (`/truesleep help`, `status`, `get`, `set`, `reset`, `reload`) with rich tab completion, permission controls (`LEVEL_GAMEMASTERS`), and 2-way synchronization between active world `GameRules` and `config/truesleep.json`.
