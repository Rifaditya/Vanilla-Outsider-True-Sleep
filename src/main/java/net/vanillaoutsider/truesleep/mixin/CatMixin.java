package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.entity.animal.feline.Cat$CatRelaxOnOwnerGoal")
public class CatMixin {

    @Redirect(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getSleepTimer()I"))
    private int bypassSleepTimerForGifts(Player player) {
        if (player.level() instanceof ServerLevel level) {
            if (TimeWarpManager.get().hasRecentWarp(level.getGameTime())) {
                // Return 100 to satisfy the ">= 100" check for cat gifts
                return 100;
            }
        }
        return player.getSleepTimer();
    }
}
