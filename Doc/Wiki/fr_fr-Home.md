# ⏱️ Vanilla Outsider: True Sleep Wiki

🌐 **Languages**: [[🇺🇸 English|fr_fr-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

<div align="center">

<p align="center">
  <img src="https://img.shields.io/badge/Architecture-Time_Warp_Engine-blue?style=for-the-badge" alt="Time Warp Engine">
  <img src="https://img.shields.io/badge/Language-Java_25-orange?style=for-the-badge&logo=openjdk" alt="Java 25">
  <img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License">
  <img src="https://img.shields.io/badge/Minecraft-26.2+-brightgreen?style=for-the-badge&logo=minecraft" alt="Minecraft 26.2+">
</p>

</div>

---

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🧭 Master Documentation Portal

Welcome to the official technical documentation for **Vanilla Outsider: True Sleep**, an advanced world simulation mod developed under the **Vanilla Outsider (VO)** design philosophy.

In vanilla Minecraft, sleeping skips the night instantly by advancing the world clock from dusk to dawn in 0 ticks ($100\text{ ticks}$ fade). While convenient, this instantly halts background world simulation: furnaces freeze mid-smelt, crops and baby animals pause growth, and potions fail to progress.

**True Sleep replaces the vanilla instant time skip with an ultra-smooth, continuous server tick acceleration engine (Time Warp)**. Time accelerates smoothly up to 50x–250x speed while keeping the entire living world active, complete with machine acceleration, biological farm aging, and seamless celestial sky rendering.

---

## 🌟 Core Subsystem Matrix

| Subsystem | Technical Description | Reference Page |
| :--- | :--- | :--- |
| **⚡ Time Warp & Stride Acceleration** | Dynamically scales server tick rate and time stride ($20\text{ to }100{,}000\text{ Virtual TPS}$) with smooth 15-tick deceleration wind-downs. | [[Time Warp & Tick Acceleration|fr_fr-Time-Warp-and-Tick-Acceleration]] |
| **🔥 Machine & Hopper Acceleration** | Multi-ticks furnaces, smokers, brewing stands, beehives, and coupled hoppers to match warp speed. | [[Machine & Hopper Acceleration|fr_fr-Machine-and-Hopper-Acceleration]] |
| **🐑 Mob Stasis & Biological Aging** | Freezes dangerous mob pathfinding while simulating baby animal growth, egg timers, and sheep grass grazing. | [[Mob Stasis & Biological Aging|fr_fr-Mob-Stasis-and-Biological-Aging]] |
| **🥖 Sleep Hunger & Drown Immunity** | Simulates natural sleep food exhaustion ($\text{safety floor at } 6\text{ hunger}$) and pulmonary oxygen stasis. | [[Sleep Hunger & Drown Immunity|fr_fr-Sleep-Hunger-and-Drown-Immunity]] |
| **🌌 Celestial Sky & Cinematics** | Eliminates screen blackouts and applies angular interpolation lerp ($0.35\text{ factor}$) for sun/moon time-lapses. | [[Celestial Sky & Client Cinematics|fr_fr-Celestial-Sky-and-Client-Cinematics]] |
| **🎛️ Dynamic GameRules** | Fully configurable namespaced GameRules in `truesleep:config` and per-mob unfreeze rules in `truesleep:mob_settings`. | [[GameRules Reference|fr_fr-GameRules]] |
| **💻 Dedicated Brigadier Command** | Full `/truesleep` command suite (`status`, `get`, `set`, `reset`, `reload`) with Gamemaster permissions. | [[Command Suite|fr_fr-Commands]] |
| **⚙️ Config GUI & ModMenu** | JSON configuration (`truesleep.json`), Cloth Config integration, and hot-reloading capabilities. | [[Configuration & GUI|fr_fr-Configuration]] |
| **🏷️ Data-Driven Tag Extension** | Extensible datapack tags `#truesleep:accelerated_machines` and `#truesleep:worker_mobs`. | [[API & Data Tags|fr_fr-API-and-Tags]] |

---

## 🚀 Quick Start & Survival Guide

### 1. Installation
1. Install **[Fabric Loader](https://fabricmc.net/)** (`>=0.16.10`).
2. Download `vanilla-outsider-true-sleep-1.3.22+26.2.jar` (or matching game version build).
3. Ensure required dependency **Dasik Library** (`>=1.8.0`) is installed in `.minecraft/mods`.
4. Launch Minecraft using **Java 25+** (or Java 21 for 1.21.x / Java 17 for 1.20.1).

### 2. In-Game Usage
1. Right-click any Bed when time reaches `12542 ticks` (Sunset/Night).
2. Instead of a pitch-black screen, you remain in-world as the night flies by in a smooth cinematic time-lapse!
3. Watch the moon and stars arc gracefully across the sky while your furnaces smelt ores at hyperspeed.
4. Wake up at sunrise (`0 ticks`) feeling refreshed, ready for breakfast.

---

## 📚 Complete Wiki Navigation Index

```
True Sleep Wiki Root
├── 🧭 Compatibility & Lifecycle
│   ├── Multi-Era Version Matrix --------------> [[Version Compatibility|fr_fr-Version-Compatibility]]
│   ├── Developer Setup & Gradle Builds --------> [[Developer Setup & Building|fr_fr-Developer-Setup-and-Building]]
│   └── Troubleshooting & FAQ ------------------> [[Troubleshooting & FAQ|fr_fr-Troubleshooting-and-FAQ]]
├── 🎮 Core Gameplay Mechanics
│   ├── Time Warp & Stride Acceleration -------> [[Time Warp & Tick Acceleration|fr_fr-Time-Warp-and-Tick-Acceleration]]
│   ├── Machine & Hopper Acceleration ----------> [[Machine & Hopper Acceleration|fr_fr-Machine-and-Hopper-Acceleration]]
│   ├── Mob Stasis & Biological Aging ----------> [[Mob Stasis & Biological Aging|fr_fr-Mob-Stasis-and-Biological-Aging]]
│   ├── Sleep Hunger & Drown Immunity ----------> [[Sleep Hunger & Drown Immunity|fr_fr-Sleep-Hunger-and-Drown-Immunity]]
│   └── Celestial Sky & Client Cinematics ------> [[Celestial Sky & Client Cinematics|fr_fr-Celestial-Sky-and-Client-Cinematics]]
├── 🎛️ Administration & Configuration
│   ├── Namespaced GameRules Registry ----------> [[GameRules Reference|fr_fr-GameRules]]
│   ├── Brigadier Command Suite (/truesleep) ---> [[Command Suite|fr_fr-Commands]]
│   ├── JSON Config & Cloth Config GUI ---------> [[Configuration & GUI|fr_fr-Configuration]]
│   └── Data-Driven Tags & Addon APIs ----------> [[API & Data Tags|fr_fr-API-and-Tags]]
└── 💻 Technical Architecture
    ├── Mixin Dissection & Class Map -----------> [[Architecture & Mixins|fr_fr-Architecture-and-Mixins]]
    └── Bed Chat Hider Synergy -----------------> [[Celestial Sky & Client Cinematics|fr_fr-Celestial-Sky-and-Client-Cinematics]]
```

---

## 📜 Credits & License

* **Architect & Developer**: **Dasik (Rifaditya)**
* **Collection**: **Vanilla Outsider (VO)**
* **License**: **GNU General Public License v3.0 (GPLv3)**
