
package com.oracle.java.testlibrary;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import java.lang.management.ManagementFactory;


public class DynamicVMOptionChecker {

    
    public static int getIntValue(String name) {

        VMOption option = ManagementFactory.
                getPlatformMXBean(HotSpotDiagnosticMXBean.class).
                getVMOption(name);

        return Integer.parseInt(option.getValue());
    }

    
    public static boolean checkIsWritable(String name) {
        VMOption option = ManagementFactory.
                getPlatformMXBean(HotSpotDiagnosticMXBean.class).
                getVMOption(name);

        if (!option.isWriteable()) {
            throw new RuntimeException(name + " is not writable");
        }

        return true;
    }

    
    public static void checkInvalidValue(String name, String value) {
        
        try {
            ManagementFactory.
                    getPlatformMXBean(HotSpotDiagnosticMXBean.class).
                    setVMOption(name, value);

        } catch (IllegalArgumentException e) {
            return;
        }

        throw new RuntimeException("Expected IllegalArgumentException was not thrown, " + name + "= " + value);
    }

    
    public static void checkValidValue(String name, String value) {
        ManagementFactory.
                getPlatformMXBean(HotSpotDiagnosticMXBean.class).
                setVMOption(name, value);

        VMOption option = ManagementFactory.
                getPlatformMXBean(HotSpotDiagnosticMXBean.class).
                getVMOption(name);

        if (!option.getValue().equals(value)) {
            throw new RuntimeException("Actual value of " + name + " \"" + option.getValue()
                    + "\" not equal origin \"" + value + "\"");
        }
    }

}
