
package pkg.b.t;

import java.lang.reflect.Method;
import java.lang.System.Logger;
import java.util.ResourceBundle;
import java.util.ListResourceBundle;

public class TestB {

    public static void main(String[] args) throws Exception {
        assertTrue(args.length == 2);
        String loggerMode = args[0];
        String loggerClassName = args[1];
        testLogger(loggerMode, loggerClassName);
        testLog(loggerClassName);
    }

    private static void testLogger(String loggerMode, String loggerClassName) {
        final Module m = TestB.class.getModule();
        final ClassLoader moduleCL = m.getClassLoader();
        assertTrue(!m.isNamed());
        assertTrue(moduleCL != null);
        String name = "test.b";
        Logger logger = getLogger(name);
        printLogger(logger);
        final Module lm = logger.getClass().getModule();
        final ClassLoader loggerCL = lm.getClassLoader();
        if (loggerMode.equals("system")) {
            assertTrue(lm.isNamed());
            assertTrue(loggerCL == null);
        } else if (loggerMode.equals("named")) {
            assertTrue(lm.isNamed());
            assertTrue(loggerCL != null);
        } else if (loggerMode.equals("unnamed")) {
            assertTrue(!lm.isNamed());
            assertTrue(loggerCL != null);
        } else {
            throw new RuntimeException("wrong parameter");
        }
        assertTrue(loggerClassName.equals(logger.getClass().getName()));
        assertTrue(!loggerClassName.equals("jdk.internal.logger.LazyLoggers$JdkLazyLogger"));
    }

    private static void testLog(String loggerClassName) throws Exception {
        if (loggerClassName.equals("pkg.a.l.LoggerA") || loggerClassName.equals("pkg.b.l.LoggerB")) {
            String name = "test.b.B";
            String plainMsg = "this is test log message #1";
            ResourceBundle rb = new MyResourcesB();
            Throwable ex = new Throwable("this is an expected exception to be logged");
            Class<?> clazz = Class.forName(loggerClassName);
            Method method = clazz.getMethod("checkLog", String.class, System.Logger.Level.class, ResourceBundle.class, String.class, Throwable.class, Object[].class);
            Logger logger = getLogger(name);
            printLogger(logger);
            assertTrue(logger.getClass().getName().equals(loggerClassName));
            assertTrue(logger.getName().equals(loggerClassName));
            logger.log(Logger.Level.WARNING, plainMsg);
            boolean pass = (boolean) method.invoke(null, name, Logger.Level.WARNING, null, plainMsg, ex, (Object) null);
            assertTrue(pass);
            pass = (boolean) method.invoke(null, name, Logger.Level.INFO, rb, MyResourcesB.VALUE, (Throwable) null, (Object) null);
            assertTrue(!pass);
            logger = getLogger(name, rb);
            printLogger(logger);
            assertTrue(logger.getClass().getName().equals("jdk.internal.logger.LocalizedLoggerWrapper"));
            assertTrue(logger.getName().equals(loggerClassName));
            logger.log(Logger.Level.INFO, MyResourcesB.KEY);
            pass = (boolean) method.invoke(null, name, Logger.Level.INFO, rb, MyResourcesB.VALUE, (Throwable) null, (Object) null);
            assertTrue(pass);
            pass = (boolean) method.invoke(null, name, Logger.Level.WARNING, null, plainMsg, ex, (Object) null);
            assertTrue(pass);
        }
    }

    private static class MyResourcesB extends ListResourceBundle {

        static final String KEY = "this is the key in MyResourcesB";

        static final String VALUE = "THIS IS THE VALUE IN MyResourcesB";

        @Override
        protected Object[][] getContents() {
            return new Object[][] { { KEY, VALUE } };
        }
    }

    private static Logger getLogger(String name) {
        return System.getLogger(name);
    }

    private static Logger getLogger(String name, ResourceBundle rb) {
        return System.getLogger(name, rb);
    }

    private static void printLogger(Logger logger) {
        System.err.println("logger name: " + logger.getName() + ", logger class: " + logger.getClass());
    }

    private static void assertTrue(boolean b) {
        if (!b) {
            throw new RuntimeException("expected true, but get false.");
        }
    }
}
