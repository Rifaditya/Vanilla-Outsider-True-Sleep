// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.Bootstrap;
import net.vanillaoutsider.truesleep.command.TrueSleepCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrueSleepCommandTest {

    @BeforeAll
    static void initMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    @DisplayName("Pillar 3: Brigadier command registration and root subcommands hierarchy")
    void testCommandRegistrationAndHierarchy() {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        TrueSleepCommand.register(dispatcher);

        CommandNode<CommandSourceStack> root = dispatcher.getRoot().getChild("truesleep");
        assertNotNull(root, "Root command '/truesleep' must be registered in dispatcher");

        assertNotNull(root.getChild("help"), "Command '/truesleep help' must be registered");
        assertNotNull(root.getChild("status"), "Command '/truesleep status' must be registered");
        assertNotNull(root.getChild("get"), "Command '/truesleep get' must be registered");
        assertNotNull(root.getChild("set"), "Command '/truesleep set' must be registered");
        assertNotNull(root.getChild("reset"), "Command '/truesleep reset' must be registered");
        assertNotNull(root.getChild("reload"), "Command '/truesleep reload' must be registered");
    }

    @Test
    @DisplayName("Pillar 3: Brigadier command settings argument and permission requirements")
    void testCommandSettingsAndPermissions() {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        TrueSleepCommand.register(dispatcher);

        CommandNode<CommandSourceStack> root = dispatcher.getRoot().getChild("truesleep");
        assertNotNull(root, "Root command '/truesleep' must be registered");

        CommandNode<CommandSourceStack> getNode = root.getChild("get");
        assertNotNull(getNode, "Subcommand '/truesleep get' must exist");
        assertNotNull(getNode.getChild("setting"), "'/truesleep get' must have 'setting' argument child");

        CommandNode<CommandSourceStack> setNode = root.getChild("set");
        assertNotNull(setNode, "Subcommand '/truesleep set' must exist");
        assertNotNull(setNode.getRequirement(), "'/truesleep set' must have a non-null requirement predicate");

        String[] expectedSettings = {
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
        };

        for (String setting : expectedSettings) {
            CommandNode<CommandSourceStack> settingNode = setNode.getChild(setting);
            assertNotNull(settingNode, "Subcommand '/truesleep set " + setting + "' must be registered");
            assertNotNull(settingNode.getChild("value"), "Setting node '" + setting + "' must have a 'value' argument");
        }
    }
}
