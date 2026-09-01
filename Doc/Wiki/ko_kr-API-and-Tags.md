# 🏷️ API & Data-Driven Tag Extension

🌐 **Languages**: [[🇺🇸 English|ko_kr-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 📦 Data-Driven Tags

Datapack creators and addon mod developers can customize True Sleep's machine and entity behaviors without writing Java code.

---

### 1. Accelerated Machines Tag (`#truesleep:accelerated_machines`)
* **Tag File**: `data/truesleep/tags/block_entity_type/accelerated_machines.json`
* **Purpose**: Declares block entity types that receive multi-tick acceleration during sleep.

```json
{
  "replace": false,
  "values": [
    "minecraft:furnace",
    "minecraft:blast_furnace",
    "minecraft:smoker",
    "minecraft:brewing_stand",
    "minecraft:campfire",
    "minecraft:soul_campfire",
    "minecraft:beehive",
    "mymod:custom_smelter",
    "mymod:alloy_forge"
  ]
}
```

---

### 2. Worker Mobs Tag (`#truesleep:worker_mobs`)
* **Tag File**: `data/truesleep/tags/entity_type/worker_mobs.json`
* **Purpose**: Declares entity types governed by the `truesleep:freeze_workers` GameRule instead of global `freeze_mobs`.

```json
{
  "replace": false,
  "values": [
    "minecraft:allay",
    "minecraft:villager",
    "mymod:goblin_trader"
  ]
}
```

---

## 💻 Developer Java Hooks

### 1. `TimeWarpManager` Static Accessor
```java
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;

// Check if world time is currently accelerating
boolean isWarping = TimeWarpManager.get().isWarping();

// Get current time stride multiplier
long stride = TimeWarpManager.get().getStride();
```

### 2. Potion Effect Aging Interface
```java
import net.vanillaoutsider.truesleep.MobEffectInstanceExtensions;

MobEffectInstance effect = mob.getEffect(MobEffects.REGENERATION);
if (effect != null) {
    ((MobEffectInstanceExtensions) effect).truesleep$ageEffect(skipTicks);
}
```
