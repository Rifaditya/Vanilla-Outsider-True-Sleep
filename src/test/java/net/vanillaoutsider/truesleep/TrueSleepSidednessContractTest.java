// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 5: Sidedness & Mixin Contract Verification Suite for Vanilla Outsider: True Sleep.
 * Asserts 100% separation between Common and Client logic, constant pool bytecode safety,
 * absence of unremapped reflection, and mixin manifest structural integrity.
 */
class TrueSleepSidednessContractTest {

    // Dynamic byte arrays to prevent constant pool self-matching (ERR-SELF-MATCH)
    private static final String CLIENT_SRC_FORBIDDEN = new String(new byte[]{
            110, 101, 116, 46, 109, 105, 110, 101, 99, 114, 97, 102, 116, 46, 99, 108, 105, 101, 110, 116, 46
    }); // "net.minecraft.client."

    private static final String BLAZE3D_SRC_FORBIDDEN = new String(new byte[]{
            99, 111, 109, 46, 109, 111, 106, 97, 110, 103, 46, 98, 108, 97, 122, 101, 51, 100, 46
    }); // "com.mojang.blaze3d."

    private static final String CLIENT_BYTECODE_FORBIDDEN = new String(new byte[]{
            110, 101, 116, 47, 109, 105, 110, 101, 99, 114, 97, 102, 116, 47, 99, 108, 105, 101, 110, 116, 47
    }); // "net/minecraft/client/"

    private static final String BLAZE3D_BYTECODE_FORBIDDEN = new String(new byte[]{
            99, 111, 109, 47, 109, 111, 106, 97, 110, 103, 47, 98, 108, 97, 122, 101, 51, 100, 47
    }); // "com/mojang/blaze3d/"

    private static final String LICENSE_HEADER = "// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3";

    @Test
    @DisplayName("Pillar 5: Common Sources & Bytecode Contain Zero Unguarded Client References")
    void testCommonSidednessIsolation() throws IOException {
        Path srcRoot = Paths.get("src/main/java");
        assertTrue(Files.exists(srcRoot), "src/main/java must exist");

        try (Stream<Path> stream = Files.walk(srcRoot)) {
            List<Path> javaFiles = stream.filter(p -> p.toString().endsWith(".java")).toList();
            assertFalse(javaFiles.isEmpty(), "Java source files must be found");

            for (Path file : javaFiles) {
                String normalized = file.toString().replace('\\', '/');
                String content = Files.readString(file, StandardCharsets.UTF_8);

                assertTrue(content.startsWith(LICENSE_HEADER),
                        "Missing GPLv3 license header on line 1 in: " + normalized);

                if (normalized.contains("/client/")) {
                    // Client files are permitted to reference client classes
                    continue;
                }

                assertFalse(content.contains(CLIENT_SRC_FORBIDDEN),
                        "Forbidden client package reference in common source file: " + normalized);
                assertFalse(content.contains(BLAZE3D_SRC_FORBIDDEN),
                        "Forbidden Blaze3D reference in common source file: " + normalized);
            }
        }

        Path classesRoot = Paths.get("build/classes/java/main");
        if (Files.exists(classesRoot)) {
            try (Stream<Path> stream = Files.walk(classesRoot)) {
                List<Path> classFiles = stream.filter(p -> p.toString().endsWith(".class")).toList();
                for (Path classFile : classFiles) {
                    String normalized = classFile.toString().replace('\\', '/');
                    if (normalized.contains("/client/")) {
                        continue;
                    }
                    String rawBytes = new String(Files.readAllBytes(classFile), StandardCharsets.ISO_8859_1);
                    assertFalse(rawBytes.contains(CLIENT_BYTECODE_FORBIDDEN),
                            "Forbidden client constant pool reference in common bytecode: " + normalized);
                    assertFalse(rawBytes.contains(BLAZE3D_BYTECODE_FORBIDDEN),
                            "Forbidden Blaze3D constant pool reference in common bytecode: " + normalized);
                }
            }
        }
    }

    @Test
    @DisplayName("Pillar 5: Legacy Guard Purged and Zero Unremapped Class.forName in Entrypoint")
    void testLegacyGuardPurgedAndNoUnremappedReflection() throws IOException {
        Path guardPath = Paths.get("src/main/java/net/vanillaoutsider/truesleep/util/" + "ModVersion" + "Guard.java");
        assertFalse(Files.exists(guardPath), "Obsolete " + "ModVersion" + "Guard.java must not exist");

        Path entrypointPath = Paths.get("src/main/java/net/vanillaoutsider/truesleep/TrueSleep.java");
        assertTrue(Files.exists(entrypointPath), "TrueSleep.java entrypoint must exist");
        String entrypointContent = Files.readString(entrypointPath, StandardCharsets.UTF_8);
        assertFalse(entrypointContent.contains("ModVersion" + "Guard"),
                "TrueSleep.java must not reference " + "ModVersion" + "Guard");
        assertFalse(entrypointContent.contains("Class.forName"),
                "TrueSleep.java must not invoke unremapped Class.forName");
    }

    @Test
    @DisplayName("Pillar 5: Mixin Configurations and Manifest Separate Client Mixins Cleanly")
    void testMixinAndManifestStructure() throws IOException {
        Path mixinConfigPath = Paths.get("src/main/resources/truesleep.mixins.json");
        Path clientMixinConfigPath = Paths.get("src/main/resources/truesleep.client.mixins.json");
        Path fabricModPath = Paths.get("src/main/resources/fabric.mod.json");

        assertTrue(Files.exists(mixinConfigPath), "truesleep.mixins.json must exist");
        assertTrue(Files.exists(clientMixinConfigPath), "truesleep.client.mixins.json must exist");
        assertTrue(Files.exists(fabricModPath), "fabric.mod.json must exist");

        String mixinJson = Files.readString(mixinConfigPath, StandardCharsets.UTF_8);
        String clientMixinJson = Files.readString(clientMixinConfigPath, StandardCharsets.UTF_8);
        String fabricJson = Files.readString(fabricModPath, StandardCharsets.UTF_8);

        // Mixin packages
        assertTrue(mixinJson.contains("\"package\": \"net.vanillaoutsider.truesleep.mixin\""));
        assertTrue(clientMixinJson.contains("\"package\": \"net.vanillaoutsider.truesleep.mixin.client\""));

        // Common mixin config must NOT list client mixins
        assertFalse(mixinJson.contains("GuiMixin"), "GuiMixin must be in client mixin config, not common");
        assertFalse(mixinJson.contains("HudMixin"), "HudMixin must be in client mixin config, not common");
        assertFalse(mixinJson.contains("SkyRendererMixin"), "SkyRendererMixin must be in client mixin config, not common");

        // Client mixin config must list client mixins
        assertTrue(clientMixinJson.contains("GuiMixin") || clientMixinJson.contains("HudMixin"), "truesleep.client.mixins.json must contain GuiMixin or HudMixin");
        assertTrue(clientMixinJson.contains("SkyRendererMixin"), "truesleep.client.mixins.json must contain SkyRendererMixin");

        // fabric.mod.json environment separation
        assertTrue(fabricJson.contains("\"truesleep.mixins.json\""));
        assertTrue(fabricJson.contains("\"config\": \"truesleep.client.mixins.json\""));
        assertTrue(fabricJson.contains("\"environment\": \"client\""));
        assertTrue(fabricJson.contains("net.vanillaoutsider.truesleep.client.config.ModMenuIntegration"));
    }
}
