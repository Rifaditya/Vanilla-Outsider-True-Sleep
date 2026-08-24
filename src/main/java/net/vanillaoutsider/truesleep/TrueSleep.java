// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.vanillaoutsider.truesleep.command.TrueSleepCommand;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrueSleep implements ModInitializer {
    public static final String MOD_ID = "vanilla-outsider-true-sleep";
    public static final Logger LOGGER = LoggerFactory.getLogger("True Sleep");

    @Override
    public void onInitialize() {
        net.vanillaoutsider.truesleep.util.ModVersionGuard.checkClass("True Sleep", "net.minecraft.world.entity.player.Player");
        LOGGER.info("Initializing True Sleep (Time Warp)...");
        TrueSleepRules.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TrueSleepCommand.register(dispatcher));
    }
}

