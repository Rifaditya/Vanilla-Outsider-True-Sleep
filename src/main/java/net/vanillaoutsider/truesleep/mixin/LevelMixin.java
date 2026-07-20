/*
 * This file is part of True Sleep.
 *
 * True Sleep is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * True Sleep is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with True Sleep.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.vanillaoutsider.truesleep.mixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Verified against: Level.java (26.2+)
@Mixin(Level.class)
public abstract class LevelMixin {

    @Unique
    private static final java.util.concurrent.ConcurrentHashMap<net.minecraft.world.level.block.entity.BlockEntityType<?>, Boolean> truesleep$PRODUCTION_MACHINE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
    @Unique
    private static final java.util.concurrent.ConcurrentHashMap<String, Boolean> truesleep$TYPE_MACHINE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
    @Unique
    private static final java.util.concurrent.ConcurrentHashMap<String, Boolean> truesleep$TYPE_HOPPER_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    @Redirect(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/TickingBlockEntity;tick()V"))
    private void truesleep$accelerateBlockEntities(TickingBlockEntity ticker) {
        Level level = (Level) (Object) this;
        if (TimeWarpManager.get().isWarping()) {
            long stride = TimeWarpManager.get().getStride();
            if (stride > 1) {
                String type = ticker.getType();
                if (type != null) {
                    boolean isHopper = truesleep$TYPE_HOPPER_CACHE.computeIfAbsent(type, t -> t.contains("hopper"));
                    if (isHopper) {
                        if (TimeWarpManager.get().shouldAccelerateHoppers()) {
                            BlockPos pos = ticker.getPos();
                            BlockState state = level.getBlockState(pos);
                            if (state.is(Blocks.HOPPER) && state.getValue(HopperBlock.ENABLED)) {
                                Direction facing = state.getValue(HopperBlock.FACING);
                                // Coupling verification: only accelerate if connected to a production machine
                                boolean isCoupled = truesleep$isProductionMachine(level.getBlockEntity(pos.relative(facing))) 
                                        || truesleep$isProductionMachine(level.getBlockEntity(pos.above()));
                                if (isCoupled) {
                                    for (int i = 0; i < stride; i++) {
                                        if (ticker.isRemoved()) break;
                                        ticker.tick();
                                    }
                                    return;
                                }
                            }
                        }
                    } else if (TimeWarpManager.get().shouldAccelerateMachines()) {
                        boolean isMachine = truesleep$TYPE_MACHINE_CACHE.computeIfAbsent(type, t -> 
                                t.contains("furnace") 
                                || t.contains("smoker") 
                                || t.contains("brewing") 
                                || t.contains("campfire")
                                || t.contains("generator") 
                                || t.contains("smelter") 
                                || t.contains("alloy") 
                                || t.contains("compressor") 
                                || t.contains("crusher") 
                                || t.contains("grinder")
                        );
                        if (isMachine) {
                            for (int i = 0; i < stride; i++) {
                                if (ticker.isRemoved()) break;
                                ticker.tick();
                            }
                            return;
                        }
                    }
                }
            }
        }
        ticker.tick();
    }

    @Unique
    private boolean truesleep$isProductionMachine(BlockEntity be) {
        if (be == null) return false;
        net.minecraft.world.level.block.entity.BlockEntityType<?> type = be.getType();
        return truesleep$PRODUCTION_MACHINE_CACHE.computeIfAbsent(type, t -> {
            Identifier id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(t);
            if (id == null) return false;
            String path = id.getPath();
            return path.contains("furnace") 
                    || path.contains("smoker") 
                    || path.contains("brewing") 
                    || path.contains("campfire") 
                    || path.contains("generator") 
                    || path.contains("smelter") 
                    || path.contains("alloy") 
                    || path.contains("compressor") 
                    || path.contains("crusher") 
                    || path.contains("grinder");
        });
    }
}
