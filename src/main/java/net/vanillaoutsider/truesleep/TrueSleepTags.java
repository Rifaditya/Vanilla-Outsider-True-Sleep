// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TrueSleepTags {
    public static final TagKey<EntityType<?>> WORKER_MOBS = TagKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("truesleep", "worker_mobs"));
    public static final TagKey<BlockEntityType<?>> ACCELERATED_MACHINES = TagKey.create(Registries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("truesleep", "accelerated_machines"));
}
