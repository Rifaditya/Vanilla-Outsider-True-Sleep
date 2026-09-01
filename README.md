<div align="center">

<p align="center">
  <img src="https://files.catbox.moe/9hc07g.png" alt="True Sleep Banner" width="100%">
</p>

<p align="center">
  <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
  <a href="https://modrinth.com/mod/dasik-library"><img src="https://img.shields.io/badge/Requires-Dasik_Library-purple?style=for-the-badge&logo=curseforge" alt="Requires Dasik Library"></a>
  <img src="https://img.shields.io/badge/Language-Java_25-orange?style=for-the-badge&logo=openjdk" alt="Language: Java 25">
  <img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License: GPLv3">
  <img src="https://img.shields.io/badge/Minecraft-26.2+-brightgreen?style=for-the-badge&logo=minecraft" alt="Minecraft 26.2+">
</p>

</div>

# ⏱️ Vanilla Outsider: True Sleep

> **"Sleeping shouldn't delete time — it should accelerate the living world."**

---

### 🌟 Active Version & Dependency Policy
* **1 Jar 1 Version Policy:** I build **1 dedicated JAR for each Minecraft version** (e.g. MC 26.1, MC 26.2, MC 26.3, MC 1.21.1, MC 1.20.1). Please download the exact build that matches your Minecraft installation.
* **Modern Era (`MC 26.x+`)**: Requires **Fabric API** AND **Dasik Library** (`v1.8.15+`).
* **Legacy Anchor Era (`MC 1.20.1`, `MC 1.21.x`)**: Self-contained (requires only **Fabric API**).

---

## 🌙 Why True Sleep?

In vanilla Minecraft, sleeping acts like an unnatural "cheat code" that deletes 12,000 game ticks in a single moment. You right-click a bed, the screen snaps to black, and the clock instantly leaps to sunrise.

While convenient, **instant time skipping completely breaks world simulation**:
* ❌ Your furnaces and blast furnaces freeze mid-smelt.
* ❌ Brewing stands pause and waste valuable potion batches.
* ❌ Crops, beehives, and farm animals stop growing.
* ❌ You're completely blinded by a pitch-black screen instead of enjoying the starry night sky.

**Vanilla Outsider: True Sleep replaces the instant time skip with a continuous, hyper-smooth tick acceleration engine (Time Warp)**. Time flies by at 50x–250x speed while keeping the entire living world active, fully simulating machinery, farm biology, and celestial sky motion in real time!

Part of the **Vanilla Outsider Collection** — modernizing core survival mechanics with seamless vanilla harmony.

---

## 🚀 Key Feature Showcase

### ⚡ 1. Quantum Time Warp Engine
Instead of freezing the game or lagging the server with thousands of brute-force ticks, True Sleep separates **Physical Engine TPS** from **Virtual Time Speed**:
* **Physical Engine Speed (`engine_tps`)**: Default **50 TPS** (2.5x normal CPU load) — keeps the server responsive and lag-free.
* **Virtual Time Speed (`virtual_tps`)**: Default **1000 TPS** (50x time speed) — configurable up to 100,000 TPS!
* **Smooth 15-Tick Deceleration Wind-Down**: If a player is disturbed or leaves bed early, time gently tapers back to 20 TPS rather than snapping jarringly.

### 🔥 2. Production Machine & Hopper Acceleration
Never wake up to raw iron ore sitting in an idle furnace:
* **Accelerated Machinery**: Furnaces, blast furnaces, smokers, brewing stands, beehives, and campfires process items at matching warp speeds.
* **Intelligent Hopper Coupling**: Hoppers connected directly to smelting or brewing machines automatically accelerate to prevent feed bottlenecks, while independent redstone item sorters remain protected.
* **Modded Tech Support**: Built-in regex auto-detection automatically accelerates modded generators, smelters, crushers, and grinders.

### 🐑 3. Mob Stasis & Biological Farm Aging
* **Mob Stasis**: Pauses hostile mob pathfinding and aggressive attacks during sleep so you aren't ambushed by creepers at hyper-speed.
* **Worker Mob Exemption**: Villagers and Allays remain unfrozen by default, allowing smooth bedtime pathfinding.
* **Biological Aging**: Baby animals grow up, chickens lay eggs on schedule, and sheared sheep graze grass to regrow wool coats overnight.

### 🥖 4. Sleep Hunger Drain & Pulmonary Stasis
* **Sleep Hunger**: Sleeping overnight naturally burns calories, waking you up ready for breakfast (~2–4 hunger points per full night).
* **Starvation Safety Floor**: Hunger drain automatically halts at **6 hunger points (3 drumsticks)** — you will **never** starve to death while asleep.
* **Drown Immunity (Pulmonary Stasis)**: Refills player oxygen continuously while resting, allowing scenic underwater naps in sub-aquatic glass bases!

