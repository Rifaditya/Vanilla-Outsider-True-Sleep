package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.clock.ServerClockManager;
import net.vanillaoutsider.truesleep.logic.QuietClockInstance;
import net.vanillaoutsider.truesleep.logic.QuietClockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ServerClockManager.class)
public class ServerClockManagerMixin implements QuietClockManager {

    @Shadow
    private Map<?, ?> clocks;

    @Override
    public void truesleep$quietAdvanceAll(long ticks) {
        if (this.clocks != null) {
            for (Object instance : this.clocks.values()) {
                if (instance instanceof QuietClockInstance quietInstance) {
                    quietInstance.truesleep$quietAdd(ticks);
                }
            }
        }
    }
}
