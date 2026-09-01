# 🌌 Celestial Sky & Client Cinematics

🌐 **Languages**: [[🇺🇸 English|ko_kr-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🎬 Cinematic Visual Overhaul

In vanilla Minecraft, sleeping triggers a full-screen black overlay (`extractSleepOverlay()`), blinding the player until morning arrives.

**True Sleep completely removes the black overlay**, allowing you to witness the fast-forward celestial motion of the sun, moon, clouds, and stars in real time!

---

## 📐 Celestial Angular Smoothing (Lerp)

When the server accelerates from 20 TPS to 50 TPS with 20x stride, the vanilla client's received world clock packets jump in discrete steps. Without smoothing, the sun and moon would appear to stutter across the sky.

True Sleep injects a client-side angular interpolation filter into `SkyRenderer.extractRenderState()`:

### Mathematical Lerp Equation
$$\Delta\theta = \text{normalize}(\text{targetAngle} - \text{lastVisualAngle})$$
$$\text{sunAngle} = \text{lastVisualAngle} + \Delta\theta \times 0.35$$
$$\text{moonAngle} = \text{sunAngle} + \pi$$
$$\text{starAngle} = \text{sunAngle}$$

* A smoothing factor of **$0.35$** provides an optimal balance between high responsiveness and buttery-smooth celestial motion at 60–240 FPS.

---

## 🤫 Warden Vibration Suppression

The Warden and Sculk Sensors detect player movements, item usages, and block events via `GameEvent` vibrations.

During high-speed time warps, thousands of accelerated ticks could generate massive vibration traffic, causing Warden anger spikes and server lag.

True Sleep mixins into `VibrationSystem$Listener.handleGameEvent()`:
* Automatically suppresses all game event vibrations during active time warp.
* Prevents accidental Warden awakenings while players sleep safely in deep dark outposts.

---

## 🤝 Synergy with Bed Chat Hider

For the ultimate clean time-lapse experience, pair True Sleep with companion mod **[[Bed Chat Hider|https://github.com/Rifaditya/Vanilla-Outsider-Bed-Chat-Hider]]**:
* Adds a **"Hide Chat"** toggle next to "Leave Bed".
* Clears all chat messages and typing boxes for an unobstructed panoramic sleeping view!
