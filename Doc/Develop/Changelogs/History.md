# History

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
