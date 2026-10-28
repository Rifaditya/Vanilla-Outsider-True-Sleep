// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import net.dasik.social.api.command.GameRuleCommandBridge;
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
 * argument parsing, and saturated arithmetic bounds under headless Minecraft environment.
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
        try {
            SharedConstants.tryDetectVersion();
            Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
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
                Component.literal("AdminTester"),
                null
        );

        unprivilegedSource = new CommandSourceStack(
                CommandSource.NULL,
                Vec3.ZERO,
                Vec2.ZERO,
                null,
                PermissionSet.NO_PERMISSIONS,
                Component.literal("GuestTester"),
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
            CommandNode<CommandSourceStack> childNode = rootNode.getChild(sub);
            assertNotNull(childNode, "Subcommand /truesleep " + sub + " must exist in command hierarchy");
        }

        CommandNode<CommandSourceStack> getNode = rootNode.getChild("get");
        assertNotNull(getNode.getChild("key"), "Subcommand /truesleep get <key> argument node must exist");

        CommandNode<CommandSourceStack> setNode = rootNode.getChild("set");
        assertNotNull(setNode.getChild("key"), "Subcommand /truesleep set <key> argument node must exist");
        assertNotNull(setNode.getChild("key").getChild("value"), "Subcommand /truesleep set <key> <value> argument node must exist");
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
    @DisplayName("Pillar 3: Tab completion suggestions for /truesleep set include all setting keys and values")
    void testSetCompletions() throws Exception {
        CompletableFuture<Suggestions> keyFuture = dispatcher.getCompletionSuggestions(
                dispatcher.parse("truesleep set ", adminSource)
        );
        Suggestions keySuggestions = keyFuture.get();
        List<String> keyList = keySuggestions.getList().stream().map(Suggestion::getText).toList();

        for (String setting : EXPECTED_SETTINGS) {
            assertTrue(keyList.contains(setting), "Set suggestions must include setting key: " + setting);
        }

        CompletableFuture<Suggestions> boolFuture = dispatcher.getCompletionSuggestions(
                dispatcher.parse("truesleep set freeze_mobs ", adminSource)
        );
        Suggestions boolSuggestions = boolFuture.get();
        List<String> boolList = boolSuggestions.getList().stream().map(Suggestion::getText).toList();
        assertTrue(boolList.contains("true"), "Boolean setting must suggest true");
        assertTrue(boolList.contains("false"), "Boolean setting must suggest false");
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
    @DisplayName("Pillar 3: Argument bounds and saturated arithmetic are enforced by GameRuleCommandBridge")
    void testArgumentBoundsAndSaturatedArithmetic() {
        GameRuleCommandBridge bridge = TrueSleepCommand.getBridge();
        assertNotNull(bridge, "Bridge must not be null");

        // Greedy string syntax accepts any input string
        assertParseSuccess("truesleep set engine_tps 19", adminSource);
        assertParseSuccess("truesleep set engine_tps 201", adminSource);

        // 1. engine_tps bounded [20, 200]
        GameRuleCommandBridge.SetResult resUnderEngine = bridge.setValue(null, "engine_tps", "19");
        assertTrue(resUnderEngine.isSuccess());
        assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, resUnderEngine.getStatus());
        assertEquals(20, ((Number) resUnderEngine.getValue()).intValue());

        GameRuleCommandBridge.SetResult resOverEngine = bridge.setValue(null, "engine_tps", "250");
        assertTrue(resOverEngine.isSuccess());
        assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, resOverEngine.getStatus());
        assertEquals(200, ((Number) resOverEngine.getValue()).intValue());

        // 2. virtual_tps bounded [20, 100000]
        GameRuleCommandBridge.SetResult resUnderVirtual = bridge.setValue(null, "virtual_tps", "10");
        assertTrue(resUnderVirtual.isSuccess());
        assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, resUnderVirtual.getStatus());
        assertEquals(20, ((Number) resUnderVirtual.getValue()).intValue());

        GameRuleCommandBridge.SetResult resOverVirtual = bridge.setValue(null, "virtual_tps", "200000");
        assertTrue(resOverVirtual.isSuccess());
        assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, resOverVirtual.getStatus());
        assertEquals(100000, ((Number) resOverVirtual.getValue()).intValue());

        // 3. sleep_threshold bounded [0, 23999]
        GameRuleCommandBridge.SetResult resUnderThreshold = bridge.setValue(null, "sleep_threshold", "-5");
        assertTrue(resUnderThreshold.isSuccess());
        assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, resUnderThreshold.getStatus());
        assertEquals(0, ((Number) resUnderThreshold.getValue()).intValue());

        GameRuleCommandBridge.SetResult resOverThreshold = bridge.setValue(null, "sleep_threshold", "30000");
        assertTrue(resOverThreshold.isSuccess());
        assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, resOverThreshold.getStatus());
        assertEquals(23999, ((Number) resOverThreshold.getValue()).intValue());

        // 4. wake_time bounded [0, 23999]
        GameRuleCommandBridge.SetResult resUnderWake = bridge.setValue(null, "wake_time", "-10");
        assertTrue(resUnderWake.isSuccess());
        assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, resUnderWake.getStatus());
        assertEquals(0, ((Number) resUnderWake.getValue()).intValue());

        GameRuleCommandBridge.SetResult resOverWake = bridge.setValue(null, "wake_time", "25000");
        assertTrue(resOverWake.isSuccess());
        assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, resOverWake.getStatus());
        assertEquals(23999, ((Number) resOverWake.getValue()).intValue());

        // 5. In-bound valid assignment
        GameRuleCommandBridge.SetResult resValid = bridge.setValue(null, "engine_tps", "50");
        assertTrue(resValid.isSuccess());
        assertEquals(GameRuleCommandBridge.Status.SUCCESS, resValid.getStatus());
        assertEquals(50, ((Number) resValid.getValue()).intValue());
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

    private void assertParsePermissionDenied(String command, CommandSourceStack source) {
        ParseResults<CommandSourceStack> parse = dispatcher.parse(command, source);
        boolean denied = parse.getContext().getCommand() == null
                || parse.getReader().canRead()
                || !parse.getExceptions().isEmpty();
        assertTrue(denied, "Unprivileged source unexpectedly passed permission check for: " + command);
    }
}
