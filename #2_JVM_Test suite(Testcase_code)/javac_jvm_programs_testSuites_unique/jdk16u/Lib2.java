

package lib2;

import java.lang.management.ManagementFactory;

public class Lib2 {
    public static long getPid() {
        return ManagementFactory.getRuntimeMXBean().getPid();
    }
}