### 🌌 5. Cinematic Celestial Sky & Warden Muting
* **No Blackout Screen**: The vanilla black fade overlay is eliminated. You stay immersed in the living world.
* **Angular Lerp Smoothing**: A 0.35 interpolation factor eliminates sun/moon jitter, providing buttery-smooth 60–240 FPS time-lapses.
* **Warden Vibration Suppression**: Suppresses `GameEvent` vibrations during warp so you don't awaken nearby Wardens while taking a nap.

---

## 🎬 Video Demonstration Showcase

<p align="center">
  <strong>🎬 Video Showcase: True Sleep in Action</strong><br>
  <em>Click the preview thumbnail or red button below to watch the live demonstration on YouTube:</em><br><br>
  <a href="https://youtu.be/FcNaMSN2WG8" target="_blank" rel="noopener">
    <img src="https://img.youtube.com/vi/FcNaMSN2WG8/maxresdefault.jpg" alt="▶️ Watch True Sleep Video Showcase" width="85%">
  </a>
  <br><br>
  <a href="https://youtu.be/FcNaMSN2WG8" target="_blank" rel="noopener">
    <img src="https://img.shields.io/badge/▶️_Watch_Video-Play_on_YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white" alt="▶️ Play Video on YouTube">
  </a>
</p>

---

## 🧩 Compatibility & HUD Integrations

