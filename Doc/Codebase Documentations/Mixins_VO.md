# Mixin Documentation: True Sleep

## `ServerLevelMixin`

**Target Class**: `net.minecraft.server.level.ServerLevel`
**Purpose**: Intercept vanilla sleep logic and inject custom "Warp" logic.

### 1. Disabling Time Skip

The most critical part of the mod is stopping the vanilla game from "skipping" the night instantly.

```java
@Redirect(
    method = "tick",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/SleepStatus;areEnoughSleeping(I)Z")
)
private boolean disableVanillaSleepSkip(SleepStatus instance, int percentage) {
    // We force this to return FALSE.
    // This tricks the vanilla 'tick' loop into thinking "not enough people are sleeping".
    // Result: The game continues to tick normally, it does NOT skip time.
    return false;
}
```

### 2. Monitoring Sleep Status

Since we lied to the vanilla game (telling it nobody is sleeping), we must check the *real* status ourselves to trigger our custom logic.

```java
@Inject(method = "tick", at = @At("TAIL"))
private void manageTimeWarp(BooleanSupplier haveTime, CallbackInfo ci) {
    // 1. Get the configured percentage rule directly
    int rule = this.getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
    
    // 2. Check the real sleep status using the Shadowed field
    boolean actuallyEnough = this.sleepStatus.areEnoughSleeping(rule);
    
    // 3. Pass this truth to our Manager
    TimeWarpManager.get().tick(
        (ServerLevel)(Object)this, 
        actuallyEnough, 
        this::wakeUpAllPlayers // Callback to wake players when done
    );
}
```

## `CatMixin`

**Target Class**: `net.minecraft.world.entity.animal.feline.Cat.CatRelaxOnOwnerGoal`
**Purpose**: Restore vanilla Cat Gift functionality during Time Warp.

### The Problem

Vanilla cats only give a morning gift if the player slept for at least **100 ticks (5 seconds)**.
However, True Sleep's "Time Warp" accelerates the night so fast that the night might pass in only **1.2 seconds** (even though 10 hours of game time passed). The cat thinks you didn't sleep long enough.

### The Fix

We hook into the check and ask the `TimeWarpManager` if a Time Warp happened recently.

```java
@Redirect(
    method = "stop", 
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getSleepTimer()I")
)
private int bypassSleepTimerForGifts(Player player) {
    // If we just finished a supersonic Time Warp, LIE to the cat.
    if (TimeWarpManager.get().hasRecentWarp(level.getGameTime())) {
        return 100; // "Yes, cat, I definitely slept for 5 seconds."
    }
    return player.getSleepTimer(); // Regular behavior
}
```

### 26.1 API Fixes

In Minecraft 26.1, `GameRules.getInt` was replaced by a generic `get`.

- **Old**: `getGameRules().getInt(KEY)`
- **26.1**: `getGameRules().get(KEY)` (Auto-unboxing handles the Integer return).

## Client-Side Mixins

### `GuiMixin`

**Target Class**: `net.minecraft.client.gui.Gui`
**Purpose**: Remove the visual obstruction when sleeping.

```java
@Inject(method = "renderSleepOverlay", at = @At("HEAD"), cancellable = true)
private void removeSleepDarkening(CallbackInfo ci) {
    // Unconditionally cancel the overlay.
    // We want players to see the accelerated time passing.
    ci.cancel();
}
```
