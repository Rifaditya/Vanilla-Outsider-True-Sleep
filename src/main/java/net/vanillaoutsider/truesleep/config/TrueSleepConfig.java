package net.vanillaoutsider.truesleep.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class TrueSleepConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("truesleep.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static TrueSleepConfig INSTANCE = new TrueSleepConfig();

    public float engineTps = 50.0f; // Stable engine speed for lag-free simulation
    public float virtualTps = 1000.0f; // Target time speed (Stride = Virtual / Engine)
    
    // Dreamweaver (v1.1.0)
    public int sleepThreshold = 12542; // Vanilla default. Set to 0 for all-day sleep.
    public int wakeTime = 0; // Vanilla default (Sunrise). Set to 13000 for Sunset.
    public boolean drownImmunity = true; // Prevents drowning during warp.

    public boolean wakeAtMorning = true; // Legacy flag, effectively replaced by wakeTime logic but kept for safety

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                INSTANCE = GSON.fromJson(reader, TrueSleepConfig.class);
            } catch (Exception e) {
                LoggerFactory.getLogger("True Sleep").error("Failed to load config", e);
            }
        }
        save();
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(INSTANCE, writer);
        } catch (Exception e) {
            LoggerFactory.getLogger("True Sleep").error("Failed to save config", e);
        }
    }

    public static TrueSleepConfig get() {
        return INSTANCE;
    }
}
