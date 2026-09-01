# 🏛️ Architecture & Mixin Dissection

🌐 **Languages**: [[🇺🇸 English|fr_fr-Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🔬 High-Level Architecture Overview

True Sleep operates by intercepting the vanilla sleep lifecycle at the server level, replacing the immediate time leap with a synchronized tick acceleration loop governed by `TimeWarpManager`.

```
                  [ Player Enters Bed ]
                            │
                            ▼
              [ BedRule.Rule.test() Intercept ]
              (Check truesleep:sleep_threshold)
                            │
                            ▼
           [ ServerLevel.tick() Sleep Check ]
          (SleepStatus.areEnoughDeepSleeping())
                            │
               +------------+------------+
               │                         │
     [ Not All Sleeping ]       [ All Sleeping (Enough) ]
               │                         │
               ▼                         ▼
      [ Initiate Wind-Down ]    [ Engage Time Warp Engine ]
      (15-tick Deceleration)             │
                                         ├─► Set Physical Rate (ENGINE_TPS: 50 TPS)
                                         ├─► Compute Stride: round(VIRTUAL_TPS / ENGINE_TPS)
                                         ├─► Scale RANDOM_TICK_SPEED by Stride
                                         ├─► Multi-Tick Machines & Coupled Hoppers
                                         ├─► Freeze Mob AI / Age Farm Biology
                                         ├─► Apply Sleep Hunger Exhaustion
                                         └─► Smooth Celestial Sun/Moon (Client Lerp)
                                                 │
                                                 ▼
                                     [ Destination Arrival ]
                                     (Cycle Dist < 20 Ticks)
                                                 │
                                                 ▼
                                    [ Snap Monotonic Clock ]
                                   (Set Time to WAKE_TIME: 0)
                                                 │
                                                 ▼
                                       [ Wake Up All Players ]
```

---

## 🧩 Complete Mixin Inventory & Injection Descriptors

| Mixin Class | Target Class | Injection Point | Method Hook | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `BedRuleMixin` | `net.minecraft.world.attribute.BedRule$Rule` | `@At("HEAD")` (Cancellable) | `test(Level)` | Overrides `WHEN_DARK` bed check against `truesleep:sleep_threshold`. |
| `ServerLevelMixin` | `net.minecraft.server.level.ServerLevel` | `@At("INVOKE")` (Redirect) | `areEnoughSleeping` / `areEnoughDeepSleeping` | Returns `false` to suppress vanilla instant skip. |
| `ServerLevelMixin` | `net.minecraft.server.level.ServerLevel` | `@At("TAIL")` | `tick(BooleanSupplier)` | Checks sleep conditions and drives `TimeWarpManager.tick()`. |
| `LevelMixin` | `net.minecraft.world.level.Level` | `@At("INVOKE")` (Redirect) | `TickingBlockEntity.tick()` | Multi-ticks accelerated machines and coupled hoppers by `stride`. |
| `MobMixin` | `net.minecraft.world.entity.Mob` | `@At("HEAD")` (Cancellable) | `tick()` | Cancels mob movement/AI if frozen; executes biological aging. |
| `EntityMixin` | `net.minecraft.world.entity.Entity` | `@At("HEAD")` | `baseTick()` | Refills air supply to prevent drowning when `drown_immunity` is active. |
| `LivingEntityMixin` | `net.minecraft.world.entity.LivingEntity` | `@At("HEAD")` | `baseTick()` | Ages active potion effects by `stride - 1` ticks. |
| `MobEffectInstanceMixin` | `net.minecraft.world.effect.MobEffectInstance` | Interface Injection | `truesleep$ageEffect(int)` | Recursively ages hidden potion effects without recursion bugs. |
| `VibrationSystemListenerMixin` | `net.minecraft.world.level.gameevent.vibrations.VibrationSystem$Listener` | `@At("HEAD")` (Cancellable) | `handleGameEvent(...)` | Suppresses Warden acoustic vibrations during time warp. |
| `client.HudMixin` | `net.minecraft.client.gui.Hud` | `@At("HEAD")` (Cancellable) | `extractSleepOverlay(...)` | Cancels the vanilla black fade screen overlay during sleep. |
| `client.SkyRendererMixin` | `net.minecraft.client.renderer.SkyRenderer` | `@At("TAIL")` | `extractRenderState(...)` | Lerps `sunAngle` with factor $0.35$ for smooth cinematic rendering. |

---

## ⚡ Zero-Allocation Hot-Path Caching

During high-speed time warps ($50\text{ physical TPS} \times 50\text{ stride} = 2{,}500\text{ effective iterations/s}$), memory allocations in entity and block entity loops can trigger aggressive Garbage Collection (GC) pauses.

True Sleep utilizes **static concurrent lock-free caches** across all hot paths:

1. **`truesleep$unfreezeCache`**: Caches dynamic GameRule instances per `EntityType<?>` to eliminate string allocations.
2. **`truesleep$WORKER_CACHE`**: Caches tag containment results for `#truesleep:worker_mobs`.
3. **`truesleep$PRODUCTION_MACHINE_CACHE`**: Caches block entity type matching for `#truesleep:accelerated_machines`.
4. **`TimeWarpManager` GameRule field caching**: Fetches GameRule boolean states once per server tick rather than querying rules inside entity loops.
