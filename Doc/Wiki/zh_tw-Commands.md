# 💻 Dedicated Brigadier Command Suite (`/truesleep`)

🌐 **Languages**: [[🇺🇸 English|zh_tw-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🌳 Command Tree Hierarchy

True Sleep includes a comprehensive in-game command suite registered under `/truesleep` with full tab-completion and dual-sync config persistence.

```
/truesleep
├── [no args] (Overview & current warp state)
├── help (Display available subcommands)
├── status (Live diagnostics, stride multiplier, and active rules)
├── get <setting> (Query a specific rule value)
├── set <setting> <value> (Update rule and persist to truesleep.json) [Permission Level 2]
├── reset (Reset all settings to defaults and sync config) [Permission Level 2]
└── reload (Reload truesleep.json from disk and apply to active world) [Permission Level 2]
```

---

## 📖 Subcommand Reference & Examples

### 1. `/truesleep status`
Displays live time warp metrics and all active GameRule states:
```
=== True Sleep Live Status ===
• Warp State: ACTIVE (Accelerating)
• Current Stride: 20x
• Performance Limit (Engine TPS): 50 TPS
• Time Speed (Virtual TPS): 1000 TPS
• Sleep Threshold: 12542 ticks (Night Start)
• Wake Time: 0 ticks (Sunrise)
• Freeze Mobs: ON
• Freeze Villagers: OFF
• Drown Immunity: ON
• Accelerate Machines: ON
• Accelerate Hoppers: ON
• Biological Farm Aging: ON
• Sleep Hunger Drain: ON
```

---

### 2. `/truesleep get <setting>`
Queries the current value of any setting.
* **Syntax**: `/truesleep get virtual_tps`
* **Response**: `[True Sleep] Time Speed (Virtual TPS): 1000 TPS`

---

### 3. `/truesleep set <setting> <value>`
Updates a GameRule dynamically and automatically writes the change to `config/truesleep.json`.
* **Change Time Speed to 250x ($5000\text{ TPS}$)**:
  ```
  /truesleep set virtual_tps 5000
  ```
* **Allow All-Day Sleeping ($0\text{ ticks}$ threshold)**:
  ```
  /truesleep set sleep_threshold 0
  ```
* **Wake Up at Noon ($6000\text{ ticks}$)**:
  ```
  /truesleep set wake_time 6000
  ```
* **Disable Mob Freezing**:
  ```
  /truesleep set freeze_mobs false
  ```

---

### 4. `/truesleep reset`
Restores all settings to default values and synchronizes `truesleep.json`.

---

### 5. `/truesleep reload`
Reloads `config/truesleep.json` from the filesystem and pushes values to active world GameRules.
