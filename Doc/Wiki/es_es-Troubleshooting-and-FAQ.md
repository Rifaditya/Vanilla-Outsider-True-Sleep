# ❓ Troubleshooting & Frequently Asked Questions (FAQ)

🌐 **Languages**: [[🇺🇸 English|es_es-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🔍 Common Issues & Solutions

### Q1: The night takes too long to pass. How do I make it faster?
* **Answer**: Increase the **Virtual TPS** (`Time Speed`).
  Use command: `/truesleep set virtual_tps 5000` (or adjust in Cloth Config).
  * Do **NOT** raise `engine_tps` to speed up the night; keep `engine_tps` between 40–60 so the CPU does not work harder than necessary.

---

### Q2: Why are my villagers not moving during sunset?
* **Answer**: Villagers and allays are unfrozen by default (`freeze_workers: false`). If they are paused, verify that `truesleep:freeze_workers` is set to `false`.

---

### Q3: My modded machines are not speeding up during sleep.
* **Answer**: True Sleep automatically detects standard machine patterns (`furnace`, `smelter`, `generator`, `crusher`, `alloy`). For custom block entities with non-standard names, add their ID to the datapack tag `#truesleep:accelerated_machines`. See [[API & Data Tags|es_es-API-and-Tags]].

---

### Q4: Can players sleep through thunderstorms?
* **Answer**: Yes! Vanilla bed logic allows sleeping during storms regardless of time of day. True Sleep accelerates thunderstorm time and clears the weather upon awakening.

---

### Q5: Does True Sleep work on multiplayer dedicated servers?
* **Answer**: Absolutely. In multiplayer, True Sleep activates when enough players are sleeping (governed by vanilla's `playersSleepingPercentage` GameRule). All players on the server experience the smooth fast-forward time warp together!
