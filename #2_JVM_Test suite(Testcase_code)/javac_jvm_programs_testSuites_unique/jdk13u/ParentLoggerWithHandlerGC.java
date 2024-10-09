import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;

public class ParentLoggerWithHandlerGC {

    public static enum TestCase {

        UNSECURE, SECURE;

        public void run(Properties propertyFile) throws Exception {
            System.out.println("Running test case: " + name());
            Configure.setUp(this, propertyFile);
            test(this.name() + " " + propertyFile.getProperty("test.name"), propertyFile);
        }
    }

    private static final String PREFIX = "FileHandler-" + UUID.randomUUID() + ".log";

    private static final String userDir = System.getProperty("user.dir", ".");

    private static final boolean userDirWritable = Files.isWritable(Paths.get(userDir));

    static enum ConfigMode {

        DEFAULT, ENSURE_CLOSE_ON_RESET_TRUE, ENSURE_CLOSE_ON_RESET_FALSE
    }

    private static final List<Properties> properties;

    static {
        Properties props1 = new Properties();
        props1.setProperty("test.name", "parent logger with handler");
        props1.setProperty("test.config.mode", ConfigMode.DEFAULT.name());
        props1.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props1.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Integer.MAX_VALUE));
        props1.setProperty(FileHandler.class.getName() + ".level", "ALL");
        props1.setProperty(FileHandler.class.getName() + ".formatter", "java.util.logging.SimpleFormatter");
        props1.setProperty("com.foo.handlers", FileHandler.class.getName());
        props1.setProperty("com.bar.level", "FINEST");
        Properties props2 = new Properties();
        props2.setProperty("test.name", "parent logger with handler");
        props2.setProperty("test.config.mode", ConfigMode.ENSURE_CLOSE_ON_RESET_TRUE.name());
        props2.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props2.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Integer.MAX_VALUE));
        props2.setProperty(FileHandler.class.getName() + ".level", "ALL");
        props2.setProperty(FileHandler.class.getName() + ".formatter", "java.util.logging.SimpleFormatter");
        props2.setProperty("com.foo.handlers", FileHandler.class.getName());
        props2.setProperty("com.foo.handlers.ensureCloseOnReset", "true");
        props2.setProperty("com.bar.level", "FINEST");
        Properties props3 = new Properties();
        props3.setProperty("test.name", "parent logger with handler");
        props3.setProperty("test.config.mode", ConfigMode.ENSURE_CLOSE_ON_RESET_FALSE.name());
        props3.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props3.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Integer.MAX_VALUE));
        props3.setProperty(FileHandler.class.getName() + ".level", "ALL");
        props3.setProperty(FileHandler.class.getName() + ".formatter", "java.util.logging.SimpleFormatter");
        props3.setProperty("com.foo.handlers", FileHandler.class.getName());
        props3.setProperty("com.foo.handlers.ensureCloseOnReset", "false");
        props3.setProperty("com.bar.level", "FINEST");
        properties = Collections.unmodifiableList(Arrays.asList(props1, props2, props3));
    }

    public static void main(String... args) throws Exception {
        if (args == null || args.length == 0) {
            args = new String[] { TestCase.UNSECURE.name(), TestCase.SECURE.name() };
        }
        try {
            for (String testName : args) {
                for (Properties propertyFile : properties) {
                    TestCase test = TestCase.valueOf(testName);
                    test.run(propertyFile);
                }
            }
        } finally {
            if (userDirWritable) {
                Configure.doPrivileged(() -> {
                    try {
                        Files.list(Paths.get(userDir)).filter((f) -> f.toString().contains(PREFIX)).forEach((f) -> {
                            try {
                                System.out.println("deleting " + f);
                                Files.delete(f);
                            } catch (Throwable t) {
                                System.err.println("Failed to delete " + f + ": " + t);
                            }
                        });
                    } catch (Throwable t) {
                        System.err.println("Cleanup failed to list files: " + t);
                        t.printStackTrace();
                    }
                });
            }
        }
    }

    static class Configure {

        static Policy policy = null;

        static final AtomicBoolean allowAll = new AtomicBoolean(false);

        static void setUp(TestCase test, Properties propertyFile) {
            switch(test) {
                case SECURE:
                    if (policy == null && System.getSecurityManager() != null) {
                        throw new IllegalStateException("SecurityManager already set");
                    } else if (policy == null) {
                        policy = new SimplePolicy(TestCase.SECURE, allowAll);
                        Policy.setPolicy(policy);
                        System.setSecurityManager(new SecurityManager());
                    }
                    if (System.getSecurityManager() == null) {
                        throw new IllegalStateException("No SecurityManager.");
                    }
                    if (policy == null) {
                        throw new IllegalStateException("policy not configured");
                    }
                    break;
                case UNSECURE:
                    if (System.getSecurityManager() != null) {
                        throw new IllegalStateException("SecurityManager already set");
                    }
                    break;
                default:
                    new InternalError("No such testcase: " + test);
            }
            doPrivileged(() -> {
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    propertyFile.store(bytes, propertyFile.getProperty("test.name"));
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes.toByteArray());
                    LogManager.getLogManager().readConfiguration(bais);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        static void doPrivileged(Runnable run) {
            allowAll.set(true);
            try {
                run.run();
            } finally {
                allowAll.set(false);
            }
        }

        static <T> T callPrivileged(Callable<T> call) throws Exception {
            allowAll.set(true);
            try {
                return call.call();
            } finally {
                allowAll.set(false);
            }
        }
    }

    @FunctionalInterface
    public static interface FileHandlerSupplier {

        public FileHandler test() throws Exception;
    }

    static final class TestAssertException extends RuntimeException {

        TestAssertException(String msg) {
            super(msg);
        }
    }

    private static void assertEquals(long expected, long received, String msg) {
        if (expected != received) {
            throw new TestAssertException("Unexpected result for " + msg + ".\n\texpected: " + expected + "\n\tactual:   " + received);
        } else {
            System.out.println("Got expected " + msg + ": " + received);
        }
    }

    public static void test(String name, Properties props) throws Exception {
        ConfigMode configMode = ConfigMode.valueOf(props.getProperty("test.config.mode"));
        System.out.println("\nTesting: " + name + " mode=" + configMode);
        if (!userDirWritable) {
            throw new RuntimeException("Not writable: " + userDir);
        }
        switch(configMode) {
            case DEFAULT:
            case ENSURE_CLOSE_ON_RESET_TRUE:
                testCloseOnResetTrue(name, props);
                break;
            case ENSURE_CLOSE_ON_RESET_FALSE:
                testCloseOnResetFalse(name, props);
                break;
            default:
                throw new RuntimeException("Unknwown mode: " + configMode);
        }
    }

    public static void testCloseOnResetTrue(String name, Properties props) throws Exception {
        Logger fooChild = Logger.getLogger("com.foo.child");
        fooChild.info("hello world");
        Logger barChild = Logger.getLogger("com.bar.child");
        barChild.info("hello world");
        ReferenceQueue<Logger> queue = new ReferenceQueue();
        WeakReference<Logger> fooRef = new WeakReference<>(Logger.getLogger("com.foo"), queue);
        if (fooRef.get() != fooChild.getParent()) {
            throw new RuntimeException("Unexpected parent logger: " + fooChild.getParent() + "\n\texpected: " + fooRef.get());
        }
        WeakReference<Logger> barRef = new WeakReference<>(Logger.getLogger("com.bar"), queue);
        if (barRef.get() != barChild.getParent()) {
            throw new RuntimeException("Unexpected parent logger: " + barChild.getParent() + "\n\texpected: " + barRef.get());
        }
        fooChild = barChild = null;
        Reference<? extends Logger> ref2 = null;
        while ((ref2 = queue.poll()) == null) {
            System.gc();
            Thread.sleep(1000);
        }
        Throwable failed = null;
        try {
            do {
                if (ref2 != barRef) {
                    throw new RuntimeException("Unexpected reference: " + ref2 + "\n\texpected: " + barRef);
                }
                if (ref2.get() != null) {
                    throw new RuntimeException("Referent not cleared: " + ref2.get());
                }
                System.out.println("Got barRef");
                System.gc();
                Thread.sleep(1000);
            } while ((ref2 = queue.poll()) != null);
            System.out.println("Parent logger GCed");
        } catch (Throwable t) {
            failed = t;
        } finally {
            final Throwable suppressed = failed;
            Configure.doPrivileged(() -> LogManager.getLogManager().reset());
            Configure.doPrivileged(() -> {
                try {
                    StringBuilder builder = new StringBuilder();
                    Files.list(Paths.get(userDir)).filter((f) -> f.toString().contains(PREFIX)).filter((f) -> f.toString().endsWith(".lck")).forEach((f) -> {
                        builder.append(f.toString()).append('\n');
                    });
                    if (!builder.toString().isEmpty()) {
                        throw new RuntimeException("Lock files not cleaned:\n" + builder.toString());
                    }
                } catch (RuntimeException | Error x) {
                    if (suppressed != null)
                        x.addSuppressed(suppressed);
                    throw x;
                } catch (Exception x) {
                    if (suppressed != null)
                        x.addSuppressed(suppressed);
                    throw new RuntimeException(x);
                }
            });
            while ((ref2 = queue.poll()) == null) {
                System.gc();
                Thread.sleep(1000);
            }
            if (ref2 != fooRef) {
                throw new RuntimeException("Unexpected reference: " + ref2 + "\n\texpected: " + fooRef);
            }
            if (ref2.get() != null) {
                throw new RuntimeException("Referent not cleared: " + ref2.get());
            }
            System.out.println("Got fooRef after reset()");
        }
        if (failed != null) {
            throw new RuntimeException(failed);
        }
    }

    private static Handler getHandlerToClose() throws Exception {
        return Configure.callPrivileged(() -> Logger.getLogger("com.foo.child").getParent().getHandlers()[0]);
    }

    public static void testCloseOnResetFalse(String name, Properties props) throws Exception {
        Logger fooChild = Logger.getLogger("com.foo.child");
        fooChild.info("hello world");
        Logger barChild = Logger.getLogger("com.bar.child");
        barChild.info("hello world");
        Handler toClose = getHandlerToClose();
        ReferenceQueue<Logger> queue = new ReferenceQueue();
        WeakReference<Logger> fooRef = new WeakReference<>(Logger.getLogger("com.foo"), queue);
        if (fooRef.get() != fooChild.getParent()) {
            throw new RuntimeException("Unexpected parent logger: " + fooChild.getParent() + "\n\texpected: " + fooRef.get());
        }
        WeakReference<Logger> barRef = new WeakReference<>(Logger.getLogger("com.bar"), queue);
        if (barRef.get() != barChild.getParent()) {
            throw new RuntimeException("Unexpected parent logger: " + barChild.getParent() + "\n\texpected: " + barRef.get());
        }
        fooChild = barChild = null;
        Reference<? extends Logger> ref2 = null;
        Set<WeakReference<Logger>> expectedRefs = new HashSet<>(Arrays.asList(fooRef, barRef));
        Throwable failed = null;
        try {
            int l = 0;
            while (failed == null && !expectedRefs.isEmpty()) {
                int max = 60;
                while ((ref2 = queue.poll()) == null) {
                    if (l > 0 && max-- <= 0) {
                        throw new RuntimeException("Logger #2 not GC'ed!" + " max too short (max=60) or " + "com.foo.handlers.ensureCloseOnReset=false" + " does not work");
                    }
                    System.gc();
                    Thread.sleep(1000);
                }
                do {
                    if (!expectedRefs.contains(ref2)) {
                        throw new RuntimeException("Unexpected reference: " + ref2 + "\n\texpected: " + expectedRefs);
                    }
                    if (ref2.get() != null) {
                        throw new RuntimeException("Referent not cleared: " + ref2.get());
                    }
                    expectedRefs.remove(ref2);
                    System.out.println("Got " + (ref2 == barRef ? "barRef" : (ref2 == fooRef ? "fooRef" : ref2.toString())));
                    System.gc();
                    Thread.sleep(1000);
                    System.out.println("Logger #" + (++l) + " GCed");
                } while ((ref2 = queue.poll()) != null);
            }
        } catch (Throwable t) {
            failed = t;
        } finally {
            final Throwable suppressed = failed;
            Configure.doPrivileged(() -> LogManager.getLogManager().reset());
            Configure.doPrivileged(() -> {
                try {
                    toClose.close();
                    StringBuilder builder = new StringBuilder();
                    Files.list(Paths.get(userDir)).filter((f) -> f.toString().contains(PREFIX)).filter((f) -> f.toString().endsWith(".lck")).forEach((f) -> {
                        builder.append(f.toString()).append('\n');
                    });
                    if (!builder.toString().isEmpty()) {
                        throw new RuntimeException("Lock files not cleaned:\n" + builder.toString());
                    }
                } catch (RuntimeException | Error x) {
                    if (suppressed != null)
                        x.addSuppressed(suppressed);
                    throw x;
                } catch (Exception x) {
                    if (suppressed != null)
                        x.addSuppressed(suppressed);
                    throw new RuntimeException(x);
                }
            });
        }
        if (failed != null) {
            throw new RuntimeException(failed);
        }
    }

    static final class PermissionsBuilder {

        final Permissions perms;

        public PermissionsBuilder() {
            this(new Permissions());
        }

        public PermissionsBuilder(Permissions perms) {
            this.perms = perms;
        }

        public PermissionsBuilder add(Permission p) {
            perms.add(p);
            return this;
        }

        public PermissionsBuilder addAll(PermissionCollection col) {
            if (col != null) {
                for (Enumeration<Permission> e = col.elements(); e.hasMoreElements(); ) {
                    perms.add(e.nextElement());
                }
            }
            return this;
        }

        public Permissions toPermissions() {
            final PermissionsBuilder builder = new PermissionsBuilder();
            builder.addAll(perms);
            return builder.perms;
        }
    }

    public static class SimplePolicy extends Policy {

        final Permissions permissions;

        final Permissions allPermissions;

        final AtomicBoolean allowAll;

        public SimplePolicy(TestCase test, AtomicBoolean allowAll) {
            this.allowAll = allowAll;
            permissions = new Permissions();
            permissions.add(new LoggingPermission("control", null));
            permissions.add(new FilePermission(PREFIX + ".lck", "read,write,delete"));
            permissions.add(new FilePermission(PREFIX, "read,write"));
            allPermissions = new Permissions();
            allPermissions.add(new java.security.AllPermission());
        }

        @Override
        public boolean implies(ProtectionDomain domain, Permission permission) {
            if (allowAll.get())
                return allPermissions.implies(permission);
            return permissions.implies(permission);
        }

        @Override
        public PermissionCollection getPermissions(CodeSource codesource) {
            return new PermissionsBuilder().addAll(allowAll.get() ? allPermissions : permissions).toPermissions();
        }

        @Override
        public PermissionCollection getPermissions(ProtectionDomain domain) {
            return new PermissionsBuilder().addAll(allowAll.get() ? allPermissions : permissions).toPermissions();
        }
    }
}
