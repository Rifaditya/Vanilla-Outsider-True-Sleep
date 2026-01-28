<div align="center">

![True Sleep Banner](https://files.catbox.moe/9hc07g.png)

</div>
<p align="center">
    <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java">
    <img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License">
</p>

# 🌙 True Sleep (Now for 26.1 Snapshots!)

**No Backports:** I will **NOT** backport this mod to older versions (1.21, 1.20, etc.). Please do not ask.

In vanilla Minecraft, sleeping is a "cheat code" that deletes time. You right-click a bed, the screen fades to black, and the game instantly skips forward 12,000 ticks.

**Vanilla Outsider: True Sleep** changes this foundation. When you sleep, the world **accelerates**. Instead of skipping the night, the game tick rate boosts to **Quantum Speeds** (variable TPS). You watch the moon zoom across the sky, stars streak by, and the sun rise rapidly.

---

## ✨ Features

### 🕰️ Quantum Warp (Simulation)

The world doesn't pause. Furnaces continue to smelt, crops continue to grow, and copper continues to oxidize while you sleep. Everything simulates at hyper-speed.

> [!NOTE]
> **Quantum Stride Technology**: We use a variable tick stride to ensure high performance.
> Default Engine Speed: **50 TPS** (Double standard speed) ensures buttery smooth sky movement without server lag.
> Virtual Speed: **1000 TPS** (50x speed) is achieved by simulating multiple ticks per server tick.

### 🎞️ Visuals

Watch the passage of time from your bed. No jarring "fade to black." The transition from night to day is seamless and grounded in the world.

### 💤 Dreamweaver Engine

Fine-tune your sleep schedule with precision:

* **Sleep Threshold**: Configure exactly when you can get into bed (dusk, midnight, etc.).
* **Wake Time**: Decide when the warp ends (dawn, noon, etc.).
* **Hybrid Config**: Use `/gamerule` for per-world settings, or `config/truesleep.json` for global defaults.

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
* **Drown Immunity**: Entities in water are granted biological stasis (water breathing) during the warp to prevent drowning due to physics acceleration. asdasd
* **Golden Dandelion**: Compatible with age-locked mobs (from other mods); they won't age to death instantly.

---

## ⚙️ Config

The mod works out of the box with zero setup.

* **Global Template**: `config/truesleep.json` (Sets defaults for new worlds)
* **In-Game**: Use `/gamerule truesleep_` to configure per-world settings.
  * `truesleep_engine_tps`: Server stability (Default: 50)
  * `truesleep_virtual_tps`: Game speed (Default: 1000)
  * `truesleep_sleep_threshold`: when to sleep
  * `truesleep_wake_time`: when to wake

---

## 📦 Install

1. Download **[Fabric Loader](https://fabricmc.net/)** for Minecraft **26.1+** (Snapshot).
2. Install **[Fabric API](https://modrinth.com/mod/fabric-api)**.
3. Download `Vanilla-Outsider-True-Sleep.jar` and place it in your `mods` folder.

---

## 🧩 Compatibility

| Feature | Fabric (26.1+) |
| :--- | :---: |
| Singleplayer | ✅ |
| Multiplayer (LAN/Server) | ✅ |
| **VO: Better Dogs** | ✅ (Wolves cool down faster!) |
| Empty Dimensions | ✅ |

---

## ☕ Support

If you enjoy **True Sleep** and the **Vanilla Outsider** philosophy, consider fueling the next update with a coffee!

[![Ko-fi](https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/dasikigaijin/tip)
[![SocioBuzz](https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge)](https://sociabuzz.com/dasikigaijin/tribe)

> [!NOTE]
> **Indonesian Users:** SocioBuzz supports local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!

---

## 📜 Credits

| Role | Author |
| :--- | :--- |
| **Creator** | DasikIgaijin |
| **Collection** | Vanilla Outsider |
| **License** | GNU GPLv3 |

---

> [!IMPORTANT]
> This mod is part of the **Vanilla Outsider** collection. You are free to use it in modpacks, videos, and servers.
>
> > [!IMPORTANT]
> > **Modpack Permissions:** You are free to include this mod in modpacks, **provided the modpack is hosted on the same platform** (e.g. Modrinth).
> >
> > **Cross-platform distribution is not permitted.** If you download this mod from Modrinth, your modpack must also be published on Modrinth.

---

<div align="center">

**Made with ❤️ for the Minecraft community**

*Part of the Vanilla Outsider Collection*

</div>
