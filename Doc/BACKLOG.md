# 📋 True Sleep: Backend Optimization & Feature Backlog

This backlog tracks upcoming technical enhancements, performance optimizations, and feature extensions for **Vanilla Outsider: True Sleep**.

---

## 🎯 Active Backlog Checklist

### 1. ⚡ Machine Acceleration Hot-Loop Caching
- [x] **Task 1.1**: Implement a `BlockEntityType` acceleration boolean cache (`ConcurrentHashMap<BlockEntityType<?>, Boolean>`) in `LevelMixin.java` / `TimeWarpManager.java`.
- [x] **Task 1.2**: Replace string keyword regexes and block state property lookups in the sleeping hot loop with $O(1)$ cached lookups.
- [x] **Task 1.3**: Audit cache invalidation on world reload / registry dynamic re-indexing.

### 2. 🌧️ Weather & Atmosphere Fast-Forwarding (`rainTime` & `thunderTime`)
- [ ] **Task 2.1**: Implement `rainTime` and `thunderTime` `stride - 1` fast-forwarding in `TimeWarpManager.java`.
- [ ] **Task 2.2**: When weather timers hit `0`, trigger `serverLevel.setWeatherParameters(...)` to clear rain and thunder by morning.
- [ ] **Task 2.3**: Verify weather transitions when sleeping through stormy nights.

### 3. 🎯 Smooth LERP Deceleration Tapering on Wake-Up
- [ ] **Task 3.1**: Implement a 15-tick LERP deceleration step counter in `TimeWarpManager.java`.
- [ ] **Task 3.2**: Smoothly decay `randomTickSpeed` and `stride` during the wake-up transition window (`stride = Math.max(1, LERP(currentStride, 1, progress))`).
- [ ] **Task 3.3**: Verify smooth visual sky rendering transitions at dawn without micro-stutters.

### 4. 🛡️ Sculk Sensor & Vibration Stasis Guard
- [ ] **Task 4.1**: Create `SculkSensorMixin.java` / `VibrationSystemMixin.java` to suppress vibration listeners while `TimeWarpManager.get().isWarping()` is `true`.
- [ ] **Task 4.2**: Test sculk sensors and Wardens near accelerated machines/crops to verify zero unwanted triggers during sleep.

---

## 🔄 Multi-Version Porting Queue
- [ ] **Phase 5.1**: Implement and verify in `True Sleep 26.2`.
- [ ] **Phase 5.2**: Port and verify in `True Sleep 26.1`.
