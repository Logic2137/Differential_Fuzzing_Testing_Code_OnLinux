
package vm.share;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;

import java.lang.management.ManagementFactory;
import java.util.Objects;

public class VMRuntimeEnvUtils {
    private static HotSpotDiagnosticMXBean DIAGNOSTIC_BEAN
            = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);

    private VMRuntimeEnvUtils() {
    }

    public static boolean isJITEnabled() {
        boolean isJITEnabled = ManagementFactory.getCompilationMXBean() != null;

        return isJITEnabled;
    }

    
    public static String getVMOption(String name) {
        Objects.requireNonNull(name);
        VMOption tmp;
        try {
            tmp = DIAGNOSTIC_BEAN.getVMOption(name);
        } catch (IllegalArgumentException e) {
            tmp = null;
        }
        return (tmp == null ? null : tmp.getValue());
    }

    
    public static String getVMOption(String name, String defaultValue) {
        String result = getVMOption(name);
        return result == null ? defaultValue : result;
    }

    
    public static boolean isVMOptionEnabled(String name) {
        String isSet = getVMOption(name, "error");
        if (isSet.equals("true")) {
            return true;
        } else if (isSet.equals("false")) {
            return false;
        }
        throw new IllegalArgumentException(name + " is not a boolean option.");
    }

    
    public static void setVMOption(String name, String value) {
        Objects.requireNonNull(name);
        DIAGNOSTIC_BEAN.setVMOption(name, value);
    }
}
