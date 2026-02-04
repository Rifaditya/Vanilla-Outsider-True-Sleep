# History

## [1.2.0-26.1] - 2026-02-04

### Added

- **Cryogenic Stasis**: Mobs are now completely frozen (stasis) during sleep warp. This prevents pathfinding lag, drowning, and starvation.
- **Configurable Stasis**: Added "Truesleep Freeze Mobs" gamerule (`truesleep_freeze_mobs`, Default: true). Disable to return to "hyper speed" mob movement (risky!).
- **Redstone Fidelity**: We now prioritize raw Engine TPS over virtual tick skipping, ensuring Redstone and Hoppers run at true accelerated speed without breaking clocks.
