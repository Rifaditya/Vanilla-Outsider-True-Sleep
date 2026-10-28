# Changelog

All notable changes to **Vanilla Outsider: True Sleep** for Minecraft 1.20.1 will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [1.0.3+1.20.1] - 2026-10-28

### Changed
- **Universal Brigadier Command Suite Migration ([BL-TS-010])**: Migrated command registration to centralized `UniversalCommandRegistry` and `ModCommandDispatcher` from DasikLibrary 1.4.0+1.20.1.
- **Saturated Arithmetic & Reactive Bridging**: Integrated `GameRuleCommandBridge` providing two-way synchronization between active server GameRules and disk configuration with non-blocking saturated warnings on extreme boundary inputs.
- **Boilerplate Reduction**: Purged manual command boilerplate and individual setters, reducing codebase complexity by over 65% while maintaining complete backward compatibility across all 11 simulation and stasis settings.

## [1.0.2+1.20.1] - 2026-10-28

### Added
- **5-Pillar Automated Testing Harness ([BL-TS-009])**: Introduced comprehensive headless testing suite spanning Chaos Property Fuzzing (20,000+ iterations), Virtual Concurrency Simulator (50 worker threads), Zero-Mock Brigadier AST Verification, Time Warp Simulation, and Bytecode Sidedness Contracts.

## [1.0.1+1.20.1] - 2026-10-27

### Fixed
- Fixed an issue where waking up from sleep unconditionally reset the weather cycle on clear nights, wiping the countdown to natural rain. Sleeping on a clear night now preserves natural weather progression, while sleeping during rain or thunderstorms properly clears the skies. *(Reported by Night Dragon(Jasmine) on Discord)*

## [1.0.0+1.20.1] - 2026-10-12

### Added
- Ground-zero backport of **Vanilla Outsider: True Sleep** to Minecraft 1.20.1 (Oldest Studio Anchor).
- Smooth accelerated daylight time warp engine with multiplayer dimension isolation (`[BL-TS-004]`).
- Dynamic sleeping time threshold evaluation in `ServerPlayer.startSleepInBed`.
- Biological stasis helper for sheep wool grazing simulation, chicken egg timers, and baby animal aging.
- Machine and hopper block entity acceleration with data-driven tags (`#truesleep:accelerated_machines`).
- Client-side sleep darkening overlay removal for smooth daytime transition.
- In-game Brigadier command suite (`/truesleep help, status, get, set, reset, reload`) with 2-way configuration sync.
- Dynamic GameRules registered via `DasikLibrary` 1.1.0+1.20.1 API.
