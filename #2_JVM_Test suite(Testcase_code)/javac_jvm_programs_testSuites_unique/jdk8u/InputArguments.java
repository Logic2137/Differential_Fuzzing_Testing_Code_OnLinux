
package com.oracle.java.testlibrary;

import java.lang.management.RuntimeMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class InputArguments {

    private static final List<String> args;

    static {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        args = runtimeMxBean.getInputArguments();
    }

    public static boolean contains(String arg) {
        return args.contains(arg);
    }

    public static boolean containsPrefix(String prefix) {
        for (String arg : args) {
            if (arg.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
