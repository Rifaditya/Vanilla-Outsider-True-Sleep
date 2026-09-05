// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import net.dasik.social.api.config.DasikSupportHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class YaclScreenHelper {
    public static ConfigScreenFactory<?> createScreen() {
        return YaclScreenHelper::buildScreen;
    }

    private static Screen buildScreen(Screen parent) {
        TrueSleepConfig config = TrueSleepConfig.get();

        // 1. General Category
        var generalCategory = ConfigCategory.createBuilder()
            .name(Component.translatable("config.truesleep.category.general"));

        // Top-pinned Creator Support Button
        Option<?> kofiButton = (Option<?>) DasikSupportHelper.createYaclButton();
        if (kofiButton != null) {
            generalCategory.option(kofiButton);
        }

        // Engine TPS (Performance Limit)
        generalCategory.option(Option.<Float>createBuilder()
            .name(Component.translatable("config.truesleep.engineTps"))
            .description(OptionDescription.of(Component.translatable("gamerule.truesleep.engine_tps.description")))
            .binding(
                50.0f,
                () -> config.engineTps,
                val -> config.engineTps = val
            )
            .controller(opt -> FloatFieldControllerBuilder.create(opt).min(1.0f))
            .build());

        // Virtual TPS (Time Speed)
        generalCategory.option(Option.<Float>createBuilder()
            .name(Component.translatable("config.truesleep.virtualTps"))
            .description(OptionDescription.of(Component.translatable("gamerule.truesleep.virtual_tps.description")))
            .binding(
                1000.0f,
                () -> config.virtualTps,
                val -> config.virtualTps = val
            )
            .controller(opt -> FloatFieldControllerBuilder.create(opt).min(1.0f))
            .build());

        // Accelerate Machines
        generalCategory.option(Option.<Boolean>createBuilder()
            .name(Component.translatable("config.truesleep.accelerateMachines"))
            .description(OptionDescription.of(Component.translatable("gamerule.truesleep.accelerate_machines.description")))
            .binding(
                true,
                () -> config.accelerateMachines,
                val -> config.accelerateMachines = val
            )
            .controller(BooleanControllerBuilder::create)
            .build());

        // Accelerate Hoppers
        generalCategory.option(Option.<Boolean>createBuilder()
            .name(Component.translatable("config.truesleep.accelerateHoppers"))
            .description(OptionDescription.of(Component.translatable("gamerule.truesleep.accelerate_hoppers.description")))
            .binding(
                true,
                () -> config.accelerateHoppers,
                val -> config.accelerateHoppers = val
            )
            .controller(BooleanControllerBuilder::create)
            .build());

        // 2. Dreamweaver Category
        var dreamweaverCategory = ConfigCategory.createBuilder()
            .name(Component.translatable("config.truesleep.category.dreamweaver"));

        // Sleep Threshold
        dreamweaverCategory.option(Option.<Integer>createBuilder()
            .name(Component.translatable("config.truesleep.sleepThreshold"))
            .description(OptionDescription.of(Component.translatable("gamerule.truesleep.sleep_threshold.description")))
            .binding(
                12542,
                () -> config.sleepThreshold,
                val -> config.sleepThreshold = val
            )
            .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(0).max(24000))
            .build());

        // Wake Time
        dreamweaverCategory.option(Option.<Integer>createBuilder()
            .name(Component.translatable("config.truesleep.wakeTime"))
            .description(OptionDescription.of(Component.translatable("gamerule.truesleep.wake_time.description")))
            .binding(
                0,
                () -> config.wakeTime,
                val -> config.wakeTime = val
            )
            .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(0).max(24000))
            .build());

        // Drown Immunity
        dreamweaverCategory.option(Option.<Boolean>createBuilder()
            .name(Component.translatable("config.truesleep.drownImmunity"))
            .description(OptionDescription.of(Component.translatable("gamerule.truesleep.drown_immunity.description")))
            .binding(
                true,
                () -> config.drownImmunity,
                val -> config.drownImmunity = val
            )
            .controller(BooleanControllerBuilder::create)
            .build());

        // Freeze Mobs
        dreamweaverCategory.option(Option.<Boolean>createBuilder()
            .name(Component.translatable("config.truesleep.freezeMobs"))
            .description(OptionDescription.of(Component.translatable("gamerule.truesleep.freeze_mobs.description")))
            .binding(
                true,
                () -> config.freezeMobs,
                val -> config.freezeMobs = val
            )
            .controller(BooleanControllerBuilder::create)
            .build());

        // Freeze Workers
        dreamweaverCategory.option(Option.<Boolean>createBuilder()
            .name(Component.translatable("config.truesleep.freezeWorkers"))
            .description(OptionDescription.of(Component.translatable("gamerule.truesleep.freeze_workers.description")))
            .binding(
                false,
                () -> config.freezeWorkers,
                val -> config.freezeWorkers = val
            )
            .controller(BooleanControllerBuilder::create)
            .build());

        return YetAnotherConfigLib.createBuilder()
            .title(Component.translatable("config.truesleep.title"))
            .category(generalCategory.build())
            .category(dreamweaverCategory.build())
            .save(TrueSleepConfig::save)
            .build()
            .generateScreen(parent);
    }
}