<div align="center">

![True Sleep Banner](https://files.catbox.moe/9hc07g.png)

</div>
<p align="center">
    <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java">
    <img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License">
</p>

# ?? True Sleep: The "Agency" Update (v1.3.13)

**Active Version Policy:** I build **1 JAR for 1 Version**. I only update and maintain the latest active Minecraft version (e.g. when 26.3 is released, 26.2 is retired). No backports or legacy version maintenance. Please do not ask.

In vanilla Minecraft, sleeping is a "cheat code" that deletes time. You right-click a bed, the screen fades to black, and the game instantly skips forward 12,000 ticks.

**Vanilla Outsider: True Sleep** changes this foundation. When you sleep, the world **accelerates**. Instead of skipping the night, the game tick rate boosts to **Quantum Speeds** (variable TPS). You watch the moon zoom across the sky, stars streak by, and the sun rise rapidly.

---

## ? Features

### ??? Quantum Warp (Simulation)

The world doesn't pause. Furnaces continue to smelt, crops continue to grow, and copper continues to oxidize while you sleep. Everything simulates at hyper-speed.

> [!NOTE]
> **Quantum Stride Technology**: We use a variable tick stride to ensure high performance.
> Default Engine Speed: **50 TPS** (2.5x standard speed) — controls how fast everything actually moves: mobs, redstone, furnaces, the sky, all of it.
> Virtual Speed: **1000 TPS** (50x speed) is achieved by simulating multiple ticks per server tick.
> 
> [!IMPORTANT]
> **Production & Hopper Acceleration (v1.3.13)**: Smelting/brewing and hoppers coupled directly to machines are now accelerated to match the time warp speed. Redstone-locked hoppers are ignored to protect automatic sorters.
> *Feedback Needed*: We need more feedback on this feature! If you find any issues with custom redstone builds or modded machines, please send an issue report for us to check. Thank you!

### ??? Visuals

Watch the passage of time from your bed. No jarring "fade to black." The transition from night to day is seamless and grounded in the world.

Feature Showcase: https://www.youtube.com/watch?v=FcNaMSN2WG8

### ?? Dreamweaver Engine

Fine-tune your sleep schedule with precision:

* **Sleep Threshold**: Configure exactly when you can get into bed (dusk, midnight, etc.). Corrected to 0–23999 range.
* **Wake Time**: Decide when the warp ends (dawn, noon, etc.). Now includes a full tick-to-time reference guide.
* **Hybrid Config**: Use `/gamerule` for per-world settings, or `config/truesleep.json` for global defaults.

### ?? Full Agency (Uncapped)

We have removed the training wheels. Engine TPS and Virtual TPS are now fully **uncapped**.

* **No More Clamps**: The legacy "stability clamp" that forced 50 TPS on high settings has been deleted.
* **Precision Control**: Set Engine TPS to 1000? Set Virtual TPS to 100,000? You have the agency.
* **Real-Time Night**: Tip: Set **Engine TPS = Virtual TPS** (e.g., both to 50 or 100). This sets the simulation stride to 1, meaning the night passes in **true real-time** at that exact tick rate with zero time dilation.

> [!WARNING]
> High TPS values (Engine TPS > 100) are experimental. Pushing the engine too far can cause server lag or disconnects depending on your hardware. We provide the agency; you handle the consequences.

### ?? Multiplayer

One player sleeping accelerates time for *everyone* on the server.

* **No more arguments:** "1/2 players sleeping" doesn't force a skip.
* **No disruption:** Other players just see the world speed up for a few seconds.

### ?? Cat Gifts

We have patched the vanilla Cat logic!
Normally, cats only give gifts if you sleep for 5+ seconds. True Sleep is so fast the night passes in 1 second.
**We fixed this:** Your cats now recognize the "Time Warp" and will still grant you Morning Gifts (Phantom Membranes, Rabbit Feet, etc.).

### ??? Quantum Safety

* **Empty Dimensions**: The mod intelligently ignores empty dimensions to prevent logic bugs.
* **Drown Immunity**: Entities in water are granted biological stasis (water breathing) during the warp to prevent drowning.
* **Mob Unfreeze (Dynamic Category)**: A dedicated "True Sleep Mobs" GameRule category is generated, containing toggles for *every* individual entity type in the game. Build 7 introduces performance-optimized stasis—mobs are frozen by default to save TPS. However, if you have a **redstone contraption or farm** that relies on a specific mob to work (e.g., an iron farm using zombies/villagers), you can selectively "unfreeze" them to keep your systems running at 1000 Virtual TPS.
  Feature Showcase: <iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/FcNaMSN2WG8" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
  ![Mob Unfreeze Category](https://raw.githubusercontent.com/Rifaditya/Vanilla-Outsider-True-Sleep/master/Images/2026-02-22_11.17.09.png)
* **Golden Dandelion**: Compatible with age-locked mobs.

---

## ?? Config


> [!IMPORTANT]
> **Config vs. In-Game GameRules:**
> The global configuration file only defines **default values for new worlds** at creation time.
> If you have **already created/opened a world**, changing the config file will have no effect. You must change the settings in-game using the **Edit Game Rules** UI screen or the /gamerule command.
The mod works out of the box with zero setup.

* **Global Template**: `config/truesleep.json` (Sets defaults for new worlds)
* **In-Game**: Use `/gamerule truesleep:` for core settings and the **True Sleep Mobs** category for entity control.
  * `truesleep:engine_tps` — **Performance Limit**: How hard the server works during sleep (Default: 50)
  * `truesleep:virtual_tps` — **Time Speed**: How fast the night flies by (Default: 1000)
  * `truesleep:sleep_threshold` — **Sleep Threshold**: When players can start sleeping (Default: 12542)
  * `truesleep:wake_time` — **Wake Time**: What time players wake up (Default: 0 / Sunrise)
  * `truesleep:accelerate_machines` — **Accelerate Machines**: Speeds up furnaces/brewers during sleep (Default: ON)
  * `truesleep:accelerate_hoppers` — **Accelerate Hoppers**: Speeds up hoppers coupled to machines (Default: ON)
  * `truesleep:freeze_mobs` — **Freeze Mobs**: Pauses all mobs during sleep (Default: ON)
  * `truesleep:freeze_workers` — **Freeze Villagers**: Also pauses villagers and iron golems (Default: OFF)
  * `truesleep:drown_immunity` — **Drown Immunity**: Prevents drowning during sleep (Default: ON)
* **ModMenu / Cloth Config**: All settings above are also available through the optional ModMenu config GUI (requires [Cloth Config](https://modrinth.com/mod/cloth-config) and [ModMenu](https://modrinth.com/mod/modmenu)).

![True Sleep Settings](https://raw.githubusercontent.com/Rifaditya/Vanilla-Outsider-True-Sleep/master/Images/2026-02-22_11.22.33.png)

> [!IMPORTANT]
> **Recommended Mod**: Since this mod generates 150+ GameRules, it is highly recommended to use **[Collapsible Game Rules](https://modrinth.com/mod/collapsible-gamerules)** for a cleaner UI.
>
> ![Mob Unfreeze Category](https://raw.githubusercontent.com/Rifaditya/Vanilla-Outsider-True-Sleep/master/Images/2026-02-22_11.17.09.png)

---

## ?? Suggested Mods

For the best experience, we recommend installing:
* **[Collapsible Game Rules](https://modrinth.com/mod/collapsible-gamerules)**: Prevents the GameRules menu from becoming cluttered by grouping the 150+ new mob toggles into a clean, searchable category.

---

## ?? Install

1. Install **[Fabric API](https://modrinth.com/mod/fabric-api)**.
2. Download `Vanilla-Outsider-True-Sleep.jar` and place it in your `mods` folder.

---

## ?? Compatibility

| Feature | Fabric (26.1+) |
| :--- | :---: |
| Singleplayer | ? |
| Multiplayer (LAN/Server) | ? |
| **VO: Better Dogs** | ? (Wolves cool down faster!) |
| **Create Mod** | ? (Kinetic networks stay at physical speed) |
| **Agrarian Reform** | ? (Offline growth catch-up compatible) |
| Empty Dimensions | ? |

---

## ? Support

If you enjoy **True Sleep** and the **Vanilla Outsider** philosophy, consider fueling the next update with a coffee!

[![Ko-fi](https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/dasikigaijin/tip)
[![SocioBuzz](https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge)](https://sociabuzz.com/dasikigaijin/tribe)
[![Saweria](https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge)](https://saweria.co/DasikIgaijinn)

> [!NOTE]
> **Indonesian Users:** SocioBuzz and Saweria support local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!

---

## ?? Credits

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

**Made with ?? for the Minecraft community**

*Part of the Vanilla Outsider Collection*

</div>
