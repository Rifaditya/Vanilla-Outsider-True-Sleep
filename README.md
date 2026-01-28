<div align="center">

# 🌙 True Sleep

### "Don't skip the night. Live through it."

![License](https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue?style=for-the-badge&logo=fabric)
![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java)

</div>

---

## 📖 About

**True Sleep** is a Minecraft mod that changes how sleeping works. Instead of instantly skipping the night (fade to black), it **accelerates** the passage of time (Quantum Warp).

* **Quantum Stride**: Boosts game logic to **1000 Virtual TPS** while keeping the server stable at **50 TPS**.
* **Dreamweaver Engine**: Configurable sleep thresholds and wake times.
* **Immersion**: Watch the moon/sun move across the sky smoothly.
* **Simulation**: Furnaces, crops, and entities continue to tick rapidly.
* **Vanilla Parity**: Includes `CatMixin` to ensure cats still give morning gifts.
* **Safety**: Handles multi-dimension logic and prevents drowning during warp.
* **Compatibility**: Supports **Golden Dandelion** (age-locked mobs won't die).

## 🛠️ Building from Source

This project uses Gradle.

### Prerequisites

* JDK 21 (for Minecraft 1.20.5+ and 26.x Snapshots)

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
