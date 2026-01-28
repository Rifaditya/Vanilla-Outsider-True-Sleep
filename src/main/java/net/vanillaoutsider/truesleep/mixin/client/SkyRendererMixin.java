package net.vanillaoutsider.truesleep.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {
    @Unique
    private static float truesleep$lastVisualAngle = -1f;

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void smoothSkyDuringWarp(ClientLevel level, float partialTicks, Camera camera, SkyRenderState state, CallbackInfo ci) {
        if (TimeWarpManager.get().isWarping()) {
            float target = state.sunAngle;
            
            if (truesleep$lastVisualAngle < 0) {
                truesleep$lastVisualAngle = target;
                return;
            }

            // Celestial Smoothing: Lerp towards the target angle to bridge server tick jumps.
            // At 1000 TPS, the "target" jumps significantly between client frames.
            float diff = target - truesleep$lastVisualAngle;
            
            // Normalize angle difference to [-PI, PI]
            while (diff < -(float)Math.PI) diff += (float)Math.PI * 2;
            while (diff > (float)Math.PI) diff -= (float)Math.PI * 2;
            
            // Use a stable lerp factor to maintain visual continuity.
            // 0.35f provides a good balance between responsiveness and smoothness.
            state.sunAngle = truesleep$lastVisualAngle + diff * 0.35f;
            truesleep$lastVisualAngle = state.sunAngle;
            
            // Reposition other celestial bodies relative to the smoothed sun
            state.moonAngle = state.sunAngle + (float)Math.PI;
            state.starAngle = state.sunAngle;
        } else {
            // Reset state when not warping to ensure vanilla accuracy upon waking
            truesleep$lastVisualAngle = -1f;
        }
    }
}
