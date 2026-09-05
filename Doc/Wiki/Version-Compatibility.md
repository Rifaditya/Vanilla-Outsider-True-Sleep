# 🗺️ Version Compatibility & Multi-Era Lifecycle Matrix

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🏛️ 1 Jar 1 Version Operating Standard

True Sleep strictly enforces the **1 Jar 1 Version Policy**: every targeted Minecraft version receives its own dedicated compiled JAR built natively against that version's Loom toolchain and bytecode.

Under the **Mandatory Multi-Version Lifecycle Mandate**, no supported version is ever retired, abandoned, or deprecated without explicit notice.

---

## 📊 Comprehensive Minecraft Version Matrix

| Minecraft Anchor | Release Era | Java Toolchain | Build Tooling | Mod Version Tag | Runtime Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **MC 26.3** | Modern Lead | Java 25+ (`release = 25`) | Loom 1.15+, Gradle 9.3+ | `1.3.23+26.3` | 🟢 Active Production |
| **MC 26.2** | Modern Predecessor | Java 25+ (`release = 25`) | Loom 1.15+, Gradle 9.3+ | `1.3.22+26.2` | 🟢 Active Production |
| **MC 26.1 / 26.1.2** | Modern Anchor | Java 25+ (`release = 25`) | Loom 1.15+, Gradle 9.3+ | `1.3.22+26.1.2` | 🟢 Active Production |
| **MC 1.21.11** | Winter Drop | Java 21 (`release = 21`) | Loom 1.15-SNAPSHOT | `1.1.2+1.21.11` | 🟡 Maintained Archive |
| **MC 1.21.1** | Transitional Early | Java 21 (`release = 21`) | Loom 1.10+ | `1.1.2+1.21.1` | 🟡 Maintained Archive |
| **MC 1.20.1** | Legacy Anchor | Java 17 (`release = 17`) | Loom 1.4+, Gradle 8.x | `1.0.3+1.20.1` | 🟡 Maintained Archive |

---

## 🛡️ ModVersionGuard Safety Verification

To protect persistent world saves and player data attachments from classloader mismatches or corrupted API states, True Sleep executes zero-dependency bytecode verification during `onInitialize()`:

```java
public class TrueSleep implements ModInitializer {
    @Override
    public void onInitialize() {
        net.vanillaoutsider.truesleep.util.ModVersionGuard.checkClass(
            "True Sleep", 
            "net.minecraft.world.entity.player.Player"
        );
        LOGGER.info("Initializing True Sleep (Time Warp)...");
        TrueSleepRules.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> 
            TrueSleepCommand.register(dispatcher)
        );
    }
}
```

### Knot ClassLoader Resolution
The verification uses `Thread.currentThread().getContextClassLoader()` to resolve vanilla runtime classes through Fabric's Knot classloader. If an incompatible game engine is detected, startup halts immediately with an explanatory diagnostic message before any chunk or entity data can be altered.

---

## 📦 Dependency Matrix

| Dependency | Minimum Version | Required / Optional | Purpose |
| :--- | :--- | :--- | :--- |
| **Fabric Loader** | `>=0.16.10` | **Required** | Mod loading and mixin transformation. |
| **Java Runtime** | `>=25` (26.x) / `>=21` (1.21) / `>=17` (1.20) | **Required** | Core JVM execution environment. |
| **Fabric API** | `*` (Open Lower Bound) | **Required** | Command registration and event hooks. |
| **Dasik Library** | `>=1.8.0` | **Required** | Dynamic GameRule engine & social system throttling. |
| **Cloth Config** | `*` | Optional | In-game configuration graphical interface. |
| **ModMenu** | `*` | Optional | Mod list integration and settings button. |
