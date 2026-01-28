package net.vanillaoutsider.truesleep.config;

import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.vanillaoutsider.truesleep.mixin.GameRulesInvoker;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class TrueSleepRules {
    public static final GameRuleCategory TRUE_SLEEP_CATEGORY = GameRuleCategory.register(Identifier.fromNamespaceAndPath("truesleep", "config"));

    // Default values
    private static int defaultEngineTps = 50;
    private static int defaultVirtualTps = 1000;
    private static int defaultSleepThreshold = 12542;
    private static int defaultWakeTime = 0;
    private static boolean defaultDrownImmunity = true;

    // Static initializer to run the Loyalty Bridge BEFORE rules are registered
    static {
        runLoyaltyBridge();
    }

    public static final GameRule<Integer> ENGINE_TPS = GameRulesInvoker.invokeRegisterInteger("truesleep_engine_tps", TRUE_SLEEP_CATEGORY, defaultEngineTps, 1, 1000);
    public static final GameRule<Integer> VIRTUAL_TPS_TARGET = GameRulesInvoker.invokeRegisterInteger("truesleep_virtual_tps", TRUE_SLEEP_CATEGORY, defaultVirtualTps, 1, 10000);
    // Dreamweaver Rules
    public static final GameRule<Integer> SLEEP_THRESHOLD = GameRulesInvoker.invokeRegisterInteger("truesleep_sleep_threshold", TRUE_SLEEP_CATEGORY, defaultSleepThreshold, 0, 24000);
    public static final GameRule<Integer> WAKE_TIME = GameRulesInvoker.invokeRegisterInteger("truesleep_wake_time", TRUE_SLEEP_CATEGORY, defaultWakeTime, 0, 24000);
    public static final GameRule<Boolean> DROWN_IMMUNITY = GameRulesInvoker.invokeRegisterBoolean("truesleep_drown_immunity", TRUE_SLEEP_CATEGORY, defaultDrownImmunity);


    private static void runLoyaltyBridge() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("truesleep.json");
        boolean needsSave = false;
        
        // 1. Load or Initialize Global Template
        TrueSleepConfig.load(); // Reads file if exists, sets defaults if not.
        TrueSleepConfig config = TrueSleepConfig.get();

        // 2. Migration Logic (Stability Clamp)
        if (config.engineTps >= 99.0f) {
           config.engineTps = 50.0f;
           needsSave = true;
           LoggerFactory.getLogger("TrueSleep").warn("Dreamweaver Protocol: Clamped Engine TPS to 50 for stability.");
        }
        
        // 3. Set Defaults from Config
        defaultEngineTps = (int) config.engineTps;
        defaultVirtualTps = (int) config.virtualTps;
        defaultSleepThreshold = config.sleepThreshold;
        defaultWakeTime = config.wakeTime;
        defaultDrownImmunity = config.drownImmunity;
        // The GameRule registration uses these static values. 
        // Note: For the new rules (Threshold/Wake), we can't easily change the static registration defaults 
        // dynamically unless we read config BEFORE this class is loaded?
        // Actually, this static block runs when class is loaded. 
        // So the static fields below (SLEEP_THRESHOLD) will use the literals we pass unless we do:
        // public static final GameRule<Integer> SLEEP_THRESHOLD = ... register(..., config.sleepThreshold, ...);
        // BUT strict Java order implies we must have variables ready.
        // Solution: Read config FIRST in static block, populate static vars, THEN register.
        // Wait, 'runLoyaltyBridge' is called in static block. It populates 'defaultEngineTps' etc.
        // So we just need to add static vars for the new rules.
        
        // 4. Persistence (Additive Update)
        // TrueSleepConfig.load() handles loading. If new fields were missing in JSON, GSON leaves them default (or null).
        // To be additive, we just save it back. GSON will write the new fields.
        TrueSleepConfig.save(); 
        
        LoggerFactory.getLogger("TrueSleep").info("Dreamweaver Protocol: Global Template Loaded.");
    }

    public static void init() {
    }
}
