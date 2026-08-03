// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.truesleep.util;

public final class ModVersionGuard {
    private ModVersionGuard() {}

    public static void checkClass(String modName, String requiredClassName) {
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader currentLoader = ModVersionGuard.class.getClassLoader();
        try {
            if (contextLoader != null) {
                Class.forName(requiredClassName, false, contextLoader);
            } else {
                Class.forName(requiredClassName, false, currentLoader);
            }
        } catch (ClassNotFoundException e) {
            try {
                Class.forName(requiredClassName, false, currentLoader);
            } catch (ClassNotFoundException e2) {
                throw new RuntimeException("\n" +
                    "=====================================================================\n" +
                    " [PRE-RELEASE / VERSION GUARD WARNING] " + modName + "\n" +
                    "---------------------------------------------------------------------\n" +
                    " CRITICAL: Incompatible Minecraft Game Runtime or Missing Class!\n" +
                    " Required Class : " + requiredClassName + "\n" +
                    " Status         : UNRESOLVED AT RUNTIME\n\n" +
                    " Safety Protection:\n" +
                    " Execution halted to prevent unreleased/incompatible build deployment\n" +
                    " or broken world state save corruption.\n\n" +
                    " Troubleshooting Steps:\n" +
                    " 1. Verify target Minecraft version (26.2+ release drop).\n" +
                    " 2. Ensure all required dependencies (Fabric API, DasikLibrary) are loaded.\n" +
                    " 3. Build/Download a verified matching release JAR from Modrinth/CurseForge.\n" +
                    "=====================================================================");
            }
        }
    }
}