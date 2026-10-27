# Changelog

All notable changes to **Vanilla Outsider: True Sleep** for Minecraft 1.20.1 will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [1.0.1+1.20.1] - 2026-10-27

### Fixed
- Fixed an issue where waking up from sleep unconditionally reset the weather cycle on clear nights, wiping the countdown to natural rain. Sleeping on a clear night now preserves natural weather progression, while sleeping during rain or thunderstorms properly clears the skies.

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
