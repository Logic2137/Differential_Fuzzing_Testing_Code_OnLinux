

import java.lang.System.Logger;
import java.util.ResourceBundle;


public final class BootUsage {

    public static Logger getLogger(String name) {
        check();
        return System.getLogger(name);
    }

    public static Logger getLogger(String name, ResourceBundle rb) {
        check();
        return System.getLogger(name, rb);
    }

    private static void check() {
        final Module m = BootUsage.class.getModule();
        final ClassLoader moduleCL = m.getClassLoader();
        assertTrue(!m.isNamed());
        assertTrue(moduleCL == null);
    }

    private static void assertTrue(boolean b) {
        if (!b) {
            throw new RuntimeException("expected true, but get false.");
        }
    }
}
