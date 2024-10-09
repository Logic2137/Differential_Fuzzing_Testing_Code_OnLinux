



import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class MXBeanFallbackTest {
    public static void main(String[] args) throws Exception {
        testPrivateMXBean("Private", new Private());

        if (failures == 0)
            System.out.println("Test passed");
        else
            throw new Exception("TEST FAILURES: " + failures);
    }

    private static int failures = 0;

    private static interface PrivateMXBean {
        public int[] getInts();
    }

    public static class Private implements PrivateMXBean {
        public int[] getInts() {
            return new int[]{1,2,3};
        }
    }

    private static void testPrivateMXBean(String type, Object bean) throws Exception {
        System.out.println(type + " MXBean test...");
        MBeanServer mbs = MBeanServerFactory.newMBeanServer();
        ObjectName on = new ObjectName("test:type=" + type);
        try {
            mbs.registerMBean(bean, on);
            success("Private MXBean registered");
        } catch (NotCompliantMBeanException e) {
            failure("Failed to register the private MXBean - " +
                     bean.getClass().getInterfaces()[0].getName());
        }
    }

    private static void success(String what) {
        System.out.println("OK: " + what);
    }

    private static void failure(String what) {
        System.out.println("FAILED: " + what);
        failures++;
    }
}
