// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: ModMenuIntegration.java (26.2+)
package net.vanillaoutsider.truesleep.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.dasik.social.api.config.GuiHelper;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return GuiHelper.getOptionalFactory(
                "vanilla-outsider-true-sleep",
                "net.vanillaoutsider.truesleep.config.ClothConfigScreenHelper",
                "createFactory"
        );
    }
}
