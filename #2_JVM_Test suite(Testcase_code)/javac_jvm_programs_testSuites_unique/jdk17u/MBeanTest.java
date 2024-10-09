import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class MBeanTest {

    private static interface PrivateMBean {

        public int[] getInts();
    }

    public static class Private implements PrivateMBean {

        public int[] getInts() {
            return new int[] { 1, 2, 3 };
        }
    }

    public static interface NonCompliantMBean {

        public boolean getInt();

        public boolean isInt();

        public void setInt(int a);

        public void setInt(long b);
    }

    public static class NonCompliant implements NonCompliantMBean {

        public boolean getInt() {
            return false;
        }

        public boolean isInt() {
            return true;
        }

        public void setInt(int a) {
        }

        public void setInt(long b) {
        }
    }

    public static interface CompliantMBean {

        public boolean isFlag();

        public int getInt();

        public void setInt(int value);
    }

    public static class Compliant implements CompliantMBean {

        public boolean isFlag() {
            return false;
        }

        public int getInt() {
            return 1;
        }

        public void setInt(int value) {
        }
    }

    private static int failures = 0;

    public static void main(String[] args) throws Exception {
        testCompliant(CompliantMBean.class, new Compliant());
        testNonCompliant(PrivateMBean.class, new Private());
        testNonCompliant(NonCompliantMBean.class, new NonCompliant());
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

    private static void testNonCompliant(Class<?> iface, Object bean) throws Exception {
        try {
            System.out.println("Registering a non-compliant MBean " + iface.getName() + " ...");
            MBeanServer mbs = MBeanServerFactory.newMBeanServer();
            ObjectName on = new ObjectName("test:type=NonCompliant");
            mbs.registerMBean(bean, on);
            fail("Registered a non-compliant MBean - " + iface.getName());
        } catch (Exception e) {
            Throwable t = e;
            while (t != null && !(t instanceof NotCompliantMBeanException)) {
                t = t.getCause();
            }
            if (t != null) {
                success("MBean not registered");
            } else {
                throw e;
            }
        }
    }

    private static void testCompliant(Class<?> iface, Object bean) throws Exception {
        try {
            System.out.println("Registering a compliant MBean " + iface.getName() + " ...");
            MBeanServer mbs = MBeanServerFactory.newMBeanServer();
            ObjectName on = new ObjectName("test:type=Compliant");
            mbs.registerMBean(bean, on);
            success("Registered a compliant MBean - " + iface.getName());
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
