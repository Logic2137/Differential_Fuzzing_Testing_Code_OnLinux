import java.lang.management.*;
import javax.management.*;
import static java.lang.management.ManagementFactory.*;

public class ThreadInfoArray {

    public static void main(String[] argv) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName on = new ObjectName(THREAD_MXBEAN_NAME);
        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        ThreadMXBean proxy = newPlatformMXBeanProxy(mbs, on.toString(), ThreadMXBean.class);
        checkNullElement(mbean, proxy, mbs, on);
        checkEmptyArray(mbean, proxy, mbs, on);
        System.out.println("Test passed");
    }

    private static void checkNullElement(ThreadMXBean mbean, ThreadMXBean proxy, MBeanServer mbs, ObjectName on) throws Exception {
        System.out.println("--- Check null element");
        long[] ids = { new Thread().getId() };
        ThreadInfo[] tinfos = mbean.getThreadInfo(ids);
        if (tinfos[0] != null) {
            throw new RuntimeException("TEST FAILED: " + "Expected to have a null element");
        }
        Object[] params = { ids };
        String[] sigs = { "[J" };
        Object[] result = (Object[]) mbs.invoke(on, "getThreadInfo", params, sigs);
        if (result[0] != null) {
            throw new RuntimeException("TEST FAILED: " + "Expected to have a null element via MBeanServer");
        }
        tinfos = proxy.getThreadInfo(ids);
        if (tinfos[0] != null) {
            throw new RuntimeException("TEST FAILED: " + "Expected to have a null element");
        }
        System.out.println("--- PASSED");
    }

    private static void checkEmptyArray(ThreadMXBean mbean, ThreadMXBean proxy, MBeanServer mbs, ObjectName on) throws Exception {
        System.out.println("--- Check empty TID array");
        long[] ids = new long[0];
        assertEmptyArray(mbean.getThreadInfo(ids), "Expected empty ThreadInfo array");
        assertEmptyArray(mbean.getThreadInfo(ids, 1), "Expected empty ThreadInfo array");
        assertEmptyArray(mbean.getThreadInfo(ids, true, true), "Expected empty ThreadInfo array");
        assertEmptyArray((Object[]) mbs.invoke(on, "getThreadInfo", new Object[] { ids }, new String[] { "[J" }), "Expected empty ThreadInfo array via MBeanServer");
        assertEmptyArray((Object[]) mbs.invoke(on, "getThreadInfo", new Object[] { ids, 1 }, new String[] { "[J", "int" }), "Expected empty ThreadInfo array via MBeanServer");
        assertEmptyArray((Object[]) mbs.invoke(on, "getThreadInfo", new Object[] { ids, true, true }, new String[] { "[J", "boolean", "boolean" }), "Expected empty ThreadInfo array via MBeanServer");
        assertEmptyArray(proxy.getThreadInfo(ids), "Expected empty ThreadInfo array");
        assertEmptyArray(proxy.getThreadInfo(ids, 1), "Expected empty ThreadInfo array");
        assertEmptyArray(proxy.getThreadInfo(ids, true, true), "Expected empty ThreadInfo array");
        System.out.println("--- PASSED");
    }

    private static void assertEmptyArray(Object[] arr, String message) throws Exception {
        if (arr.length > 0) {
            throw new RuntimeException("TEST FAILED: " + message);
        }
    }
}
