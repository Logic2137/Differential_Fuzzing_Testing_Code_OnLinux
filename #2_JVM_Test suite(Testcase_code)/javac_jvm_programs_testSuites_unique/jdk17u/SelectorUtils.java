import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.nio.channels.Selector;

public class SelectorUtils {

    public static boolean mightHoldLock(Thread t, Object lock) {
        long tid = t.getId();
        int hash = System.identityHashCode(lock);
        ThreadInfo ti = ManagementFactory.getThreadMXBean().getThreadInfo(new long[] { tid }, true, false, 100)[0];
        if (ti != null) {
            for (MonitorInfo mi : ti.getLockedMonitors()) {
                if (mi.getIdentityHashCode() == hash)
                    return true;
            }
        }
        return false;
    }

    public static void spinUntilLocked(Thread t, Selector sel) throws Exception {
        while (!mightHoldLock(t, sel.selectedKeys())) {
            Thread.sleep(50);
        }
    }
}
