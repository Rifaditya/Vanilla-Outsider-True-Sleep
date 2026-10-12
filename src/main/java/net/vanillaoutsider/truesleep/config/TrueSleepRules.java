// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.config;

// Verified against: GameRules.java (1.21.1)

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import org.slf4j.LoggerFactory;

public class TrueSleepRules {
    public static final CustomGameRuleCategory TRUE_SLEEP_CATEGORY = DynamicGameRuleManager
            .registerCategory(ResourceLocation.fromNamespaceAndPath("truesleep", "config"), Component.literal("True Sleep"));
    public static final CustomGameRuleCategory TRUE_SLEEP_MOB_CATEGORY;

    // Default values
    private static int defaultEngineTps = 50;
    private static int defaultVirtualTps = 1000;
    private static int defaultSleepThreshold = 12542;
    private static int defaultWakeTime = 0;
    private static boolean defaultDrownImmunity = true;
    private static boolean defaultFreezeMobs = true;
    private static boolean defaultWorkerMobsFrozen = false;
    private static boolean defaultAccelerateMachines = true;
    private static boolean defaultAccelerateHoppers = true;
    private static boolean defaultBiologicalAging = true;
    private static boolean defaultSleepHunger = true;

    // Static initializer
    static {
        TRUE_SLEEP_MOB_CATEGORY = DynamicGameRuleManager
                .registerCategory(ResourceLocation.fromNamespaceAndPath("truesleep", "mob_settings"), Component.literal("True Sleep Mob Settings"));

        runLoyaltyBridge();
    }

