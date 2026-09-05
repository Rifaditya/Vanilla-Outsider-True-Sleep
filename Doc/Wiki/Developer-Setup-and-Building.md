# 🛠️ Developer Setup & Gradle Building Guide

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🏗️ Environment Prerequisites

Building True Sleep from source requires the following toolchains:

1. **Java Development Kit (JDK)**: JDK 25 or higher (e.g., Eclipse Temurin 25 or GraalVM 25).
2. **Git**: Version 2.40+.
3. **IDE**: IntelliJ IDEA 2026.1+ or VS Code with Java Extension Pack.

---

## 📥 Cloning the Repository

```bash
# Clone the main True Sleep repository
git clone https://github.com/Rifaditya/Vanilla-Outsider-True-Sleep.git
cd Vanilla-Outsider-True-Sleep
```

---

## 🔨 Gradle Build Instructions

True Sleep uses Fabric Loom with Gradle 9.3+ under daemonless builds.

### 1. Compile and Package JAR
```bash
./gradlew build --no-daemon
```
The compiled mod JAR and sources JAR will be generated in `build/libs/`:
* `vanilla-outsider-true-sleep-1.3.22+26.2.jar`
* `vanilla-outsider-true-sleep-1.3.22+26.2-sources.jar`

### 2. Run Headless Unit Tests
```bash
./gradlew test --no-daemon
```

### 3. Launch Development Client
```bash
./gradlew runClient --no-daemon
```

### 4. Launch Dedicated Development Server
```bash
./gradlew runServer --no-daemon
```

---

## 📐 Project Structure Map

```
Vanilla-Outsider-True-Sleep/
├── build.gradle                 # Loom build script (Java 25 release)
├── gradle.properties            # Target Minecraft & mod versions
├── src/main/java/net/vanillaoutsider/truesleep/
│   ├── TrueSleep.java           # Entrypoint & ModVersionGuard init
│   ├── TrueSleepTags.java       # Datapack tag constants
│   ├── command/
│   │   └── TrueSleepCommand.java# Brigadier /truesleep command tree
│   ├── config/
│   │   ├── TrueSleepRules.java  # Namespaced GameRule definitions
│   │   ├── TrueSleepConfig.java # GSON JSON config persistence
│   │   ├── ClothConfigScreenHelper.java # Cloth Config GUI builder
│   │   └── ModMenuIntegration.java      # ModMenu API integration
│   ├── logic/
│   │   ├── TimeWarpManager.java        # Hyperspace acceleration engine
│   │   ├── BiologicalStasisHelper.java # Farm aging math & potion aging
│   │   ├── SleepHungerHelper.java      # Sleep hunger exhaustion math
│   │   └── QuietClockManager.java      # World clock interfaces
│   ├── mixin/
│   │   ├── BedRuleMixin.java           # Bed threshold override
│   │   ├── EntityMixin.java            # Drown immunity pulmonary stasis
│   │   ├── LevelMixin.java             # Machine & hopper tick multiplier
│   │   ├── LivingEntityMixin.java      # Active potion effect aging
│   │   ├── MobEffectInstanceMixin.java # Cascade duration decrement
│   │   ├── MobMixin.java               # Mob freeze & aging bridge
│   │   ├── ServerLevelMixin.java       # Sleep suppression & warp hook
│   │   └── VibrationSystemListenerMixin.java # Warden vibration mute
│   └── mixin/client/
│       ├── HudMixin.java               # Sleep black fade cancellation
│       └── SkyRendererMixin.java       # Angular celestial sky lerp
└── src/main/resources/
    ├── fabric.mod.json          # Mod metadata & dependencies
    ├── truesleep.mixins.json    # Common mixin configuration
    ├── truesleep.client.mixins.json # Client mixin configuration
    └── data/truesleep/tags/     # Machine & worker mob tags
```
