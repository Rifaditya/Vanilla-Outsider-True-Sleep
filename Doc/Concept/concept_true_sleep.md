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

### 2. Cryogenic Stasis (Entities)
>
> [!IMPORTANT]
> **Mobs are FROZEN by default. Configurable via Gamerule.**

- **Feature**: `truesleep_freeze_mobs` (Default: `true`)
- **Behavior (Enabled)**:
  - **Mobs**: Completely frozen during warp (No tick, no AI, no movement, no breath/physics). Prevents lag and death.
  - **Players**: Not frozen.
- **Behavior (Disabled)**:
  - **Mobs**: Tick rapidly with the engine. Move fast, age fast, burn fast. Warning: May cause drowning or starvation if warp is long.

## Feature Parity Checklist

| # | Feature | Implementation | Config/Gamerule |
|---|---|---|---|
| 1 | Sleep triggers Time Warp | `TimeWarpManager` | `truesleep_sleep_threshold` |
| 2 | Sun/Moon move visually | `ServerLevelMixin` | - |
| 3 | Redstone/Furnaces run fast | Native `TickRateManager` | `truesleep_engine_tps` |
| 4 | Mobs Freeze (No move/breath) | `MobMixin` (tick cancel) | `truesleep_freeze_mobs` |
| 5 | Non-Sleeping Players Active | `Player` excluded from `MobMixin` | - |
