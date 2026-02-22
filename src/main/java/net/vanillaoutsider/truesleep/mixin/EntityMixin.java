package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.entity.Entity;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public int tickCount;

    @Shadow
    public abstract void setAirSupply(int air);

    @Shadow
    public abstract int getMaxAirSupply();

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void truesleep$injectAging(CallbackInfo ci) {
        if (TimeWarpManager.get().isWarping()) {
            // Pulmonary Stasis: Freeze air supply to prevent drowning logic from
            // triggering.
            // Only active when the DROWN_IMMUNITY GameRule is enabled.
            Entity self = (Entity) (Object) this;
            if (self.level() instanceof net.minecraft.server.level.ServerLevel serverLevel
                    && serverLevel.getGameRules().get(TrueSleepRules.DROWN_IMMUNITY)) {
                this.setAirSupply(this.getMaxAirSupply());
            }

            long stride = TimeWarpManager.get().getStride();
            if (stride > 1) {
                this.tickCount += (int) (stride - 1);
            }
        }
    }
}
