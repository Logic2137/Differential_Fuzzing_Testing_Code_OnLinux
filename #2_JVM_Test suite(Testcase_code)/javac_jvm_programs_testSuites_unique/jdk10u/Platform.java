

package jdk.test.lib;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

public class Platform {
    public  static final String vmName      = System.getProperty("java.vm.name");
    public  static final String vmInfo      = System.getProperty("java.vm.info");
    private static final String osVersion   = System.getProperty("os.version");
    private static       int osVersionMajor = -1;
    private static       int osVersionMinor = -1;
    private static final String osName      = System.getProperty("os.name");
    private static final String dataModel   = System.getProperty("sun.arch.data.model");
    private static final String vmVersion   = System.getProperty("java.vm.version");
    private static final String jdkDebug    = System.getProperty("jdk.debug");
    private static final String osArch      = System.getProperty("os.arch");
    private static final String userName    = System.getProperty("user.name");
    private static final String compiler    = System.getProperty("sun.management.compiler");

    public static boolean isClient() {
        return vmName.endsWith(" Client VM");
    }

    public static boolean isServer() {
        return vmName.endsWith(" Server VM");
    }

    public static boolean isGraal() {
        return vmName.endsWith(" Graal VM");
    }

    public static boolean isZero() {
        return vmName.endsWith(" Zero VM");
    }

    public static boolean isMinimal() {
        return vmName.endsWith(" Minimal VM");
    }

    public static boolean isEmbedded() {
        return vmName.contains("Embedded");
    }

    public static boolean isEmulatedClient() {
        return vmInfo.contains(" emulated-client");
    }

    public static boolean isTieredSupported() {
        return compiler.contains("Tiered Compilers");
    }

    public static boolean isInt() {
        return vmInfo.contains("interpreted");
    }

    public static boolean isMixed() {
        return vmInfo.contains("mixed");
    }

    public static boolean isComp() {
        return vmInfo.contains("compiled");
    }

    public static boolean is32bit() {
        return dataModel.equals("32");
    }

    public static boolean is64bit() {
        return dataModel.equals("64");
    }

    public static boolean isAix() {
        return isOs("aix");
    }

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

    public static String getOsName() {
        return osName;
    }

    
    private static void init_version() {
        try {
            final String[] tokens = osVersion.split("\\.");
            if (tokens.length > 0) {
                osVersionMajor = Integer.parseInt(tokens[0]);
                if (tokens.length > 1) {
                    osVersionMinor = Integer.parseInt(tokens[1]);
                }
            }
        } catch (NumberFormatException e) {
            osVersionMajor = osVersionMinor = 0;
        }
    }

    
    
    public static int getOsVersionMajor() {
        if (osVersionMajor == -1) init_version();
        return osVersionMajor;
    }

    
    
    public static int getOsVersionMinor() {
        if (osVersionMinor == -1) init_version();
        return osVersionMinor;
    }

    public static boolean isDebugBuild() {
        return (jdkDebug.toLowerCase().contains("debug"));
    }

    public static boolean isSlowDebugBuild() {
        return (jdkDebug.toLowerCase().equals("slowdebug"));
    }

    public static boolean isFastDebugBuild() {
        return (jdkDebug.toLowerCase().equals("fastdebug"));
    }

    public static String getVMVersion() {
        return vmVersion;
    }

    public static boolean isAArch64() {
        return isArch("aarch64");
    }

    public static boolean isARM() {
        return isArch("arm.*");
    }

    public static boolean isPPC() {
        return isArch("ppc.*");
    }

    
    public static boolean isS390x() {
        return isArch("s390.*") || isArch("s/390.*") || isArch("zArch_64");
    }

    
    public static boolean isSparc() {
        return isArch("sparc.*");
    }

    public static boolean isX64() {
        
        return isArch("(amd64)|(x86_64)");
    }

    public static boolean isX86() {
        
        return isArch("(i386)|(x86(?!_64))");
    }

    public static String getOsArch() {
        return osArch;
    }

    
    public static boolean shouldSAAttach() throws IOException {
        if (isAix()) {
            return false; 
        } else if (isLinux()) {
            if (isS390x()) {
                return false; 
            }
            return canPtraceAttachLinux();
        } else if (isOSX()) {
            return canAttachOSX();
        } else {
            
            return true;
        }
    }

    
    private static boolean canPtraceAttachLinux() throws IOException {
        
        File deny_ptrace = new File("/sys/fs/selinux/booleans/deny_ptrace");
        if (deny_ptrace.exists()) {
            try (RandomAccessFile file = new RandomAccessFile(deny_ptrace, "r")) {
                if (file.readByte() != '0') {
                    return false;
                }
            }
        }

        
        
        
        
        
        File ptrace_scope = new File("/proc/sys/kernel/yama/ptrace_scope");
        if (ptrace_scope.exists()) {
            try (RandomAccessFile file = new RandomAccessFile(ptrace_scope, "r")) {
                byte yama_scope = file.readByte();
                if (yama_scope == '3') {
                    return false;
                }

                if (!userName.equals("root") && yama_scope != '0') {
                    return false;
                }
            }
        }
        
        return true;
    }

    
    private static boolean canAttachOSX() {
        return userName.equals("root");
    }

    private static boolean isArch(String archnameRE) {
        return Pattern.compile(archnameRE, Pattern.CASE_INSENSITIVE)
                      .matcher(osArch)
                      .matches();
    }

    
    public static String sharedLibraryExt() {
        if (isWindows()) {
            return "dll";
        } else if (isOSX()) {
            return "dylib";
        } else {
            return "so";
        }
    }

    
    public static boolean areCustomLoadersSupportedForCDS() {
        boolean isLinux = Platform.isLinux();
        boolean is64 = Platform.is64bit();
        boolean isSolaris = Platform.isSolaris();
        boolean isAix = Platform.isAix();

        return (is64 && (isLinux || isSolaris || isAix));
    }
}
