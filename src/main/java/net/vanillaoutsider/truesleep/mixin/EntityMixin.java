package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.entity.Entity;
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
    private void injectAging(CallbackInfo ci) {
        if (TimeWarpManager.get().isWarping()) {
            // Pulmonary Stasis: Freeze air supply to prevent drowning logic from triggering.
            this.setAirSupply(this.getMaxAirSupply());
            
            long stride = TimeWarpManager.get().getStride();
            if (stride > 1) {
                this.tickCount += (int) (stride - 1);
            }
        }
    }
}
