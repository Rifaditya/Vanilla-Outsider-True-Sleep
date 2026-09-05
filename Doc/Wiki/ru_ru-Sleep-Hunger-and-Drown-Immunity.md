# 🥖 Sleep Hunger & Drown Immunity

🌐 **Languages**: [[🇺🇸 English|ru_ru-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🥐 Sleep Hunger Drain Mechanics

Sleeping overnight in real life burns calories; waking up in Minecraft should naturally make you ready for breakfast.

When `truesleep:sleep_hunger` is enabled (default: `ON`), sleeping players experience a subtle, balanced food exhaustion drain during time warp.

### 📐 Food Exhaustion Equation
$$\text{exhaustion} = \text{strideTicks} \times 0.0008\text{ food exhaustion points}$$

* Over a standard $10{,}000\text{-tick}$ night ($12542 \to 24000$), total exhaustion is:
  $$\text{Total Exhaustion} = 10000 \times 0.0008 = 8.0\text{ points} = 2.0\text{ hunger points (1 drumstick)}$$
* If sleeping through a full $12{,}000\text{-tick}$ cycle, you will consume approximately $3.0\text{ to }4.0\text{ hunger points}$.

### 🛡️ Starvation Safety Floor
True Sleep will **NEVER starve a sleeping player to death**.
The drain automatically halts when the player's food level reaches **6 points (3 drumsticks)**:

```java
if (player.getFoodData().getFoodLevel() <= 6) return;
```

---

## 🫧 Pulmonary Stasis (Drown Immunity)

In vanilla Minecraft, placing a bed underwater or getting submerged by flowing water while sleeping causes rapid oxygen loss, resulting in drowning damage and forced bed ejection.

When `truesleep:drown_immunity` is enabled (default: `ON`), True Sleep activates **Pulmonary Stasis**:
* Intercepts `Entity.baseTick()` for sleeping players.
* Refills air supply continuously to `getMaxAirSupply()` ($300\text{ air ticks}$).
* Allows players to enjoy scenic underwater base naps without drowning!

```java
if (serverLevel.getGameRules().get(TrueSleepRules.DROWN_IMMUNITY)) {
    this.setAirSupply(this.getMaxAirSupply());
}
```

---

## 🎛️ Controlling GameRules

| GameRule | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `truesleep:sleep_hunger` | Boolean | `true` | Simulates natural sleep food exhaustion down to 6 food points. |
| `truesleep:drown_immunity` | Boolean | `true` | Prevents drowning damage while resting underwater. |
