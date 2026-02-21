<div align="center">

# 🌙 True Sleep

### "Don't skip the night. Live through it."

![License](https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue?style=for-the-badge&logo=fabric)
![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java)

</div>

---

### 2. Standalone Download

If you prefer installing individual mods instead of the full modpack:

1.  Download `vanilla-outsider-true-sleep-26.1-snapshots-1.3.2+build.5.jar`.
2.  Install [Fabric API](https://modrinth.com/mod/fabric-api) for Snapshot 26.1.
3.  Place both into your `.minecraft/mods` folder.

> ⚠️ **IMPORTANT**: True Sleep now dynamically generates over 150+ GameRules (one for every mob in the game) to allow individual mob unfreezing. It is **HIGHLY RECOMMENDED** to install the **[Collapsible Game Rules](https://github.com/Rifaditya/MC-CollapsibleGameRuleScreen)** mod to prevent the GameRules menu from becoming an unscrollable mess.

## 📖 About

**True Sleep** is a Minecraft mod that changes how sleeping works. Instead of instantly skipping the night (fade to black), it **accelerates** the passage of time (Quantum Warp).

* **Safety**: Handles multi-dimension logic and prevents drowning during warp.
* **Compatibility**: Supports **Golden Dandelion** (age-locked mobs won't die).

## 🛠️ Building from Source

This project uses Gradle.

### Prerequisites

* JDK 25 (for Minecraft 26.x Snapshots)

### Build Command

```bash
./gradlew build
```

The output jar will be in `build/libs/`.

### Setup for Eclipse/IntelliJ

```bash
./gradlew genSources
./gradlew vscode  # for VSCode
./gradlew idea    # for IntelliJ
```

## 🧩 Compatibility

| Mod | Status | Notes |
| :--- | :---: | :--- |
| **VO: Better Dogs** | ✅ | Wolves cool down faster during warp. |
| **Fabric Seasons** | ❓ | Untested, but likely compatible (time advances naturally). |

## ☕ Support the Development

[![Ko-fi](https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/dasikigaijin/tip)
[![SocioBuzz](https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge)](https://sociabuzz.com/dasikigaijin/tribe)

> [!NOTE]
> **Indonesian Users:** SocioBuzz supports local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!

## 📜 License

This project is licensed under the **GNU General Public License v3.0**.
See [LICENSE](LICENSE) for details.

---
<div align="center">
    <i>Part of the Vanilla Outsider Collection</i>
</div>
