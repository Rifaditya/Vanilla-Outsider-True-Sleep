// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.vanillaoutsider.truesleep.command.TrueSleepCommand;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrueSleep implements ModInitializer {
    public static final String MOD_ID = "vanilla-outsider-true-sleep";
    public static final Logger LOGGER = LoggerFactory.getLogger("True Sleep");

    @Override
    public void onInitialize() {
        LOGGER.info("True Sleep for Minecraft 1.21.1 initialized.");
        TrueSleepRules.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TrueSleepCommand.register(dispatcher));

        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> {
            if (player.level() instanceof ServerLevel serverLevel) {
                int threshold = serverLevel.getGameRules().getInt(TrueSleepRules.SLEEP_THRESHOLD);
                long time = serverLevel.getDayTime() % 24000L;
                if (time >= threshold) {
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }
}
