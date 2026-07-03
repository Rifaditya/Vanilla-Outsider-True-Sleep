<div align="center">

![True Sleep Banner](https://files.catbox.moe/9hc07g.png)

</div>
<p align="center">
    <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java">
    <img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License">
</p>

# 🌙 True Sleep: The "Agency" Update (Build 10)

**No Backports:** I will **NOT** backport this mod to older versions (1.21, 1.20, etc.). Please do not ask.

In vanilla Minecraft, sleeping is a "cheat code" that deletes time. You right-click a bed, the screen fades to black, and the game instantly skips forward 12,000 ticks.

**Vanilla Outsider: True Sleep** changes this foundation. When you sleep, the world **accelerates**. Instead of skipping the night, the game tick rate boosts to **Quantum Speeds** (variable TPS). You watch the moon zoom across the sky, stars streak by, and the sun rise rapidly.

---

## ✨ Features

### 🕰️ Quantum Warp (Simulation)

The world doesn't pause. Furnaces continue to smelt, crops continue to grow, and copper continues to oxidize while you sleep. Everything simulates at hyper-speed.

> [!NOTE]
> **Quantum Stride Technology**: We use a variable tick stride to ensure high performance.
> Default Engine Speed: **50 TPS** (2.5x standard speed) — controls how fast everything actually moves: mobs, redstone, furnaces, the sky, all of it.
> Virtual Speed: **1000 TPS** (50x speed) is achieved by simulating multiple ticks per server tick.

### 🎞️ Visuals

Watch the passage of time from your bed. No jarring "fade to black." The transition from night to day is seamless and grounded in the world.

Feature Showcase: https://www.youtube.com/watch?v=FcNaMSN2WG8

### 💤 Dreamweaver Engine

Fine-tune your sleep schedule with precision:

* **Sleep Threshold**: Configure exactly when you can get into bed (dusk, midnight, etc.). Corrected to 0–23999 range.
* **Wake Time**: Decide when the warp ends (dawn, noon, etc.). Now includes a full tick-to-time reference guide.
* **Hybrid Config**: Use `/gamerule` for per-world settings, or `config/truesleep.json` for global defaults.

### 🚀 Full Agency (Uncapped)

In **Build 10**, we have removed the training wheels. Engine TPS and Virtual TPS are now fully **uncapped**.

* **No More Clamps**: The legacy "stability clamp" that forced 50 TPS on high settings has been deleted.
* **Precision Control**: Set Engine TPS to 1000? Set Virtual TPS to 100,000? You have the agency.
* **Real-Time Night**: Tip: Set **Engine TPS = Virtual TPS** (e.g., both to 50 or 100). This sets the simulation stride to 1, meaning the night passes in **true real-time** at that exact tick rate with zero time dilation.

> [!WARNING]
> High TPS values (Engine TPS > 100) are experimental. Pushing the engine too far can cause server lag or disconnects depending on your hardware. We provide the agency; you handle the consequences.

### ⚖️ Multiplayer

One player sleeping accelerates time for *everyone* on the server.

* **No more arguments:** "1/2 players sleeping" doesn't force a skip.
* **No disruption:** Other players just see the world speed up for a few seconds.

### 🐈 Cat Gifts

We have patched the vanilla Cat logic!
Normally, cats only give gifts if you sleep for 5+ seconds. True Sleep is so fast the night passes in 1 second.
**We fixed this:** Your cats now recognize the "Time Warp" and will still grant you Morning Gifts (Phantom Membranes, Rabbit Feet, etc.).

### 🛡️ Quantum Safety

* **Empty Dimensions**: The mod intelligently ignores empty dimensions to prevent logic bugs.
* **Drown Immunity**: Entities in water are granted biological stasis (water breathing) during the warp to prevent drowning.
* **Mob Unfreeze (Dynamic Category)**: A dedicated "True Sleep Mobs" GameRule category is generated, containing toggles for *every* individual entity type in the game. Build 7 introduces performance-optimized stasis—mobs are frozen by default to save TPS. However, if you have a **redstone contraption or farm** that relies on a specific mob to work (e.g., an iron farm using zombies/villagers), you can selectively "unfreeze" them to keep your systems running at 1000 Virtual TPS.
  Feature Showcase: <iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/FcNaMSN2WG8" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
  ![Mob Unfreeze Category](https://raw.githubusercontent.com/Rifaditya/Vanilla-Outsider-True-Sleep/master/Images/2026-02-22_11.17.09.png)
* **Golden Dandelion**: Compatible with age-locked mobs (from other mods).

---

## 🛠️ Building from Source

This project uses Gradle.

### Prerequisites

* JDK 25 (for Minecraft 26.x Snapshots)

### Build Command

```bash
./gradlew build
```

The output jar will be in `build/libs/`.

### Setup for Eclipse/IntelliJ

```bash
./gradlew genSources
./gradlew vscode  # for VSCode
./gradlew idea    # for IntelliJ
```

## 🧩 Compatibility

| Mod | Status | Notes |
| :--- | :---: | :--- |
| **VO: Better Dogs** | ✅ | Wolves cool down faster during warp. |
| **Fabric Seasons** | ❓ | Untested, but likely compatible (time advances naturally). |

## ☕ Support the Development

[![Ko-fi](https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/dasikigaijin/tip)
[![SocioBuzz](https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge)](https://sociabuzz.com/dasikigaijin/tribe)
[![Saweria](https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge)](https://saweria.co/DasikIgaijinn)

> [!NOTE]
> **Indonesian Users:** SocioBuzz and Saweria support local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!

## 📜 License

This project is licensed under the **GNU General Public License v3.0**.
See [LICENSE](LICENSE) for details.

---
<div align="center">
    <i>Part of the Vanilla Outsider Collection</i>
</div>
