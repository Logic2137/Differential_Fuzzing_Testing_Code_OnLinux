

import java.util.logging.*;



public class LogManagerInstanceTest {
    public static void main(String[] argv) {
        LogManager mgr = LogManager.getLogManager();
        if (getRootLogger(mgr) == null) {
            throw new RuntimeException("Root logger not exist");
        }

        SecondLogManager mgr2 = new SecondLogManager();
        Logger root = getRootLogger(mgr2);
        if (mgr2.base != root) {
            throw new RuntimeException(mgr2.base + " is not the root logger");
        }
    }

    private static Logger getRootLogger(LogManager mgr) {
        Logger l = mgr.getLogger("");
        if (l != null && !l.getName().isEmpty()) {
            throw new RuntimeException(l.getName() + " is not an invalid root logger");
        }
        return l;
    }

    static class SecondLogManager extends LogManager {
        final Logger base;
        private SecondLogManager() {
            Logger root = getLogger("");
            if (root == null) {
                root = new BaseLogger("", null);
                if (!super.addLogger(root))
                    throw new RuntimeException("Fail to addLogger " + root);
            } else {
                throw new RuntimeException("Root logger already exists");
            }
            this.base = root;
        }
    }
    static class BaseLogger extends Logger {
        BaseLogger(String name, String rbname) {
            super(name, rbname);
        }
    }
}
