package net.vanillaoutsider.truesleep;

import net.fabricmc.api.ModInitializer;
import net.vanillaoutsider.truesleep.config.TrueSleepRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrueSleep implements ModInitializer {
    public static final String MOD_ID = "vanilla-outsider-true-sleep";
    public static final Logger LOGGER = LoggerFactory.getLogger("True Sleep");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing True Sleep (Time Warp)...");
        TrueSleepRules.init();
    }
}
