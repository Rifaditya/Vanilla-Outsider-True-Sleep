# ⚡ Time Warp & Tick Acceleration Engine

🌐 **Languages**: [[🇺🇸 English|pt_br-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🔬 How Time Warp Works

The core breakthrough of True Sleep is separating **Physical Server Engine Speed (`engine_tps`)** from **Apparent Virtual Time Speed (`virtual_tps`)**.

Instead of forcing your CPU to execute 1,000 real game ticks per second (which would cause severe server tick lag and disconnects), True Sleep runs the server engine at a stable, configurable rate (default: $50\text{ TPS}$) and advances game time using a mathematical **Stride Multiplier**.

---

## 📐 Mathematical Equations & Stride Ratio

### 1. Stride Ratio Formula
The server calculates how many time ticks to advance per physical engine tick:
$$\text{Physical Target} = \min(\text{engine\_tps}, \text{targetRate})$$
$$\text{Stride} = \max\left(1, \text{round}\left(\frac{\text{targetRate}}{\text{Physical Target}}\right)\right)$$

* **Example with Defaults**:
  * $\text{engine\_tps} = 50\text{ TPS}$
  * $\text{virtual\_tps} = 1000\text{ TPS}$
  * $\text{Stride} = \frac{1000}{50} = 20\times\text{ stride}$
  * **Result**: The server runs at 50 ticks/second, advancing 20 world ticks per physical tick, achieving $1000\text{ TPS}$ apparent speed with only 2.5x normal CPU load!

---

### 2. Smooth Destination Tapering
When approaching the target wake time ($	ext{dist} < 200\text{ ticks}$), the warp engine decelerates smoothly to prevent abrupt jarring transitions:

$$\text{progress} = \max\left(0.0, \min\left(1.0, \frac{\text{dist} - 20}{180}\right)\right)$$
$$\text{targetRate} = 20.0 + (\text{virtual\_tps} - 20.0) \times \text{progress}$$

---

### 3. 15-Tick Deceleration Wind-Down
If a player wakes up early (e.g. bed broken, player leaves bed, or damage taken), True Sleep initiates a smooth **15-tick linear wind-down** rather than snapping instantly to 20 TPS:

$$\text{progress} = \frac{\text{decelerateTicks}}{15.0}$$
$$\text{currentRate} = 20.0 + (\text{lastWarpRate} - 20.0) \times \text{progress}$$
$$\text{stride} = \max(1, \text{round}(1 + (\text{lastWarpStride} - 1) \times \text{progress}))$$

---

### 4. Random Tick Speed Scaling
Crop growth, leaf decay, and copper oxidation depend on the vanilla `randomTickSpeed` GameRule. True Sleep dynamically scales this rule during sleep so farms progress in exact 1-to-1 sync:

$$\text{newRandomSpeed} = \min(500, \text{originalRandomTickSpeed} \times \text{stride})$$

---

### 5. Monotonic Clock Snapping
Upon reaching destination wake time, True Sleep snaps the world clock monotonically forward:
$$\text{snappedTime} = \text{currentDay} + \text{wakeTime}$$
$$\text{if } \text{snappedTime} < \text{currentFull} \implies \text{snappedTime} += 24000L$$

This ensures the world clock never rolls backward, preserving daylight sensor states and time-dependent datapacks.
