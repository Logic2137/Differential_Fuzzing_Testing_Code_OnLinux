

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;




public class TestEnabled {

    public static void main(String... args) {
        boolean expected = Boolean.getBoolean("expected");
        boolean actual = isEnabled();
        if (expected != actual) {
            throw new IllegalStateException("Error: expected = " + expected + ", actual = " + actual);
        }
    }

    public static boolean isEnabled() {
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if (bean.getName().contains("Shenandoah")) {
                return true;
            }
        }
        return false;
    }

}
