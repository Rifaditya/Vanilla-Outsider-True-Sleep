# 🔥 Machine & Hopper Acceleration

🌐 **Languages**: [[🇺🇸 English|zh_cn-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## ⚙️ Overview & Mechanical Problem

When world time is accelerated by a factor of $\text{Stride} \times$ during sleep, standard block entity tickers (furnaces, brewing stands, smokers) would normally only tick once per physical engine tick.

Without compensation, a furnace would only smelt $2.5\times$ faster during sleep instead of matching the full $50\times$ or $250\times$ celestial time-lapse.

**True Sleep solves this by intercepting `Level.tickBlockEntities()` and executing multi-tick bursts on verified production machines and coupled hoppers**.

---

## 🏭 Accelerated Production Machines

True Sleep checks every ticking block entity against the `#truesleep:accelerated_machines` tag and cached string patterns:

| Block Entity | Vanilla Namespace ID | Acceleration Behavior |
| :--- | :--- | :--- |
| **Furnace** | `minecraft:furnace` | Smelts items and consumes fuel $N\times$ faster per engine tick. |
| **Blast Furnace** | `minecraft:blast_furnace` | Smelts ores and metal items at hyperspeed. |
| **Smoker** | `minecraft:smoker` | Cooks food at hyperspeed. |
| **Brewing Stand** | `minecraft:brewing_stand` | Brews potions and consumes blaze powder at hyperspeed. |
| **Campfire** | `minecraft:campfire` | Cooks up to 4 food items simultaneously at hyperspeed. |
| **Soul Campfire** | `minecraft:soul_campfire` | Cooks food items at hyperspeed with soul fire particles. |
| **Beehive / Bee Nest** | `minecraft:beehive`, `minecraft:bee_nest` | Matures honey levels at matching warp rates. |
| **Modded Tech Machines** | Auto-detected (generators, smelters, crushers, grinders) | Multi-ticks modded processing machines automatically! |

---

## 🚰 Intelligent Hopper Coupling

Accelerating furnaces without accelerating their input/output hoppers causes bottlenecks: a furnace smelting 20 items per second would starve because a vanilla hopper only pushes 1 item every 8 ticks.

True Sleep implements **Coupling Verification**:
A hopper is accelerated **only if it is directly coupled to a production machine** (either pointing into it or feeding from above):

```
       [ Input Chest ]
              │
              ▼
   [ Hopper (Accelerated) ] ◄── Verified Coupling: Points into Furnace
              │
              ▼
       [ Smelting Furnace ] (Accelerated)
              │
              ▼
   [ Hopper (Accelerated) ] ◄── Verified Coupling: Pulls from Furnace above
              │
              ▼
       [ Output Chest ]
```

### Safety Decoupling
Hoppers not connected to production machines (such as item sorters, dropper clocks, or storage conduits) tick at normal speed to prevent chunk overload or lag spikes.

---

## 🎛️ Controlling GameRules

* `truesleep:accelerate_machines` (Default: `true`): Toggles machine multi-ticking.
* `truesleep:accelerate_hoppers` (Default: `true`): Toggles coupled hopper multi-ticking.
