# Architecture & Symbol Index: Vanilla Outsider: True Sleep

## 1. Mod Metadata & Entrypoint
- **Mod ID**: `vanilla-outsider-true-sleep`
- **Main Entrypoint**: `net.vanillaoutsider.truesleep.TrueSleep` (`net.fabricmc.api.ModInitializer`)
- **Client Entrypoint**: `None`

## 2. Bytecode Mixin Target Registry
| Target Vanilla Class | Mixin Class | Purpose |
| :--- | :--- | :--- |
| `Vanilla Class` | `net.vanillaoutsider.truesleep.mixin.BedRuleMixin` | Core mixin hook |
| `Vanilla Class` | `net.vanillaoutsider.truesleep.mixin.EntityMixin` | Core mixin hook |
| `Vanilla Class` | `net.vanillaoutsider.truesleep.mixin.LevelMixin` | Core mixin hook |
| `Vanilla Class` | `net.vanillaoutsider.truesleep.mixin.LivingEntityMixin` | Core mixin hook |
| `Vanilla Class` | `net.vanillaoutsider.truesleep.mixin.MobEffectInstanceMixin` | Core mixin hook |
| `Vanilla Class` | `net.vanillaoutsider.truesleep.mixin.MobMixin` | Core mixin hook |
| `Vanilla Class` | `net.vanillaoutsider.truesleep.mixin.ServerLevelMixin` | Core mixin hook |
| `Vanilla Class` | `net.vanillaoutsider.truesleep.mixin.VibrationSystemListenerMixin` | Core mixin hook |

## 3. Core Mechanics & Subsystems
- **Source Root**: `src/main/java/`
- **Resource Root**: `src/main/resources/`

## 4. Dynamic GameRules & Commands
- **GameRules / Commands**: Configured dynamically via namespaced keys (`vanilla-outsider-true-sleep:*`).

## 5. Configuration & Sidedness Isolation
- **Sidedness**: Server-safe logic in main, client isolated in `src/client/java` or client entrypoint.
