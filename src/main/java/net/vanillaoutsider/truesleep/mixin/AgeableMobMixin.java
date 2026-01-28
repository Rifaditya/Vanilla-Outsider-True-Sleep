package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.entity.AgeableMob;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin {
    @Shadow
    protected int age;

    @Shadow
    protected int forcedAge;

    @Shadow
    public abstract boolean isAgeLocked();

    @Inject(method = "aiStep", at = @At("HEAD"), cancellable = true)
    private void injectGrowth(CallbackInfo ci) {
        if (this.isAgeLocked()) return;

        long stride = TimeWarpManager.get().getStride();
        if (stride > 1) {
            int skip = (int) (stride - 1);
            if (this.age < 0) {
                this.age = Math.min(0, this.age + skip);
            } else if (this.age > 0) {
                this.age = Math.max(0, this.age - skip);
            }
            this.forcedAge += skip; // Match generic aging increment
        }
    }
}
