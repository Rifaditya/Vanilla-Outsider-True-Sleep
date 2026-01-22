# True Sleep: Project Summary

**Version:** 1.0.0-26.1 (Targeting Minecraft 26.1-snapshot-4)  
**Dependencies:** Java 25, Fabric Loader >=0.16.9

## 1. Project Philosophy (Vanilla Outsider)

Adheres to the "One Click, One Action" rule.

* **No instant time skips:** Sleeping accelerates the world (500 TPS) instead of jumping to dawn.
* **Simulation integrity:** Crops grow, furnaces smelt, and entities move during the warp.
* **Parity:** Restores functionality broken by high speeds (e.g., Cat gifts).

## 2. Dependencies & Environment

Checked against Protocol `general_agent_protocol.yaml` (Step verified):

* `fabricloader`: `>=0.16.9` (Verified: `>=0.16.9`)
* `minecraft`: `~26.1-` (Verified: `26.1-snapshot-4`)
* `java`: `>=25` (Verified: `>=25`)
* `fabric-api`: `*` (Verified: `*`)

## 3. Technical Implementation

* **Core Logic**: `TimeWarpManager` (State Machine).
  * States: `IDLE` -> `WARP_START` -> `WARPING` -> `WARP_END`.
* **Mixins**:
  * `ServerLevelMixin`: Redirects vanilla `areEnoughSleeping` to `false` to prevent vanilla skip, then injects logic to drive the warp.
  * `CatMixin`: Redirects `getSleepTimer` to trick cats into giving gifts even if the night passed in <5 seconds.
* **26.1 Specifics**:
  * Uses `level.isBrightOutside()` for morning detection.
  * Uses `level.getLevelData().getGameTime()` for time tracking.

## 4. Audit & Safety

* **Network**: No external requests (Offline only).
* **Data**: No telemetry or data collection.
* **OS**: No binary execution.

## 5. Documentation Map

* [Modrinth Description](Modrinth/Description%20Modrinth.md)
* [Logic Documentation](Codebase%20Documentations/Logic_VO.md)
* [Mixin Documentation](Codebase%20Documentations/Mixins_VO.md)
