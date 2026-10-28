// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.command;

// Verified against: CommandSourceStack.java (26.3+)

import com.mojang.brigadier.CommandDispatcher;
import java.util.List;
import net.dasik.social.api.command.GameRuleCommandBridge;
import net.dasik.social.api.command.UniversalCommandRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.vanillaoutsider.truesleep.TrueSleep;
import net.vanillaoutsider.truesleep.config.TrueSleepConfig;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;

public class TrueSleepCommand {
    public static final String ROOT_LITERAL = "truesleep";
    public static final String MOD_NAME = "True Sleep";

    public static final List<String> SETTING_KEYS = List.of(
            "engine_tps",
            "virtual_tps",
            "sleep_threshold",
            "wake_time",
            "freeze_mobs",
            "freeze_workers",
            "drown_immunity",
            "accelerate_machines",
            "accelerate_hoppers",
            "biological_aging",
            "sleep_hunger"
    );

    private static volatile GameRuleCommandBridge activeBridge;

    public static GameRuleCommandBridge getBridge() {
        if (activeBridge == null) {
            activeBridge = createBridge();
        }
        return activeBridge;
    }

    public static GameRuleCommandBridge createBridge() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();

        bridge.registerInteger("engine_tps", TrueSleepRules.ENGINE_TPS, 50, 20, 200);
        bridge.registerInteger("virtual_tps", TrueSleepRules.VIRTUAL_TPS_TARGET, 1000, 20, 100000);
        bridge.registerInteger("sleep_threshold", TrueSleepRules.SLEEP_THRESHOLD, 12542, 0, 23999);
        bridge.registerInteger("wake_time", TrueSleepRules.WAKE_TIME, 0, 0, 23999);
        bridge.registerBoolean("freeze_mobs", TrueSleepRules.MOBS_FROZEN, true);
        bridge.registerBoolean("freeze_workers", TrueSleepRules.WORKER_MOBS_FROZEN, false);
        bridge.registerBoolean("drown_immunity", TrueSleepRules.DROWN_IMMUNITY, true);
        bridge.registerBoolean("accelerate_machines", TrueSleepRules.ACCELERATE_MACHINES, true);
        bridge.registerBoolean("accelerate_hoppers", TrueSleepRules.ACCELERATE_HOPPERS, true);
        bridge.registerBoolean("biological_aging", TrueSleepRules.BIOLOGICAL_AGING, true);
        bridge.registerBoolean("sleep_hunger", TrueSleepRules.SLEEP_HUNGER, true);

        syncToBridge(bridge);

        bridge.setSaveCallback(server -> {
            syncFromBridge(bridge);
            TrueSleepConfig.save();
        });

