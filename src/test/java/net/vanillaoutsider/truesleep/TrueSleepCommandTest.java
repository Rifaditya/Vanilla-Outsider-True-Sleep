// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.vanillaoutsider.truesleep.command.TrueSleepCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 3: Comprehensive Brigadier Command Tree & Execution Suite for True Sleep.
 * Validates root registration, subcommands, suggestions provider, permission gating,
 * argument parsing, and range constraints under headless Minecraft environment.
 */
class TrueSleepCommandTest {

    private CommandDispatcher<CommandSourceStack> dispatcher;
    private CommandSourceStack adminSource;
    private CommandSourceStack unprivilegedSource;

    private static final List<String> EXPECTED_SETTINGS = List.of(
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

    @BeforeAll
    static void initMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @BeforeEach
    void setUp() {
        dispatcher = new CommandDispatcher<>();
        TrueSleepCommand.register(dispatcher);

        adminSource = new CommandSourceStack(
                CommandSource.NULL,
                Vec3.ZERO,
                Vec2.ZERO,
                null,
                PermissionSet.ALL_PERMISSIONS,
                "AdminTester",
                Component.literal("AdminTester"),
                null,
                null
        );

        unprivilegedSource = new CommandSourceStack(
                CommandSource.NULL,
                Vec3.ZERO,
                Vec2.ZERO,
                null,
                PermissionSet.NO_PERMISSIONS,
                "GuestTester",
                Component.literal("GuestTester"),
                null,
                null
        );
    }

    @Test
    @DisplayName("Pillar 3: Root /truesleep command is registered with all expected subcommands")
    void testRootCommandRegistration() {
        CommandNode<CommandSourceStack> rootNode = dispatcher.getRoot().getChild("truesleep");
        assertNotNull(rootNode, "Root /truesleep command node must be registered in dispatcher");

        String[] expectedSubcommands = {"help", "status", "get", "set", "reset", "reload"};
        for (String sub : expectedSubcommands) {
            assertNotNull(rootNode.getChild(sub), "Subcommand /truesleep " + sub + " must exist in command hierarchy");
        }
    }

    @Test
    @DisplayName("Pillar 3: Tab completion suggestions for /truesleep get include all 11 setting keys")
    void testGetCompletions() throws Exception {
        CompletableFuture<Suggestions> future = dispatcher.getCompletionSuggestions(
                dispatcher.parse("truesleep get ", adminSource)
        );
        Suggestions suggestions = future.get();
        List<String> list = suggestions.getList().stream().map(Suggestion::getText).toList();

        assertEquals(11, EXPECTED_SETTINGS.size(), "There must be exactly 11 settings registered");
        for (String setting : EXPECTED_SETTINGS) {
            assertTrue(list.contains(setting), "Suggestions must include setting key: " + setting);
        }
    }

    @Test
    @DisplayName("Pillar 3: All valid /truesleep set branches parse properly for booleans and integers")
    void testSetSubtreeParsing() {
        // Integer settings within valid ranges
        assertParseSuccess("truesleep set engine_tps 50", adminSource);
        assertParseSuccess("truesleep set virtual_tps 1000", adminSource);
        assertParseSuccess("truesleep set sleep_threshold 12542", adminSource);
        assertParseSuccess("truesleep set wake_time 0", adminSource);

        // Boolean settings
        assertParseSuccess("truesleep set freeze_mobs true", adminSource);
        assertParseSuccess("truesleep set freeze_mobs false", adminSource);
        assertParseSuccess("truesleep set freeze_workers true", adminSource);
        assertParseSuccess("truesleep set freeze_workers false", adminSource);
        assertParseSuccess("truesleep set drown_immunity true", adminSource);
        assertParseSuccess("truesleep set drown_immunity false", adminSource);
        assertParseSuccess("truesleep set accelerate_machines true", adminSource);
        assertParseSuccess("truesleep set accelerate_machines false", adminSource);
        assertParseSuccess("truesleep set accelerate_hoppers true", adminSource);
        assertParseSuccess("truesleep set accelerate_hoppers false", adminSource);
        assertParseSuccess("truesleep set biological_aging true", adminSource);
        assertParseSuccess("truesleep set biological_aging false", adminSource);
        assertParseSuccess("truesleep set sleep_hunger true", adminSource);
        assertParseSuccess("truesleep set sleep_hunger false", adminSource);
    }

    @Test
    @DisplayName("Pillar 3: Read-only commands (overview, help, status, get) parse correctly")
    void testReadOnlyCommandsParsing() {
        assertParseSuccess("truesleep", adminSource);
        assertParseSuccess("truesleep help", adminSource);
        assertParseSuccess("truesleep status", adminSource);

        for (String setting : EXPECTED_SETTINGS) {
            assertParseSuccess("truesleep get " + setting, adminSource);
        }
    }

    @Test
    @DisplayName("Pillar 3: Security & Permission Gating enforces operator level on modifying commands")
    void testPermissionGatingForModifyingCommands() {
        // Read-only commands are permissible for unprivileged users
        assertParseSuccess("truesleep", unprivilegedSource);
        assertParseSuccess("truesleep help", unprivilegedSource);
        assertParseSuccess("truesleep status", unprivilegedSource);
        assertParseSuccess("truesleep get engine_tps", unprivilegedSource);

        // Mutating commands must fail permission check for unprivileged users
        assertParsePermissionDenied("truesleep set engine_tps 60", unprivilegedSource);
        assertParsePermissionDenied("truesleep set freeze_mobs false", unprivilegedSource);
        assertParsePermissionDenied("truesleep reset", unprivilegedSource);
        assertParsePermissionDenied("truesleep reload", unprivilegedSource);
    }

    @Test
    @DisplayName("Pillar 3: Argument range boundary constraints are enforced by Brigadier parser")
    void testArgumentBoundsValidation() {
        // engine_tps bounded [20, 200]
        assertParseSuccess("truesleep set engine_tps 20", adminSource);
        assertParseSuccess("truesleep set engine_tps 200", adminSource);
        assertParseFailure("truesleep set engine_tps 19", adminSource);
        assertParseFailure("truesleep set engine_tps 201", adminSource);

        // virtual_tps bounded [20, 100000]
        assertParseSuccess("truesleep set virtual_tps 20", adminSource);
        assertParseSuccess("truesleep set virtual_tps 100000", adminSource);
        assertParseFailure("truesleep set virtual_tps 10", adminSource);
        assertParseFailure("truesleep set virtual_tps -50", adminSource);

        // sleep_threshold bounded [0, 23999]
        assertParseSuccess("truesleep set sleep_threshold 0", adminSource);
        assertParseSuccess("truesleep set sleep_threshold 23999", adminSource);
        assertParseFailure("truesleep set sleep_threshold -1", adminSource);
        assertParseFailure("truesleep set sleep_threshold 24000", adminSource);

        // wake_time bounded [0, 23999]
        assertParseSuccess("truesleep set wake_time 0", adminSource);
        assertParseSuccess("truesleep set wake_time 23999", adminSource);
        assertParseFailure("truesleep set wake_time -1", adminSource);
        assertParseFailure("truesleep set wake_time 24000", adminSource);
    }

    private void assertParseSuccess(String command, CommandSourceStack source) {
        ParseResults<CommandSourceStack> parse = dispatcher.parse(command, source);
        assertFalse(parse.getReader().canRead(),
                "Command parsing left unread input for: '" + command + "' -> " + parse.getReader().getRemaining());
        assertEquals(0, parse.getExceptions().size(),
                "Command parsing generated exceptions for: '" + command + "': " + parse.getExceptions());
        assertNotNull(parse.getContext().getCommand(),
                "Command node has no executable handler bound for: '" + command + "'");
    }

    private void assertParseFailure(String command, CommandSourceStack source) {
        ParseResults<CommandSourceStack> parse = dispatcher.parse(command, source);
        boolean failed = parse.getContext().getCommand() == null
                || parse.getReader().canRead()
                || !parse.getExceptions().isEmpty();
        assertTrue(failed, "Command expected to fail parsing but succeeded: " + command);
    }

    private void assertParsePermissionDenied(String command, CommandSourceStack source) {
        ParseResults<CommandSourceStack> parse = dispatcher.parse(command, source);
        boolean denied = parse.getContext().getCommand() == null
                || parse.getReader().canRead()
                || !parse.getExceptions().isEmpty();
        assertTrue(denied, "Unprivileged source unexpectedly passed permission check for: " + command);
    }
}
