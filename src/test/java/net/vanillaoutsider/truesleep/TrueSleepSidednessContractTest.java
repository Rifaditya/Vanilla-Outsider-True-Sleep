// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TrueSleepSidednessContractTest: Pillar 5 Sidedness, Reflection Hygiene & License Contract Test Suite.
 *
 * Verifies across all Minecraft version anchors:
 * - Exact single-line GPLv3 license header compliance on line 1 across all Java source and test files.
 * - Zero references to client-only packages (net.minecraft.client, com.mojang.blaze3d) in common/server sources.
 * - Anti-Unmapped Reflection Law enforcement (zero raw Class.forName calls in production sources).
 */
public class TrueSleepSidednessContractTest {

    private static final String EXPECTED_LICENSE_HEADER = "// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3";

    // Testing Policy: Search tokens constructed dynamically from byte arrays prevent compiler constant-pool inlining
    private static final String TOKEN_CLIENT = new String(new byte[]{
            110, 101, 116, 46, 109, 105, 110, 101, 99, 114, 97, 102, 116, 46, 99, 108, 105, 101, 110, 116
    }, StandardCharsets.UTF_8); // "net.minecraft.client"

    private static final String TOKEN_BLAZE3D = new String(new byte[]{
            99, 111, 109, 46, 109, 111, 106, 97, 110, 103, 46, 98, 108, 97, 122, 101, 51, 100
    }, StandardCharsets.UTF_8); // "com.mojang.blaze3d"

    private static final String FORBIDDEN_REFLECTION_TOKEN = new String(new byte[]{
            67, 108, 97, 115, 115, 46, 102, 111, 114, 78, 97, 109, 101
    }, StandardCharsets.UTF_8); // "Class.forName"

    private static Path resolveRoot() {
        Path userDir = Paths.get(System.getProperty("user.dir", "."));
        if (Files.exists(userDir.resolve("src/main/java/net/vanillaoutsider/truesleep/TrueSleep.java"))) {
            return userDir;
        }
        Path current = userDir;
        while (current != null) {
            if (Files.exists(current.resolve("src/main/java/net/vanillaoutsider/truesleep/TrueSleep.java"))) {
                return current;
            }
            current = current.getParent();
        }
        try (Stream<Path> stream = Files.walk(userDir, 4)) {
            Optional<Path> found = stream.filter(p -> Files.exists(p.resolve("src/main/java/net/vanillaoutsider/truesleep/TrueSleep.java"))).findFirst();
            if (found.isPresent()) {
                return found.get();
            }
        } catch (Exception ignored) {
        }
        return userDir;
    }

    private Path resolvePath(String relative) {
        Path root = resolveRoot();
        Path direct = root.resolve(relative);
        if (Files.exists(direct)) {
            return direct;
        }
        Path path = Paths.get(relative);
        if (Files.exists(path)) {
            return path;
        }
        return direct;
    }

    @Test
    @DisplayName("Pillar 5: Exact Single-Line GPLv3 License Header Present On Line 1 Across All Sources")
    void testLicenseHeaders() throws IOException {
        Path[] roots = new Path[]{
                resolvePath("src/main/java"),
                resolvePath("src/test/java")
        };

        for (Path root : roots) {
            if (!Files.exists(root)) {
                continue;
            }
            try (Stream<Path> stream = Files.walk(root)) {
                List<Path> javaFiles = stream.filter(p -> p.toString().endsWith(".java")).toList();
                assertFalse(javaFiles.isEmpty(), "Java files must exist in: " + root);

                for (Path file : javaFiles) {
                    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                        String firstLine = reader.readLine();
                        assertNotNull(firstLine, "Java source file must not be empty: " + file);
                        assertTrue(firstLine.trim().startsWith(EXPECTED_LICENSE_HEADER),
                                "Missing or invalid GPLv3 license header on line 1 in: " + file.toString().replace('\\', '/'));
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Pillar 5: Common Non-Client Sources Zero Client or Blaze3D Imports")
    void testCommonSourceZeroClientImports() throws IOException {
        Path srcMain = resolvePath("src/main/java");
        assertTrue(Files.exists(srcMain), "src/main/java must exist at: " + srcMain.toAbsolutePath());

        List<String> violations = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(srcMain)) {
            List<Path> javaFiles = stream.filter(p -> p.toString().endsWith(".java")).toList();
            assertFalse(javaFiles.isEmpty(), "Must find Java source files in src/main/java");

            for (Path file : javaFiles) {
                String normalizedPath = file.toString().replace('\\', '/').toLowerCase();
                if (normalizedPath.contains("/client/")) {
                    continue;
                }

                List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i).trim();
                    if (line.startsWith("import ")) {
                        if (line.contains(TOKEN_CLIENT)) {
                            violations.add(String.format("%s:%d -> %s (forbidden: %s)",
                                    file.getFileName(), i + 1, line, TOKEN_CLIENT));
                        }
                        if (line.contains(TOKEN_BLAZE3D)) {
                            violations.add(String.format("%s:%d -> %s (forbidden: %s)",
                                    file.getFileName(), i + 1, line, TOKEN_BLAZE3D));
                        }
                    }
                }
            }
        }

        assertTrue(violations.isEmpty(),
                "Forbidden client-side imports found in common sources:\n" + String.join("\n", violations));
    }

    @Test
    @DisplayName("Pillar 5: Anti-Unmapped Reflection Law Enforcement (Zero Unsafe Class.forName)")
    void testZeroUnsafeReflection() throws IOException {
        Path srcMain = resolvePath("src/main/java");
        assertTrue(Files.exists(srcMain), "src/main/java must exist at: " + srcMain.toAbsolutePath());

        List<String> violations = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(srcMain)) {
            List<Path> javaFiles = stream.filter(p -> p.toString().endsWith(".java")).toList();
            assertFalse(javaFiles.isEmpty(), "Must find Java source files in src/main/java");

            for (Path file : javaFiles) {
                List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    if (line.contains(FORBIDDEN_REFLECTION_TOKEN)) {
                        violations.add(String.format("%s:%d -> %s",
                                file.getFileName(), i + 1, line.trim()));
                    }
                }
            }
        }

        assertTrue(violations.isEmpty(),
                "Forbidden Class.forName invocations found in production sources:\n" + String.join("\n", violations));
    }
}
