
package org.netbeans.jemmy.util;

public class Platform {

    private static final String osName = System.getProperty("os.name");

    public static boolean isLinux() {
        return isOs("linux");
    }

    public static boolean isOSX() {
        return isOs("mac");
    }

    public static boolean isSolaris() {
        return isOs("sunos");
    }

    public static boolean isWindows() {
        return isOs("win");
    }

    private static boolean isOs(String osname) {
        return osName.toLowerCase().startsWith(osname.toLowerCase());
    }
}
