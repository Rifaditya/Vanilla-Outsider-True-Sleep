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
package net.vanillaoutsider.truesleep.config;

import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.slf4j.LoggerFactory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;


public class TrueSleepRules {
        public static final GameRuleCategory TRUE_SLEEP_CATEGORY = GameRuleCategory
                        .register(Identifier.fromNamespaceAndPath("truesleep", "config"));
        public static final GameRuleCategory TRUE_SLEEP_MOB_CATEGORY;

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

        // Static initializer
        static {
                // Must be registered here to be ready before runLoyaltyBridge and GameRules
                TRUE_SLEEP_MOB_CATEGORY = DynamicGameRuleManager
                                .registerCategory(Identifier.fromNamespaceAndPath("truesleep", "mob_settings"));

                runLoyaltyBridge();
        }

        public static final GameRule<Integer> ENGINE_TPS = DynamicGameRuleManager
                        .integerRule("truesleep:engine_tps", TRUE_SLEEP_CATEGORY, defaultEngineTps)
                        .range(20, 200)
                        .name("Performance Limit")
                        .description("How hard the server works during sleep. Keep this LOW (40-60) for best performance. Higher values use more CPU but don't make night faster — use Time Speed for that. Default: 50.")
                        .register();
        public static final GameRule<Integer> VIRTUAL_TPS_TARGET = DynamicGameRuleManager
                        .integerRule("truesleep:virtual_tps", TRUE_SLEEP_CATEGORY, defaultVirtualTps)
                        .range(20, 100000)
                        .name("Time Speed")
                        .description("How fast the night flies by. Higher = faster. 1000 = 50x speed, 5000 = 250x speed. This is the main setting to change. Default: 1000.")
                        .register();
        // Dreamweaver Rules
        public static final GameRule<Integer> SLEEP_THRESHOLD = DynamicGameRuleManager
                        .integerRule("truesleep:sleep_threshold", TRUE_SLEEP_CATEGORY, defaultSleepThreshold)
                        .range(0, 23999)
                        .name("Sleep Threshold")
                        .description("When players can start sleeping (in ticks). 0 = midnight, 6000 = noon, 12542 = sunset. Default: 12542.")
                        .register();
        public static final GameRule<Integer> WAKE_TIME = DynamicGameRuleManager
                        .integerRule("truesleep:wake_time", TRUE_SLEEP_CATEGORY, defaultWakeTime)
                        .range(0, 23999)
                        .name("Wake Time")
                        .description("What time of day players wake up (in ticks). 0 = sunrise, 6000 = noon, 18000 = midnight. Default: 0 (sunrise).")
                        .register();
        public static final GameRule<Boolean> DROWN_IMMUNITY = DynamicGameRuleManager
                        .booleanRule("truesleep:drown_immunity", TRUE_SLEEP_CATEGORY, defaultDrownImmunity)
                        .name("Drown Immunity")
                        .description("Prevents players from drowning while sleeping. Turn off if you want risky underwater naps. Default: ON.")
                        .register();
        public static final GameRule<Boolean> MOBS_FROZEN = DynamicGameRuleManager
                        .booleanRule("truesleep:freeze_mobs", TRUE_SLEEP_CATEGORY, defaultFreezeMobs)
                        .name("Freeze Mobs")
                        .description("Pauses all mobs while sleeping so they don't wander or despawn. Turn off for chaos. Default: ON.")
                        .register();
        public static final GameRule<Boolean> WORKER_MOBS_FROZEN = DynamicGameRuleManager
                        .booleanRule("truesleep:freeze_workers", TRUE_SLEEP_CATEGORY, defaultWorkerMobsFrozen)
                        .name("Freeze Villagers")
                        .description("Also pauses villagers and iron golems while sleeping. Turn ON if villagers are causing lag during sleep. Default: OFF.")
                        .register();
        public static final GameRule<Boolean> ACCELERATE_MACHINES = DynamicGameRuleManager
                        .booleanRule("truesleep:accelerate_machines", TRUE_SLEEP_CATEGORY, defaultAccelerateMachines)
                        .name("Accelerate Machines")
                        .description("Speeds up furnaces, brewing stands, and modded machines during sleep. Default: ON.")
                        .register();
        public static final GameRule<Boolean> ACCELERATE_HOPPERS = DynamicGameRuleManager
                        .booleanRule("truesleep:accelerate_hoppers", TRUE_SLEEP_CATEGORY, defaultAccelerateHoppers)
                        .name("Accelerate Hoppers")
                        .description("Speeds up hoppers connected directly to smelting or brewing machines to feed them at matching speeds. Default: ON.")
                        .register();

        private static void runLoyaltyBridge() {
                // 1. Load or Initialize Global Template

                TrueSleepConfig.load(); // Reads file if exists, sets defaults if not.
                TrueSleepConfig config = TrueSleepConfig.get();

                // 2. Migration Logic — stability clamp removed. User owns their configuration.

                // 3. Set Defaults from Config
                defaultEngineTps = (int) config.engineTps;
                defaultVirtualTps = (int) config.virtualTps;
                defaultSleepThreshold = config.sleepThreshold;
                defaultWakeTime = config.wakeTime;
                defaultDrownImmunity = config.drownImmunity;
                defaultFreezeMobs = config.freezeMobs;
                defaultWorkerMobsFrozen = config.freezeWorkers;
                defaultAccelerateMachines = config.accelerateMachines;
                defaultAccelerateHoppers = config.accelerateHoppers;
                // But for now, sticking to the plan's direct register or variable approach.
                // Let's keep it consistent.

                // The GameRule registration uses these static values.
                // Note: For the new rules (Threshold/Wake), we can't easily change the static
                // registration defaults
                // dynamically unless we read config BEFORE this class is loaded?
                // Actually, this static block runs when class is loaded.
                // So the static fields below (SLEEP_THRESHOLD) will use the literals we pass
                // unless we do:
                // public static final GameRule<Integer> SLEEP_THRESHOLD = ... register(...,
                // config.sleepThreshold, ...);
                // BUT strict Java order implies we must have variables ready.
                // Solution: Read config FIRST in static block, populate static vars, THEN
                // register.
                // Wait, 'runLoyaltyBridge' is called in static block. It populates
                // 'defaultEngineTps' etc.
                // So we just need to add static vars for the new rules.

                // 4. Persistence (Additive Update)
                // TrueSleepConfig.load() handles loading. If new fields were missing in JSON,
                // GSON leaves them default (or null).
                // To be additive, we just save it back. GSON will write the new fields.
                TrueSleepConfig.save();

                LoggerFactory.getLogger("TrueSleep").info("Dreamweaver Protocol: Global Template Loaded.");
        }

        public static void init() {
                // Dynamically register an unfreeze override rule for EVERY entity type
                BuiltInRegistries.ENTITY_TYPE.stream().forEach(entityType -> {
                        Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
                        if (id != null) {
                                String ruleName = "truesleep:unfreeze_" + id.getNamespace() + "_" + id.getPath();
                                String entityName = net.minecraft.util.Util.makeDescriptionId("entity", id);
                                DynamicGameRuleManager.booleanRule(ruleName, TRUE_SLEEP_MOB_CATEGORY, false)
                                    .name("Unfreeze " + id.getPath().replace('_', ' '))
                                    .description("Allows this entity type to bypass True Sleep freezing.")
                                    .register();
                        }
                });
        }
}
