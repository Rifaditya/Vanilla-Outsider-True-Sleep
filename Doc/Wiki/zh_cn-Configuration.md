# ⚙️ Configuration & Graphical Interface (GUI)

🌐 **Languages**: [[🇺🇸 English|zh_cn-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 📄 JSON Configuration File (`truesleep.json`)

True Sleep persists its global configuration in `.minecraft/config/truesleep.json`.

When creating a new world or launching a dedicated server, this file acts as the **Global Template** for initializing world GameRules.

```json
{
  "engineTps": 50.0,
  "virtualTps": 1000.0,
  "sleepThreshold": 12542,
  "wakeTime": 0,
  "drownImmunity": true,
  "freezeMobs": true,
  "freezeWorkers": false,
  "accelerateMachines": true,
  "accelerateHoppers": true,
  "biologicalAging": true,
  "sleepHunger": true,
  "wakeAtMorning": true
}
```

---

## 🎨 Optional Graphical Interface (Cloth Config & ModMenu)

True Sleep provides full optional client GUI integration:

1. **ModMenu Integration**: Adds a **Settings** button in the ModMenu mod list screen.
2. **Cloth Config Screen**:
   * **General Settings Category**: Performance Limit, Time Speed, Sleep Threshold, Wake Time.
   * **Mechanics Category**: Drown Immunity, Freeze Mobs, Freeze Villagers, Accelerate Machines, Accelerate Hoppers, Biological Aging, Sleep Hunger.

### Zero Server-Crash Architecture
GUI classes are safely gated behind client entrypoints (`truesleep.client.mixins.json` and `ModMenuIntegration`), ensuring dedicated servers run with 100% stability without requiring client GUI libraries.
