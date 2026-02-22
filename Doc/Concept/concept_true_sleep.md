# concept_true_sleep.md

## Philosophy

**True Sleep** repairs the broken simulation loop of vanilla Minecraft. Instead of "deleting" time (skipping to morning), it **Fast-Forwards** time. This preserves world consistency: furnaces smelt, crops grow, and the moon travels across the sky.

## Core Variable: The Stride

- **Logic**: The game engine runs at accelerated speed (e.g. 50-100 TPS) while skipping logic ticks (if necessary) to simulate massive time passage (1000 Virtual TPS).
- **Redstone/Physics**: Prioritize raw **Engine TPS**. We avoid "virtual" skipping for Redstone to prevent breaking clocks.

## Mechanics

### 1. Quantum Stride (Environment)

- **Sun/Moon**: Visibly streak across the sky.
- **Vegetation**: Random ticks are scaled up, causing crops/saplings to grow rapidly.
- **Machines**: Block Entities (Furnaces, Hoppers) tick at the **Accelerated Engine Rate**.

### 2. Cryogenic Stasis & Selective Unfreeze (Build 7)
>
> [!IMPORTANT]
> **Mobs are FROZEN by default. Selective unfreeze via Gamerule.**

- **Feature**: Dynamic GameRule per entity type (`ts_unfreeze_<namespace>_<path>`).
- **Behavior (Frozen)**:
  - **Mobs**: Completely frozen during warp (No tick, no AI, no movement). Saves significant TPS at 1000 Virtual TPS.
- **Behavior (Unfrozen)**:
  - **Mobs**: Tick rapidly with the engine. **Essential for redstone contraptions/farms** that rely on specific mob logic (e.g., villager breeders, iron farms) to continue operating during the warp.

## Feature Parity Checklist

| # | Feature | Implementation | Config/Gamerule |
|---|---|---|---|
| 1 | Sleep triggers Time Warp | `TimeWarpManager` | `truesleep_sleep_threshold` |
| 2 | Sun/Moon move visually | `ServerLevelMixin` | - |
| 3 | Redstone/Furnaces run fast | Native `TickRateManager` | `truesleep_engine_tps` |
| 4 | Mobs Freeze (Performance) | `MobMixin` (tick cancel) | `ts_unfreeze_*` (per-mob) |
| 5 | Redstone Contraptions | selective unfreeze | `ts_unfreeze_villager` etc. |
| 6 | Non-Sleeping Players Active | `Player` excluded | - |
