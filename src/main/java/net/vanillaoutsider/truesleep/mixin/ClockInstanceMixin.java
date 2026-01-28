package net.vanillaoutsider.truesleep.mixin;

import net.vanillaoutsider.truesleep.logic.QuietClockInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.clock.ServerClockManager$ClockInstance")
public class ClockInstanceMixin implements QuietClockInstance {
    @Shadow
    private long totalTicks;

    @Override
    public void truesleep$quietAdd(long ticks) {
        this.totalTicks += ticks;
    }
}