    public static final GameRules.Key<GameRules.IntegerValue> ENGINE_TPS = DynamicGameRuleManager
            .integerRule("truesleep:engine_tps", TRUE_SLEEP_CATEGORY, defaultEngineTps)
            .range(20, 200)
            .name("Performance Limit")
            .description("How hard the server works during sleep. Keep this LOW (40-60) for best performance. Higher values use more CPU but don't make night faster — use Time Speed for that. Default: 50.")
            .register();
    public static final GameRules.Key<GameRules.IntegerValue> VIRTUAL_TPS_TARGET = DynamicGameRuleManager
            .integerRule("truesleep:virtual_tps", TRUE_SLEEP_CATEGORY, defaultVirtualTps)
            .range(20, 100000)
            .name("Time Speed")
            .description("How fast the night flies by. Higher = faster. 1000 = 50x speed, 5000 = 250x speed. This is the main setting to change. Default: 1000.")
            .register();
    // Dreamweaver Rules
    public static final GameRules.Key<GameRules.IntegerValue> SLEEP_THRESHOLD = DynamicGameRuleManager
            .integerRule("truesleep:sleep_threshold", TRUE_SLEEP_CATEGORY, defaultSleepThreshold)
            .range(0, 23999)
            .name("Sleep Threshold")
            .description("When players can start sleeping (in ticks). 0 = midnight, 6000 = noon, 12542 = sunset. Default: 12542.")
            .register();
    public static final GameRules.Key<GameRules.IntegerValue> WAKE_TIME = DynamicGameRuleManager
            .integerRule("truesleep:wake_time", TRUE_SLEEP_CATEGORY, defaultWakeTime)
            .range(0, 23999)
            .name("Wake Time")
            .description("What time of day players wake up (in ticks). 0 = sunrise, 6000 = noon, 18000 = midnight. Default: 0 (sunrise).")
            .register();
    public static final GameRules.Key<GameRules.BooleanValue> DROWN_IMMUNITY = DynamicGameRuleManager
            .booleanRule("truesleep:drown_immunity", TRUE_SLEEP_CATEGORY, defaultDrownImmunity)
            .name("Drown Immunity")
            .description("Prevents players from drowning while sleeping. Turn off if you want risky underwater naps. Default: ON.")
            .register();
    public static final GameRules.Key<GameRules.BooleanValue> MOBS_FROZEN = DynamicGameRuleManager
            .booleanRule("truesleep:freeze_mobs", TRUE_SLEEP_CATEGORY, defaultFreezeMobs)
            .name("Freeze Mobs")
            .description("Pauses all mobs while sleeping so they don't wander or despawn. Turn off for chaos. Default: ON.")
            .register();
    public static final GameRules.Key<GameRules.BooleanValue> WORKER_MOBS_FROZEN = DynamicGameRuleManager
            .booleanRule("truesleep:freeze_workers", TRUE_SLEEP_CATEGORY, defaultWorkerMobsFrozen)
            .name("Freeze Villagers")
            .description("Also pauses villagers and iron golems while sleeping. Turn ON if villagers are causing lag during sleep. Default: OFF.")
            .register();
    public static final GameRules.Key<GameRules.BooleanValue> ACCELERATE_MACHINES = DynamicGameRuleManager
            .booleanRule("truesleep:accelerate_machines", TRUE_SLEEP_CATEGORY, defaultAccelerateMachines)
            .name("Accelerate Machines")
            .description("Speeds up furnaces, brewing stands, beehives, and modded machines during sleep. Default: ON.")
            .register();
    public static final GameRules.Key<GameRules.BooleanValue> ACCELERATE_HOPPERS = DynamicGameRuleManager
            .booleanRule("truesleep:accelerate_hoppers", TRUE_SLEEP_CATEGORY, defaultAccelerateHoppers)
            .name("Accelerate Hoppers")
            .description("Speeds up hoppers connected directly to smelting or brewing machines to feed them at matching speeds. Default: ON.")
            .register();
    public static final GameRules.Key<GameRules.BooleanValue> BIOLOGICAL_AGING = DynamicGameRuleManager
            .booleanRule("truesleep:biological_aging", TRUE_SLEEP_CATEGORY, defaultBiologicalAging)
            .name("Biological Farm Aging")
            .description("Simulates sheep wool grazing, chicken egg timers, and baby animal growth during sleep stasis. Default: ON.")
            .register();
    public static final GameRules.Key<GameRules.BooleanValue> SLEEP_HUNGER = DynamicGameRuleManager
            .booleanRule("truesleep:sleep_hunger", TRUE_SLEEP_CATEGORY, defaultSleepHunger)
            .name("Sleep Hunger Drain")
            .description("Drains player hunger naturally overnight during time warp so you wake up needing breakfast. Default: ON.")
            .register();

    private static void runLoyaltyBridge() {
        TrueSleepConfig.load();
        TrueSleepConfig config = TrueSleepConfig.get();

        defaultEngineTps = (int) config.engineTps;
        defaultVirtualTps = (int) config.virtualTps;
        defaultSleepThreshold = config.sleepThreshold;
        defaultWakeTime = config.wakeTime;
        defaultDrownImmunity = config.drownImmunity;
        defaultFreezeMobs = config.freezeMobs;
        defaultWorkerMobsFrozen = config.freezeWorkers;
        defaultAccelerateMachines = config.accelerateMachines;
        defaultAccelerateHoppers = config.accelerateHoppers;
        defaultBiologicalAging = config.biologicalAging;
        defaultSleepHunger = config.sleepHunger;

        TrueSleepConfig.save();

        LoggerFactory.getLogger("TrueSleep").info("Dreamweaver Protocol: Global Template Loaded.");
    }

    public static void init() {
        BuiltInRegistries.ENTITY_TYPE.stream().forEach(entityType -> {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            if (id != null) {
                String ruleName = "truesleep:unfreeze_" + id.getNamespace() + "_" + id.getPath();
                DynamicGameRuleManager.booleanRule(ruleName, TRUE_SLEEP_MOB_CATEGORY, false)
                        .name("Unfreeze " + id.getPath().replace('_', ' '))
                        .description("Allows this entity type to bypass True Sleep freezing.")
                        .register();
            }
        });
    }
}
