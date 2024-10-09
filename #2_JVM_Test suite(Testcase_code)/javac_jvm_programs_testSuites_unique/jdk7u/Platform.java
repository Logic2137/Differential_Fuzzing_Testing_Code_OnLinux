

package com.oracle.java.testlibrary;

public class Platform {
    private static final String osName      = System.getProperty("os.name");
    private static final String dataModel   = System.getProperty("sun.arch.data.model");
    private static final String vmVersion   = System.getProperty("java.vm.version");
    private static final String osArch      = System.getProperty("os.arch");

    public static boolean is32bit() {
        return dataModel.equals("32");
    }

    public static boolean is64bit() {
        return dataModel.equals("64");
    }

    public static boolean isSolaris() {
        return isOs("sunos");
    }

    public static boolean isWindows() {
        return isOs("win");
    }

    public static boolean isOSX() {
        return isOs("mac");
    }

    public static boolean isLinux() {
        return isOs("linux");
    }

    private static boolean isOs(String osname) {
        return osName.toLowerCase().startsWith(osname.toLowerCase());
    }

    public static String getOsName() {
        return osName;
    }

    public static boolean isDebugBuild() {
        return vmVersion.toLowerCase().contains("debug");
    }

    public static String getVMVersion() {
        return vmVersion;
    }

    
    public static boolean isSparc() {
        return isArch("sparc");
    }

    public static boolean isARM() {
        return isArch("arm");
    }

    public static boolean isPPC() {
        return isArch("ppc");
    }

    public static boolean isX86() {
        
        return (isArch("i386") || isArch("x86"));
    }

    public static boolean isX64() {
        
        return (isArch("amd64") || isArch("x86_64"));
    }

    private static boolean isArch(String archname) {
        return osArch.toLowerCase().startsWith(archname.toLowerCase());
    }

    public static String getOsArch() {
        return osArch;
    }

}
