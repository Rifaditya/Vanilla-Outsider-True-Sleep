<div align="center">

# 🌙 True Sleep

### "Don't skip the night. Live through it."

![License](https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue?style=for-the-badge&logo=fabric)
![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java)

</div>

---

## 📖 About

**True Sleep** is a Minecraft mod that changes how sleeping works. Instead of instantly skipping the night (fade to black), it **accelerates** the passage of time (Time Warp).

* **Logic**: Uses a custom `TimeWarpManager` to boost tick rate to ~500 TPS.
* **Immersion**: Watch the moon/sun move across the sky.
* **Simulation**: Furnaces, crops, and entities continue to tick rapidly.
* **Vanilla Parity**: Includes `CatMixin` to ensure cats still give morning gifts despite the short night duration.
* **Safety**: Explicitly handles multi-dimension logic (Nether/End) to prevent aborts.

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

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/D1D81SP3P7)

## 📜 License

This project is licensed under the **GNU General Public License v3.0**.
See [LICENSE](LICENSE) for details.

---
<div align="center">
    <i>Part of the Vanilla Outsider Collection</i>
</div>
