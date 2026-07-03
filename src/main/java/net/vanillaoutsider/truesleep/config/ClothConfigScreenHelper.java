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
// Verified against: ClothConfigScreenHelper.java (26.1.2+)
package net.vanillaoutsider.truesleep.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigScreenHelper {
    public static ConfigScreenFactory<?> createFactory() {
        return ClothConfigScreenHelper::createScreen;
    }

    public static Screen createScreen(Screen parent) {
        TrueSleepConfig config = TrueSleepConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.truesleep.title"));

        builder.setSavingRunnable(TrueSleepConfig::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // --- GENERAL CATEGORY ---
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.truesleep.category.general"));
        general.addEntry(entryBuilder.startTextDescription(Component.translatable("config.truesleep.warning")).build());

        general.addEntry(entryBuilder.startFloatField(Component.translatable("config.truesleep.engineTps"), config.engineTps)
                .setDefaultValue(50.0f)
                .setSaveConsumer(val -> config.engineTps = val)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.translatable("config.truesleep.virtualTps"), config.virtualTps)
                .setDefaultValue(1000.0f)
                .setSaveConsumer(val -> config.virtualTps = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.truesleep.accelerateMachines"), config.accelerateMachines)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.accelerateMachines = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.truesleep.accelerateHoppers"), config.accelerateHoppers)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.accelerateHoppers = val)
                .build());

        // --- DREAMWEAVER CATEGORY ---
        ConfigCategory dreamweaver = builder.getOrCreateCategory(Component.translatable("config.truesleep.category.dreamweaver"));

        dreamweaver.addEntry(entryBuilder.startIntField(Component.translatable("config.truesleep.sleepThreshold"), config.sleepThreshold)
                .setDefaultValue(12542)
                .setSaveConsumer(val -> config.sleepThreshold = val)
                .build());

        dreamweaver.addEntry(entryBuilder.startIntField(Component.translatable("config.truesleep.wakeTime"), config.wakeTime)
                .setDefaultValue(0)
                .setSaveConsumer(val -> config.wakeTime = val)
                .build());

        dreamweaver.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.truesleep.drownImmunity"), config.drownImmunity)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.drownImmunity = val)
                .build());

        dreamweaver.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.truesleep.freezeMobs"), config.freezeMobs)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.freezeMobs = val)
                .build());

        dreamweaver.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.truesleep.freezeWorkers"), config.freezeWorkers)
                .setDefaultValue(false)
                .setSaveConsumer(val -> config.freezeWorkers = val)
                .build());

        return builder.build();
    }
}
