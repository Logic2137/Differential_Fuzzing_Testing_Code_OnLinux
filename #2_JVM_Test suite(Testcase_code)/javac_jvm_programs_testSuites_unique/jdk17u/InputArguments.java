
package jdk.test.lib.management;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class InputArguments {

    public static String[] getVmInputArgs() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        List<String> args = runtime.getInputArguments();
        return args.toArray(new String[args.size()]);
    }
}
