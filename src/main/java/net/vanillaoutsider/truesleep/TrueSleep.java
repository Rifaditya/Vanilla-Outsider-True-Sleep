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