        return bridge;
    }

    public static void configure(UniversalCommandRegistry.Builder builder) {
        configure(builder, getBridge());
    }

    public static void configure(UniversalCommandRegistry.Builder builder, GameRuleCommandBridge bridge) {
        builder
            .withRootLiteral(ROOT_LITERAL)
            .withModName(MOD_NAME)
            .withBridge(bridge)
            .withStatusSupplier(level -> {
                boolean warping = TimeWarpManager.get().isWarping();
                long stride = TimeWarpManager.get().getStride();
                return List.of(
                    "Warp State: " + (warping ? "ACTIVE (Accelerating)" : "IDLE"),
                    "Current Stride: " + stride + "x"
                );
            })
            .withReloadAction(server -> {
                TrueSleepConfig.load();
                syncToBridge(bridge);
                if (server != null) {
                    var rules = server.getGameRules();
                    TrueSleepConfig c = TrueSleepConfig.get();
                    rules.set(TrueSleepRules.ENGINE_TPS, (int) c.engineTps, server);
                    rules.set(TrueSleepRules.VIRTUAL_TPS_TARGET, (int) c.virtualTps, server);
                    rules.set(TrueSleepRules.SLEEP_THRESHOLD, c.sleepThreshold, server);
                    rules.set(TrueSleepRules.WAKE_TIME, c.wakeTime, server);
                    rules.set(TrueSleepRules.MOBS_FROZEN, c.freezeMobs, server);
                    rules.set(TrueSleepRules.WORKER_MOBS_FROZEN, c.freezeWorkers, server);
                    rules.set(TrueSleepRules.DROWN_IMMUNITY, c.drownImmunity, server);
                    rules.set(TrueSleepRules.ACCELERATE_MACHINES, c.accelerateMachines, server);
                    rules.set(TrueSleepRules.ACCELERATE_HOPPERS, c.accelerateHoppers, server);
                    rules.set(TrueSleepRules.BIOLOGICAL_AGING, c.biologicalAging, server);
                    rules.set(TrueSleepRules.SLEEP_HUNGER, c.sleepHunger, server);
                }
            });
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        UniversalCommandRegistry.Builder builder = UniversalCommandRegistry.builder(TrueSleep.MOD_ID, dispatcher);
        configure(builder);
        builder.register();
    }

    public static void syncFromBridge(GameRuleCommandBridge bridge) {
        if (bridge == null) return;
        TrueSleepConfig config = TrueSleepConfig.get();

        Object engineVal = bridge.getValue(null, "engine_tps");
        if (engineVal instanceof Number n) config.engineTps = n.floatValue();

        Object virtualVal = bridge.getValue(null, "virtual_tps");
        if (virtualVal instanceof Number n) config.virtualTps = n.floatValue();

        Object thresholdVal = bridge.getValue(null, "sleep_threshold");
        if (thresholdVal instanceof Number n) config.sleepThreshold = n.intValue();

        Object wakeVal = bridge.getValue(null, "wake_time");
        if (wakeVal instanceof Number n) config.wakeTime = n.intValue();

        Object mobsVal = bridge.getValue(null, "freeze_mobs");
        if (mobsVal instanceof Boolean b) config.freezeMobs = b;

        Object workersVal = bridge.getValue(null, "freeze_workers");
        if (workersVal instanceof Boolean b) config.freezeWorkers = b;

        Object drownVal = bridge.getValue(null, "drown_immunity");
        if (drownVal instanceof Boolean b) config.drownImmunity = b;

        Object machinesVal = bridge.getValue(null, "accelerate_machines");
        if (machinesVal instanceof Boolean b) config.accelerateMachines = b;

        Object hoppersVal = bridge.getValue(null, "accelerate_hoppers");
        if (hoppersVal instanceof Boolean b) config.accelerateHoppers = b;

        Object agingVal = bridge.getValue(null, "biological_aging");
        if (agingVal instanceof Boolean b) config.biologicalAging = b;

        Object hungerVal = bridge.getValue(null, "sleep_hunger");
        if (hungerVal instanceof Boolean b) config.sleepHunger = b;
    }

    public static void syncToBridge(GameRuleCommandBridge bridge) {
        if (bridge == null) return;
        TrueSleepConfig config = TrueSleepConfig.get();

        if (bridge.getRuleEntry("engine_tps") != null) bridge.getRuleEntry("engine_tps").setCurrentValue((int) config.engineTps);
        if (bridge.getRuleEntry("virtual_tps") != null) bridge.getRuleEntry("virtual_tps").setCurrentValue((int) config.virtualTps);
        if (bridge.getRuleEntry("sleep_threshold") != null) bridge.getRuleEntry("sleep_threshold").setCurrentValue(config.sleepThreshold);
        if (bridge.getRuleEntry("wake_time") != null) bridge.getRuleEntry("wake_time").setCurrentValue(config.wakeTime);
        if (bridge.getRuleEntry("freeze_mobs") != null) bridge.getRuleEntry("freeze_mobs").setCurrentValue(config.freezeMobs);
        if (bridge.getRuleEntry("freeze_workers") != null) bridge.getRuleEntry("freeze_workers").setCurrentValue(config.freezeWorkers);
        if (bridge.getRuleEntry("drown_immunity") != null) bridge.getRuleEntry("drown_immunity").setCurrentValue(config.drownImmunity);
        if (bridge.getRuleEntry("accelerate_machines") != null) bridge.getRuleEntry("accelerate_machines").setCurrentValue(config.accelerateMachines);
        if (bridge.getRuleEntry("accelerate_hoppers") != null) bridge.getRuleEntry("accelerate_hoppers").setCurrentValue(config.accelerateHoppers);
        if (bridge.getRuleEntry("biological_aging") != null) bridge.getRuleEntry("biological_aging").setCurrentValue(config.biologicalAging);
        if (bridge.getRuleEntry("sleep_hunger") != null) bridge.getRuleEntry("sleep_hunger").setCurrentValue(config.sleepHunger);
    }
}
