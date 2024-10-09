

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;


public class MBeanFallbackTest {
    private static interface PrivateMBean {
        public int[] getInts();
    }

    public static class Private implements PrivateMBean {
        public int[] getInts() {
            return new int[]{1,2,3};
        }
    }

    private static int failures = 0;

    public static void main(String[] args) throws Exception {
        testPrivate(PrivateMBean.class, new Private());

        if (failures == 0)
            System.out.println("Test passed");
        else
            throw new Exception("TEST FAILURES: " + failures);
    }

    private static void fail(String msg) {
        failures++;
        System.out.println("FAIL: " + msg);
    }

    private static void success(String msg) {
        System.out.println("OK: " + msg);
    }

    private static void testPrivate(Class<?> iface, Object bean) throws Exception {
        try {
            System.out.println("Registering a private MBean " +
                                iface.getName() + " ...");

            MBeanServer mbs = MBeanServerFactory.newMBeanServer();
            ObjectName on = new ObjectName("test:type=Compliant");

            mbs.registerMBean(bean, on);
            success("Registered a private MBean - " + iface.getName());
        } catch (Exception e) {
            Throwable t = e;
            while (t != null && !(t instanceof NotCompliantMBeanException)) {
                t = t.getCause();
            }
            if (t != null) {
                fail("MBean not registered");
            } else {
                throw e;
            }
        }
    }
}
