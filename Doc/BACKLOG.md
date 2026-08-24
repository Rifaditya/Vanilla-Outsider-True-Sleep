# 📋 True Sleep: Feature & Optimization Backlog

This backlog tracks technical enhancements, performance optimizations, and feature extensions for **Vanilla Outsider: True Sleep**.

---

## 🎯 Active Backlog Checklist

*(No active tasks pending. Ready for future feature proposals and ideas!)*

---

## 🏆 Completed Enhancements (v1.3.14 - v1.3.18)

- [x] **Task 1: Machine Acceleration Hot-Loop Caching** (v1.3.14)
  - Implemented `ConcurrentHashMap<BlockEntityType<?>, Boolean>` caching in `LevelMixin.java` to eliminate string regex allocations and registry lookups in the sleeping hot loop.
- [x] **Task 2: Weather & Atmosphere Fast-Forwarding** (v1.3.15)
  - Fast-forwarded `rainTime`, `thunderTime`, and `clearWeatherTime` via `ServerLevel`'s `WeatherData` in `TimeWarpManager.java`.
- [x] **Task 3: Smooth LERP Deceleration Tapering on Wake-Up** (v1.3.17)
  - Implemented smooth LERP deceleration near morning arrival (`dist [200 -> 20]`) and a 15-tick smooth LERP wind-down when sleep is interrupted mid-warp.
- [x] **Task 4: Sculk Sensor & Vibration Stasis Guard** (v1.3.18)
  - Implemented `VibrationSystemListenerMixin.java` to suppress game event vibrations in `VibrationSystem$Listener` while time warp is active.
