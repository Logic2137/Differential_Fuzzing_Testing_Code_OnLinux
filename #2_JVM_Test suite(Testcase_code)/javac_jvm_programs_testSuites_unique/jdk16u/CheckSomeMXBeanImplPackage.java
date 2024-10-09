

import java.lang.management.ManagementFactory;
import java.lang.management.PlatformManagedObject;


public class CheckSomeMXBeanImplPackage {
    private static String implPackageName = "com.sun.management.internal";

    public static void main(String[] args) throws Exception {
        boolean present = false;
        try {
            Class.forName("com.sun.management.GarbageCollectorMXBean");
            present = true;
        } catch (ClassNotFoundException cnfe) {}

        if (present) {
            Class <? extends PlatformManagedObject> klazz =
                    java.lang.management.GarbageCollectorMXBean.class;
            for (Object obj :
                    ManagementFactory.getPlatformMXBeans(klazz)) {
                check(klazz.getName(), obj);
            }

            klazz = com.sun.management.GarbageCollectorMXBean.class;
            for (Object obj :
                    ManagementFactory.getPlatformMXBeans(klazz)) {
                check(klazz.getName(), obj);
            }

            klazz = java.lang.management.ThreadMXBean.class;
            check(klazz.getName(),
                    ManagementFactory.getPlatformMXBean(klazz));

            klazz = com.sun.management.ThreadMXBean.class;
            check(klazz.getName(),
                    ManagementFactory.getPlatformMXBean(klazz));

            System.out.println("--- PASSED!");
        } else {
            System.out.println("--- Skip the test, jdk.management module is not present!");
        }
    }

    private static void check(String mbeanName, Object impl) {
        if (!impl.getClass().getName().startsWith(implPackageName)) {
            throw new RuntimeException(mbeanName+" implementation package "
                    + "should be: " + implPackageName
                    + ", but got: " + impl.getClass());
        } else {
            System.out.println("--- Good, "+mbeanName+" got right implementation: " + impl);
        }
    }
}
