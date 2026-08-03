// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
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
                        BlockPos pos = ticker.getPos();
                        BlockEntity be = level.getBlockEntity(pos);
                        boolean isMachine = be != null && truesleep$isProductionMachine(be);
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
            if (t.builtInRegistryHolder().is(net.vanillaoutsider.truesleep.TrueSleepTags.ACCELERATED_MACHINES)) {
                return true;
            }
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
