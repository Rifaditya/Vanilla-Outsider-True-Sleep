# Changelog

All notable changes to **Vanilla Outsider: True Sleep** for Minecraft 1.21.11 will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [1.0.1+1.21.11] - 2026-10-27

### Fixed
- Fixed an issue where waking up from sleep unconditionally reset the weather cycle on clear nights, wiping the countdown to natural rain. Sleeping on a clear night now preserves natural weather progression, while sleeping during rain or thunderstorms properly clears the skies. *(Reported by Night Dragon(Jasmine) on Discord)*

## [1.0.0+1.21.11] - 2026-10-11

### Added
- Ground-zero backport of **Vanilla Outsider: True Sleep** to Minecraft 1.21.11 (Winter Drop Anchor).
- Smooth accelerated daylight time warp engine with multiplayer dimension isolation (`[BL-TS-004]`).
- Biological stasis helper for sheep wool grazing simulation, chicken egg timers, and baby animal aging.
- Machine and hopper block entity acceleration with data-driven tags (`#truesleep:accelerated_machines`).
- In-game Brigadier command suite (`/truesleep help, status, get, set, reset, reload`) with 2-way configuration sync.
- Dynamic GameRules registered via `DasikLibrary` 1.1.0+1.21.11 API.
