import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SystemLoggerInPlatformLoader {

    static final class PlatformClassLoaderChild extends ClassLoader {

        private PlatformClassLoaderChild() {
            super(ClassLoader.getPlatformClassLoader());
        }

        public Class<?> definePlatformClass(String name) throws IOException {
            String testClasses = System.getProperty("test.classes", "./build/classes");
            String fname = name.replace('.', '/').concat(".class");
            try (InputStream is = new FileInputStream(new File(testClasses, fname))) {
                byte[] b = is.readAllBytes();
                ClassLoader parent = getParent();
                try {
                    Method m = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
                    m.setAccessible(true);
                    return (Class<?>) m.invoke(parent, name, b, 0, b.length);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                    throw new IOException(ex);
                }
            }
        }

        static final PlatformClassLoaderChild INSTANCE = new PlatformClassLoaderChild();

        static Class<?> loadLoggerAccessor() throws IOException {
            return INSTANCE.definePlatformClass("systempkg.log.SystemLoggerAccessor");
        }
    }

    static final Class<?> LOGGER_ACCESSOR_CLASS;

    static {
        try {
            LOGGER_ACCESSOR_CLASS = PlatformClassLoaderChild.loadLoggerAccessor();
            ClassLoader platformCL = ClassLoader.getPlatformClassLoader();
            if (LOGGER_ACCESSOR_CLASS.getClassLoader() != platformCL) {
                throw new ExceptionInInitializerError("Could not load accessor class in platform class loader: " + LOGGER_ACCESSOR_CLASS.getClassLoader());
            }
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    static System.Logger getSystemLogger(String name) {
        try {
            return (System.Logger) LOGGER_ACCESSOR_CLASS.getMethod("getSystemLogger", String.class).invoke(null, name);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException("Failed to invoke LoggerAccessor.getJULLogger", ex);
        }
    }

    public static void main(String[] args) {
        System.Logger platformLogger = getSystemLogger("bar");
        System.Logger appLogger = System.getLogger("bar");
        if (appLogger == platformLogger) {
            throw new RuntimeException("Same loggers");
        }
        Class<?> platformLoggerType = platformLogger.getClass();
        System.out.println("platformLogger: " + platformLoggerType);
        boolean simpleConsoleOnly = !ModuleLayer.boot().findModule("java.logging").isPresent();
        if (simpleConsoleOnly) {
            if (!platformLoggerType.getSimpleName().equals("SimpleConsoleLogger")) {
                throw new RuntimeException(platformLoggerType.getSimpleName() + ": unexpected class for platform logger" + " (expected a simple console logger class)");
            }
        } else {
            if (!platformLoggerType.getSimpleName().equals("JdkLazyLogger")) {
                throw new RuntimeException(platformLoggerType.getSimpleName() + ": unexpected class for platform logger" + " (expected a lazy logger for a platform class)");
            }
            Class<?> appLoggerType = appLogger.getClass();
            System.out.println("appLogger: " + appLoggerType);
            if (appLoggerType.equals(platformLoggerType)) {
                throw new RuntimeException(appLoggerType + ": unexpected class for application logger" + " (a lazy logger was not expected" + " for a non platform class)");
            }
        }
    }
}
