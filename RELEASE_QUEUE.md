# 📋 Vanilla Outsider: True Sleep Release Queue & Backlog (MC 1.20.1)

This file tracks which built versions (from the central archive folder "E:\Minecraft Project\Vanilla Outsider Collections\True Sleep\Archive Jar of all versions\MC 1.20.1") have been published.
Open this file in your editor and change `[ ]` to `[x]` when you publish a version.

## 🚀 Published & Backlog Queue

- [x] **`1.0.0+1.20.1`** (2026-10-12) - - Initial ground-zero backport to Minecraft 1.20.1 (Oldest Studio Anchor) with full feature parity. - - Time Warp Engine: smooth daylight acceleration with multiplayer dimension isolation (`[BL-TS-004]`), dynamic mob freezing, biological stasis, and machine acceleration. - - In-game Brigadier command suite (`/truesleep help, status, get, set, reset, reload`) with 2-way config synchronization. - - Dasik Library 1.1.0+1.20.1 runtime integration.
- [ ] **1.0.1+1.20.1** (2026-10-27) - - **Weather Fix (TimeWarpManager)**: Preserved natural weather progression on clear sleep cycles; sleep now clears sky only during active rain or thunderstorms ([BL-TS-012]).
- [ ] **1.0.2+1.20.1** (2026-10-28) - - **5-Pillar Testing & Sidedness Verification Harness**: Adopted 5-pillar autonomous headless test suite covering Chaos Fuzzing (20k iterations), Concurrency Load (50 threads), Brigadier Command Tree AST verification, GameTest simulation, and Sidedness bytecode contracts ([BL-TS-009]).