* **Bed Chat Hider Synergy**: Pairs perfectly with **[Bed Chat Hider](https://modrinth.com/mod/vanilla-outsider-bed-chat-hider)** to hide chat text and typing boxes during sleep for an unobstructed panoramic view.
* **Cloth Config & ModMenu**: Provides full in-game graphical settings screen when installed, while remaining 100% crash-free on dedicated servers without GUI dependencies.
* **Multiplayer Dedicated Servers**: Activates automatically when the required threshold of players sleep (`playersSleepingPercentage`), accelerating the entire server in perfect sync.

---

## 📊 Mechanics & Kinematics Matrix

| Operational Metric | Formula / Value | Practical Survival Effect |
| :--- | :--- | :--- |
| **Stride Multiplier** | $\text{Stride} = \text{round}(\text{Virtual TPS} / \text{Engine TPS})$ | Computes how many world ticks advance per physical CPU tick ($1000/50 = 20\times$). |
| **Sleep Hunger Drain** | $\text{Exhaustion} = \text{ticks} \times 0.0008$ | Drains ~1 drumstick over a 10,000-tick night (Safety floor: 6 hunger). |
| **Sheep Wool Regrowth** | $\text{GrazeChance} = \min(1.0, \text{ticks} / 1000.0)$ | Sheared sheep grazing on grass regrow wool coats during overnight stasis. |
| **Celestial Sky Lerp** | $\text{Angle} = \text{lastAngle} + \Delta\theta \times 0.35$ | Renders ultra-smooth sun/moon rotation without discrete server packet stutter. |
| **Deceleration Wind-Down** | $15\text{ ticks linear taper}$ | Smoothly decelerates back to 20 TPS when sleep is interrupted. |

---

## 💻 In-Game Brigadier Commands (`/truesleep`)

True Sleep includes a comprehensive command suite with tab-completion and 2-way config persistence:

```
/truesleep
├── [no args] .................... Display overview and active warp state
├── help ......................... View list of all subcommands
├── status ....................... Live diagnostics, active stride, and GameRule states
├── get <setting> ................ Query current value of a GameRule
├── set <setting> <value> ........ Update GameRule and persist to config (Permission Level 2)
├── reset ........................ Reset all settings to default (Permission Level 2)
└── reload ....................... Reload config/truesleep.json from disk (Permission Level 2)
```

### Command Examples:
* Boost time speed to 250x: `/truesleep set virtual_tps 5000`
* Allow all-day sleeping: `/truesleep set sleep_threshold 0`
* Set wake-up time to noon: `/truesleep set wake_time 6000`

---

## 🎛️ Native GameRules & Configuration

> [!IMPORTANT]
> **💡 Config vs. In-Game GameRules:** The global configuration file (`config/truesleep.json`) defines default values for newly created worlds. In existing worlds, change settings in-game via the **Edit Game Rules** UI screen, the Cloth Config menu, or the `/truesleep set` command.

| GameRule | Type | Default | Bounds | Description |
| :--- | :--- | :--- | :--- | :--- |
| `truesleep:engine_tps` | Integer | `50` | `20` .. `200` | Physical server tick rate during warp. Keep at 40–60 for peak performance. |
| `truesleep:virtual_tps` | Integer | `1000` | `20` .. `100000` | Virtual time speed ($1000 = 50\times$, $5000 = 250\times$). |
| `truesleep:sleep_threshold` | Integer | `12542` | `0` .. `23999` | Time in ticks when bed sleep becomes allowed ($12542 = \text{sunset}$). |
| `truesleep:wake_time` | Integer | `0` | `0` .. `23999` | Time in ticks when players awaken ($0 = \text{sunrise}$). |
| `truesleep:freeze_mobs` | Boolean | `true` | `true` / `false` | Pauses hostile mob pathfinding during sleep. |
| `truesleep:freeze_workers` | Boolean | `false` | `true` / `false` | Pauses villagers and allays during sleep. |
| `truesleep:accelerate_machines` | Boolean | `true` | `true` / `false` | Multi-ticks furnaces, blast furnaces, smokers, brewing stands. |
| `truesleep:accelerate_hoppers` | Boolean | `true` | `true` / `false` | Multi-ticks hoppers coupled directly to production machines. |
| `truesleep:biological_aging` | Boolean | `true` | `true` / `false` | Simulates baby animal growth, chicken egg timers, sheep grazing. |
| `truesleep:sleep_hunger` | Boolean | `true` | `true` / `false` | Drains player hunger overnight down to 6 food points. |
| `truesleep:drown_immunity` | Boolean | `true` | `true` / `false` | Prevents underwater drowning while resting in a bed. |
| `truesleep:unfreeze_<id>` | Boolean | `false` | `true` / `false` | Dynamic per-entity unfreeze override for any registered mob. |

---

## 📖 In-Depth How-To & Gameplay Playbook

### 1. The Bedtime Setup
1. Place your bed in a secure bedroom, mountain watchtower, or underwater glass dome.
2. When the clock strikes sunset (`12542 ticks`), right-click your bed.
3. Instead of a black screen, your camera locks into a cozy sleeping angle while time accelerates smoothly!

### 2. High-Efficiency Smelting Setup
* Place a **Chest** above a **Hopper** pointing into a **Furnace / Blast Furnace**, with an output **Hopper** feeding into a collection chest.
* Load a stack of raw iron ore and coal before heading to bed.
* During sleep, True Sleep accelerates both the furnace and its coupled hoppers in lockstep, smelting your entire batch before sunrise!

### 3. Tuning Speed vs CPU Load
* **Want a faster night?** Increase `virtual_tps` (`/truesleep set virtual_tps 3000`).
* **Experiencing server tick lag?** Lower `engine_tps` (`/truesleep set engine_tps 40`).
* **Never raise `engine_tps` to speed up the night** — `engine_tps` controls how hard your CPU works; `virtual_tps` controls how fast time flies.

---

## ☕ Support & Community

If you enjoy True Sleep and want to support ongoing multi-version maintenance and new features, consider buying me a coffee:

<div align="center">

<p align="center">
  <a href="https://ko-fi.com/dasikigaijin" target="_blank" rel="noopener">
    <img src="https://img.shields.io/badge/Ko--fi-Support_on_Kofi-FF5E5B?style=for-the-badge&logo=kofi&logoColor=white" alt="Support on Ko-fi">
  </a>
  <a href="https://sociobuzz.com/dasik/tribe" target="_blank" rel="noopener">
    <img src="https://img.shields.io/badge/SocioBuzz-Tribe_Support-4E80EE?style=for-the-badge&logo=buffer&logoColor=white" alt="Support on SocioBuzz">
  </a>
  <a href="https://saweria.co/dasik" target="_blank" rel="noopener">
    <img src="https://img.shields.io/badge/Saweria-Local_Donation-FFA500?style=for-the-badge&logo=coffeescript&logoColor=white" alt="Support on Saweria">
  </a>
</p>

</div>

> [!NOTE]
> **🇮🇩 Indonesian Users:** SocioBuzz and Saweria support local payment methods (GoPay, OVO, DANA, QRIS) if you want to support me without PayPal!

---

## 📜 Credits & Modpack Permissions

| Attribute | Details |
| :--- | :--- |
| **Creator & Lead Architect** | **Dasik (Rifaditya)** |
| **Collection Hierarchy** | **Vanilla Outsider Collection** |
| **Software License** | **GNU General Public License v3.0 (GPLv3)** |
| **Official GitHub Wiki** | **[Browse Encyclopedic Wiki](https://github.com/Rifaditya/Vanilla-Outsider-True-Sleep/wiki)** |
| **Source Code Repository** | **[GitHub Repository](https://github.com/Rifaditya/Vanilla-Outsider-True-Sleep)** |

> [!IMPORTANT]
> **📦 Modpack Permissions & Distribution:**<br>
> You are free to include True Sleep in any modpack on any platform. However, the mod file must be downloaded from official release pages on **Modrinth** or **CurseForge**. Re-uploading mod JARs to third-party mirror sites is strictly prohibited.<br><br>
> **License & Forks:**<br>
> Since the source code is licensed under **GNU GPLv3**, you are fully permitted to inspect, fork, compile, and distribute modified versions under the terms of GPLv3.

---

<div align="center">

<p align="center">
  <b>Made with ❤️ for the Minecraft community</b><br>
  <i>Part of the Vanilla Outsider Collection — elevating vanilla survival with precision engineering.</i>
</p>

</div>
