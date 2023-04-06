package net.ninjaworks.generalapi;

import java.util.Locale;

/**
 * A simple OS enumerator (utility)
 */
public enum OS {
    WINDOWS,
    LINUX,
    MACOS;

    /**
     * Determines the system's os.
     * @return The determined os, null if it fails
     */
    public static OS determine() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String os = "";
        if(osName.contains("windows")) {os = "windows";}
        else if(osName.contains("linux")) {os = "linux";}
        else if(osName.contains("mac os")) {os = "macos";}
        else {System.out.println("Cannot determine OS type from '" + osName + "'!");}
        return fromString(os);
    }

    /**
     * Parses an os from a given string.
     * @param string The string to parse the os from
     * @return The parsed os, null if it fails
     */
    public static OS fromString(String string) {
        return switch (string) {
            case "windows" -> WINDOWS;
            case "linux" -> LINUX;
            case "macos" -> MACOS;
            case default -> null;
        };
    }
}