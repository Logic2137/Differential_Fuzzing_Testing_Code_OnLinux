

package jdk.test.lib.management;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;


public final class ThreadMXBeanTool {

    
    public static void waitUntilBlockingOnObject(Thread thread, Thread.State state, Object object)
        throws InterruptedException {
        String want = object == null ? null : object.getClass().getName() + '@'
                + Integer.toHexString(System.identityHashCode(object));
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        while (thread.isAlive()) {
            ThreadInfo ti = tmx.getThreadInfo(thread.getId());
            if (ti.getThreadState() == state
                    && (want == null || want.equals(ti.getLockName()))) {
                return;
            }
            Thread.sleep(1);
        }
    }

    
    public static void waitUntilInNative(Thread thread) throws InterruptedException {
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        while (thread.isAlive()) {
            ThreadInfo ti = tmx.getThreadInfo(thread.getId());
            if (ti.isInNative()) {
                return;
            }
            Thread.sleep(1);
        }
    }

}
