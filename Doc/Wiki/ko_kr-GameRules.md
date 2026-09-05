# 🎛️ Namespaced GameRules Reference

🌐 **Languages**: [[🇺🇸 English|ko_kr-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 📋 Master GameRules Reference Table

All True Sleep rules are registered under the namespaced category `truesleep:config` and `truesleep:mob_settings` via `DynamicGameRuleManager`.

| GameRule Identifier | Type | Default Value | Value Range | In-Game Display Name & Function |
| :--- | :--- | :--- | :--- | :--- |
| `truesleep:engine_tps` | Integer | `50` | `20` .. `200` | **Performance Limit**: Physical server tickrate cap during warp. Keep low (40–60) for best performance. |
| `truesleep:virtual_tps` | Integer | `1000` | `20` .. `100000` | **Time Speed**: Target apparent time speed ($1000 = 50\times$, $5000 = 250\times$). |
| `truesleep:sleep_threshold` | Integer | `12542` | `0` .. `23999` | **Sleep Threshold**: Time of day in ticks when sleep is allowed ($0 = \text{midnight}, 12542 = \text{sunset}$). |
| `truesleep:wake_time` | Integer | `0` | `0` .. `23999` | **Wake Time**: Time of day in ticks when players awaken ($0 = \text{sunrise}, 6000 = \text{noon}$). |
| `truesleep:drown_immunity` | Boolean | `true` | `true` / `false` | **Drown Immunity**: Prevents underwater drowning during sleep. |
| `truesleep:freeze_mobs` | Boolean | `true` | `true` / `false` | **Freeze Mobs**: Pauses hostile and ambient mob movement/AI. |
| `truesleep:freeze_workers` | Boolean | `false` | `true` / `false` | **Freeze Villagers**: Pauses villagers and allays during sleep. |
| `truesleep:accelerate_machines` | Boolean | `true` | `true` / `false` | **Accelerate Machines**: Multi-ticks furnaces, smokers, brewing stands, beehives. |
| `truesleep:accelerate_hoppers` | Boolean | `true` | `true` / `false` | **Accelerate Hoppers**: Multi-ticks hoppers coupled to production machines. |
| `truesleep:biological_aging` | Boolean | `true` | `true` / `false` | **Biological Farm Aging**: Simulates baby animal growth, egg timers, sheep grazing. |
| `truesleep:sleep_hunger` | Boolean | `true` | `true` / `false` | **Sleep Hunger Drain**: Drains player hunger overnight down to 6 food points. |

---

## 🦎 Dynamic Per-Entity Unfreeze Rules

True Sleep automatically registers a dedicated unfreeze override rule for **every entity type in the game registry** under category `truesleep:mob_settings`:

```
/gamerule truesleep:unfreeze_<namespace>_<path> <true|false>
```

### Examples:
* Allow Iron Golems to patrol during sleep:
  ```
  /gamerule truesleep:unfreeze_minecraft_iron_golem true
  ```
* Allow Cats to roam during sleep:
  ```
  /gamerule truesleep:unfreeze_minecraft_cat true
  ```
* Unfreeze custom modded companion entities:
  ```
  /gamerule truesleep:unfreeze_betterdogs_dog true
  ```
