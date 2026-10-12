// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.command;

// Verified against: CommandSourceStack.java (1.20.1)

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.dasik.social.api.config.DasikSupportHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.vanillaoutsider.truesleep.config.TrueSleepConfig;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import net.vanillaoutsider.truesleep.logic.TimeWarpManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TrueSleepCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrueSleepCommand.class);

    private static final List<String> SETTING_KEYS = List.of(
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

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("truesleep")
                .executes(TrueSleepCommand::executeOverview)
                .then(Commands.literal("help")
                        .executes(TrueSleepCommand::executeHelp))
                .then(Commands.literal("status")
                        .executes(TrueSleepCommand::executeStatus))
                .then(Commands.literal("get")
                        .then(Commands.argument("setting", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(SETTING_KEYS, builder))
                                .executes(TrueSleepCommand::executeGet)))
                .then(Commands.literal("set")
                        .requires(source -> source.hasPermission(2))
                        // Integer settings
                        .then(Commands.literal("engine_tps")
                                .then(Commands.argument("value", IntegerArgumentType.integer(20, 200))
                                         .executes(ctx -> setEngineTps(ctx, IntegerArgumentType.getInteger(ctx, "value")))))
                        .then(Commands.literal("virtual_tps")
                                .then(Commands.argument("value", IntegerArgumentType.integer(20, 100000))
                                         .executes(ctx -> setVirtualTps(ctx, IntegerArgumentType.getInteger(ctx, "value")))))
                        .then(Commands.literal("sleep_threshold")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 23999))
                                         .executes(ctx -> setSleepThreshold(ctx, IntegerArgumentType.getInteger(ctx, "value")))))
                        .then(Commands.literal("wake_time")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 23999))
                                         .executes(ctx -> setWakeTime(ctx, IntegerArgumentType.getInteger(ctx, "value")))))
                        // Boolean settings
                        .then(Commands.literal("freeze_mobs")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                         .executes(ctx -> setFreezeMobs(ctx, BoolArgumentType.getBool(ctx, "value")))))
                        .then(Commands.literal("freeze_workers")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                         .executes(ctx -> setFreezeWorkers(ctx, BoolArgumentType.getBool(ctx, "value")))))
                        .then(Commands.literal("drown_immunity")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                         .executes(ctx -> setDrownImmunity(ctx, BoolArgumentType.getBool(ctx, "value")))))
                        .then(Commands.literal("accelerate_machines")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                         .executes(ctx -> setAccelerateMachines(ctx, BoolArgumentType.getBool(ctx, "value")))))
                        .then(Commands.literal("accelerate_hoppers")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                         .executes(ctx -> setAccelerateHoppers(ctx, BoolArgumentType.getBool(ctx, "value")))))
                        .then(Commands.literal("biological_aging")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                         .executes(ctx -> setBiologicalAging(ctx, BoolArgumentType.getBool(ctx, "value")))))
                        .then(Commands.literal("sleep_hunger")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                         .executes(ctx -> setSleepHunger(ctx, BoolArgumentType.getBool(ctx, "value"))))))
                .then(Commands.literal("reset")
                        .requires(source -> source.hasPermission(2))
                        .executes(TrueSleepCommand::executeReset))
                .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2))
                        .executes(TrueSleepCommand::executeReload)));
    }

    private static int executeOverview(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        boolean warping = TimeWarpManager.get().isWarping();
        long stride = TimeWarpManager.get().getStride();

        source.sendSuccess(() -> Component.literal(
                "§6[Vanilla Outsider: True Sleep]§r\n" +
                "§7Status: " + (warping ? "§aWarping (Stride: " + stride + "x)" : "§eIdle") + "§r\n" +
                "§7Type §a/truesleep help§7 for command list or §a/truesleep status§7 for live diagnostics."
        ), false);
        return 1;
    }

    private static int executeHelp(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> Component.literal(
                "§6=== True Sleep Command Suite ===§r\n" +
                "§a/truesleep status§r - View live time warp metrics & active settings\n" +
                "§a/truesleep get <setting>§r - Query specific GameRule value\n" +
                "§a/truesleep set <setting> <value>§r - Update GameRule & sync to config\n" +
                "§a/truesleep reset§r - Reset all settings to defaults & sync to config\n" +
                "§a/truesleep reload§r - Reload JSON config from disk and apply to world\n" +
                "§7Valid Settings: §fengine_tps, virtual_tps, sleep_threshold, wake_time, freeze_mobs, freeze_workers, drown_immunity, accelerate_machines, accelerate_hoppers, biological_aging, sleep_hunger§r"
        ), false);
        source.sendSuccess(DasikSupportHelper::getCommandFooter, false);
        return 1;
    }

    private static int executeStatus(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        GameRules rules = source.getLevel().getGameRules();

        boolean warping = TimeWarpManager.get().isWarping();
        long stride = TimeWarpManager.get().getStride();
        int engineTps = rules.getInt(TrueSleepRules.ENGINE_TPS);
        int virtualTps = rules.getInt(TrueSleepRules.VIRTUAL_TPS_TARGET);
        int threshold = rules.getInt(TrueSleepRules.SLEEP_THRESHOLD);
        int wakeTime = rules.getInt(TrueSleepRules.WAKE_TIME);
        boolean freezeMobs = rules.getBoolean(TrueSleepRules.MOBS_FROZEN);
        boolean freezeWorkers = rules.getBoolean(TrueSleepRules.WORKER_MOBS_FROZEN);
        boolean drownImmunity = rules.getBoolean(TrueSleepRules.DROWN_IMMUNITY);
        boolean accelMachines = rules.getBoolean(TrueSleepRules.ACCELERATE_MACHINES);
        boolean accelHoppers = rules.getBoolean(TrueSleepRules.ACCELERATE_HOPPERS);
        boolean bioAging = rules.getBoolean(TrueSleepRules.BIOLOGICAL_AGING);
        boolean sleepHunger = rules.getBoolean(TrueSleepRules.SLEEP_HUNGER);

        source.sendSuccess(() -> Component.literal(
                "§6=== True Sleep Live Status ===§r\n" +
                "§e• Warp State: " + (warping ? "§aACTIVE (Accelerating)" : "§7IDLE") + "§r\n" +
                "§e• Current Stride: §b" + stride + "x§r\n" +
                "§e• Performance Limit (Engine TPS): §f" + engineTps + " TPS§r\n" +
                "§e• Time Speed (Virtual TPS): §f" + virtualTps + " TPS§r\n" +
                "§e• Sleep Threshold: §f" + threshold + " ticks (" + formatTimeTicks(threshold) + ")§r\n" +
                "§e• Wake Time: §f" + wakeTime + " ticks (" + formatTimeTicks(wakeTime) + ")§r\n" +
                "§e• Freeze Mobs: " + (freezeMobs ? "§aON" : "§cOFF") + "§r\n" +
                "§e• Freeze Villagers: " + (freezeWorkers ? "§aON" : "§cOFF") + "§r\n" +
                "§e• Drown Immunity: " + (drownImmunity ? "§aON" : "§cOFF") + "§r\n" +
                "§e• Accelerate Machines: " + (accelMachines ? "§aON" : "§cOFF") + "§r\n" +
                "§e• Accelerate Hoppers: " + (accelHoppers ? "§aON" : "§cOFF") + "§r\n" +
                "§e• Biological Farm Aging: " + (bioAging ? "§aON" : "§cOFF") + "§r\n" +
                "§e• Sleep Hunger Drain: " + (sleepHunger ? "§aON" : "§cOFF") + "§r"
        ), false);
        source.sendSuccess(DasikSupportHelper::getCommandFooter, false);
        return 1;
    }

    private static int executeGet(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String setting = StringArgumentType.getString(context, "setting").toLowerCase();
        GameRules rules = source.getLevel().getGameRules();

        switch (setting) {
            case "engine_tps" -> {
                int val = rules.getInt(TrueSleepRules.ENGINE_TPS);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Performance Limit (Engine TPS): §b" + val + " TPS"), false);
            }
            case "virtual_tps" -> {
                int val = rules.getInt(TrueSleepRules.VIRTUAL_TPS_TARGET);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Time Speed (Virtual TPS): §b" + val + " TPS"), false);
            }
            case "sleep_threshold" -> {
                int val = rules.getInt(TrueSleepRules.SLEEP_THRESHOLD);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Sleep Threshold: §b" + val + " ticks (" + formatTimeTicks(val) + ")"), false);
            }
            case "wake_time" -> {
                int val = rules.getInt(TrueSleepRules.WAKE_TIME);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Wake Time: §b" + val + " ticks (" + formatTimeTicks(val) + ")"), false);
            }
            case "freeze_mobs" -> {
                boolean val = rules.getBoolean(TrueSleepRules.MOBS_FROZEN);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Freeze Mobs: " + (val ? "§aON" : "§cOFF")), false);
            }
            case "freeze_workers" -> {
                boolean val = rules.getBoolean(TrueSleepRules.WORKER_MOBS_FROZEN);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Freeze Villagers: " + (val ? "§aON" : "§cOFF")), false);
            }
            case "drown_immunity" -> {
                boolean val = rules.getBoolean(TrueSleepRules.DROWN_IMMUNITY);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Drown Immunity: " + (val ? "§aON" : "§cOFF")), false);
            }
            case "accelerate_machines" -> {
                boolean val = rules.getBoolean(TrueSleepRules.ACCELERATE_MACHINES);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Accelerate Machines: " + (val ? "§aON" : "§cOFF")), false);
            }
            case "accelerate_hoppers" -> {
                boolean val = rules.getBoolean(TrueSleepRules.ACCELERATE_HOPPERS);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Accelerate Hoppers: " + (val ? "§aON" : "§cOFF")), false);
            }
            case "biological_aging" -> {
                boolean val = rules.getBoolean(TrueSleepRules.BIOLOGICAL_AGING);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Biological Farm Aging: " + (val ? "§aON" : "§cOFF")), false);
            }
            case "sleep_hunger" -> {
                boolean val = rules.getBoolean(TrueSleepRules.SLEEP_HUNGER);
                source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Sleep Hunger Drain: " + (val ? "§aON" : "§cOFF")), false);
            }
            default -> {
                source.sendFailure(Component.literal("§cUnknown setting: '" + setting + "'. Use /truesleep help for list of settings."));
                return 0;
            }
        }
        return 1;
    }

    private static int setEngineTps(CommandContext<CommandSourceStack> context, int value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.ENGINE_TPS).set(value, source.getServer());
        TrueSleepConfig.get().engineTps = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Performance Limit set to §b" + value + " TPS§r (synced to config)."), true);
        return 1;
    }

    private static int setVirtualTps(CommandContext<CommandSourceStack> context, int value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.VIRTUAL_TPS_TARGET).set(value, source.getServer());
        TrueSleepConfig.get().virtualTps = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Time Speed set to §b" + value + " TPS§r (synced to config)."), true);
        return 1;
    }

    private static int setSleepThreshold(CommandContext<CommandSourceStack> context, int value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.SLEEP_THRESHOLD).set(value, source.getServer());
        TrueSleepConfig.get().sleepThreshold = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Sleep Threshold set to §b" + value + " ticks§r (" + formatTimeTicks(value) + ", synced to config)."), true);
        return 1;
    }

    private static int setWakeTime(CommandContext<CommandSourceStack> context, int value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.WAKE_TIME).set(value, source.getServer());
        TrueSleepConfig.get().wakeTime = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Wake Time set to §b" + value + " ticks§r (" + formatTimeTicks(value) + ", synced to config)."), true);
        return 1;
    }

    private static int setFreezeMobs(CommandContext<CommandSourceStack> context, boolean value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.MOBS_FROZEN).set(value, source.getServer());
        TrueSleepConfig.get().freezeMobs = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Freeze Mobs set to " + (value ? "§aON" : "§cOFF") + "§r (synced to config)."), true);
        return 1;
    }

    private static int setFreezeWorkers(CommandContext<CommandSourceStack> context, boolean value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.WORKER_MOBS_FROZEN).set(value, source.getServer());
        TrueSleepConfig.get().freezeWorkers = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Freeze Villagers set to " + (value ? "§aON" : "§cOFF") + "§r (synced to config)."), true);
        return 1;
    }

    private static int setDrownImmunity(CommandContext<CommandSourceStack> context, boolean value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.DROWN_IMMUNITY).set(value, source.getServer());
        TrueSleepConfig.get().drownImmunity = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Drown Immunity set to " + (value ? "§aON" : "§cOFF") + "§r (synced to config)."), true);
        return 1;
    }

    private static int setAccelerateMachines(CommandContext<CommandSourceStack> context, boolean value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.ACCELERATE_MACHINES).set(value, source.getServer());
        TrueSleepConfig.get().accelerateMachines = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Accelerate Machines set to " + (value ? "§aON" : "§cOFF") + "§r (synced to config)."), true);
        return 1;
    }

    private static int setAccelerateHoppers(CommandContext<CommandSourceStack> context, boolean value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.ACCELERATE_HOPPERS).set(value, source.getServer());
        TrueSleepConfig.get().accelerateHoppers = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Accelerate Hoppers set to " + (value ? "§aON" : "§cOFF") + "§r (synced to config)."), true);
        return 1;
    }

    private static int setBiologicalAging(CommandContext<CommandSourceStack> context, boolean value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.BIOLOGICAL_AGING).set(value, source.getServer());
        TrueSleepConfig.get().biologicalAging = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Biological Farm Aging set to " + (value ? "§aON" : "§cOFF") + "§r (synced to config)."), true);
        return 1;
    }

    private static int setSleepHunger(CommandContext<CommandSourceStack> context, boolean value) {
        CommandSourceStack source = context.getSource();
        source.getLevel().getGameRules().getRule(TrueSleepRules.SLEEP_HUNGER).set(value, source.getServer());
        TrueSleepConfig.get().sleepHunger = value;
        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Sleep Hunger Drain set to " + (value ? "§aON" : "§cOFF") + "§r (synced to config)."), true);
        return 1;
    }

    private static int executeReset(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        MinecraftServer server = source.getServer();
        GameRules rules = source.getLevel().getGameRules();

        TrueSleepConfig.get().resetDefaults();
        TrueSleepConfig config = TrueSleepConfig.get();

        rules.getRule(TrueSleepRules.ENGINE_TPS).set((int) config.engineTps, server);
        rules.getRule(TrueSleepRules.VIRTUAL_TPS_TARGET).set((int) config.virtualTps, server);
        rules.getRule(TrueSleepRules.SLEEP_THRESHOLD).set(config.sleepThreshold, server);
        rules.getRule(TrueSleepRules.WAKE_TIME).set(config.wakeTime, server);
        rules.getRule(TrueSleepRules.MOBS_FROZEN).set(config.freezeMobs, server);
        rules.getRule(TrueSleepRules.WORKER_MOBS_FROZEN).set(config.freezeWorkers, server);
        rules.getRule(TrueSleepRules.DROWN_IMMUNITY).set(config.drownImmunity, server);
        rules.getRule(TrueSleepRules.ACCELERATE_MACHINES).set(config.accelerateMachines, server);
        rules.getRule(TrueSleepRules.ACCELERATE_HOPPERS).set(config.accelerateHoppers, server);
        rules.getRule(TrueSleepRules.BIOLOGICAL_AGING).set(config.biologicalAging, server);
        rules.getRule(TrueSleepRules.SLEEP_HUNGER).set(config.sleepHunger, server);

        TrueSleepConfig.save();
        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r All settings reset to default and synchronized to config."), true);
        return 1;
    }

    private static int executeReload(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        MinecraftServer server = source.getServer();
        GameRules rules = source.getLevel().getGameRules();

        TrueSleepConfig.load();
        TrueSleepConfig config = TrueSleepConfig.get();

        rules.getRule(TrueSleepRules.ENGINE_TPS).set((int) config.engineTps, server);
        rules.getRule(TrueSleepRules.VIRTUAL_TPS_TARGET).set((int) config.virtualTps, server);
        rules.getRule(TrueSleepRules.SLEEP_THRESHOLD).set(config.sleepThreshold, server);
        rules.getRule(TrueSleepRules.WAKE_TIME).set(config.wakeTime, server);
        rules.getRule(TrueSleepRules.MOBS_FROZEN).set(config.freezeMobs, server);
        rules.getRule(TrueSleepRules.WORKER_MOBS_FROZEN).set(config.freezeWorkers, server);
        rules.getRule(TrueSleepRules.DROWN_IMMUNITY).set(config.drownImmunity, server);
        rules.getRule(TrueSleepRules.ACCELERATE_MACHINES).set(config.accelerateMachines, server);
        rules.getRule(TrueSleepRules.ACCELERATE_HOPPERS).set(config.accelerateHoppers, server);
        rules.getRule(TrueSleepRules.BIOLOGICAL_AGING).set(config.biologicalAging, server);
        rules.getRule(TrueSleepRules.SLEEP_HUNGER).set(config.sleepHunger, server);

        source.sendSuccess(() -> Component.literal("§6[True Sleep]§r Configuration reloaded from disk and applied to active world GameRules."), true);
        return 1;
    }

    private static String formatTimeTicks(int ticks) {
        if (ticks == 0) return "Sunrise";
        if (ticks == 6000) return "Noon";
        if (ticks == 12000) return "Sunset";
        if (ticks == 12542) return "Night Start";
        if (ticks == 18000) return "Midnight";
        int hours = (ticks / 1000 + 6) % 24;
        int minutes = (ticks % 1000) * 60 / 1000;
        return String.format("%02d:%02d", hours, minutes);
    }
}
