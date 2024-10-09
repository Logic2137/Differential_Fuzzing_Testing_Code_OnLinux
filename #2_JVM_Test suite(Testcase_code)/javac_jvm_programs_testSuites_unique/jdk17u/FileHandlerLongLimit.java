import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
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
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.LoggingPermission;

public class FileHandlerLongLimit {

    public static enum TestCase {

        UNSECURE, SECURE;

        public void run(Properties propertyFile) throws Exception {
            System.out.println("Running test case: " + name());
            Configure.setUp(this, propertyFile);
            test(this.name() + " " + propertyFile.getProperty("test.name"), propertyFile, Long.parseLong(propertyFile.getProperty(FileHandler.class.getName() + ".limit")));
        }
    }

    private static final String PREFIX = "FileHandler-" + UUID.randomUUID() + ".log";

    private static final String userDir = System.getProperty("user.dir", ".");

    private static final boolean userDirWritable = Files.isWritable(Paths.get(userDir));

    private static final Field limitField;

    private static final Field meterField;

    private static final Field writtenField;

    private static final Field outField;

    private static final List<Properties> properties;

    static {
        Properties props1 = new Properties();
        Properties props2 = new Properties();
        Properties props3 = new Properties();
        props1.setProperty("test.name", "with limit=Integer.MAX_VALUE");
        props1.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props1.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Integer.MAX_VALUE));
        props2.setProperty("test.name", "with limit=Integer.MAX_VALUE*4");
        props2.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props2.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(((long) Integer.MAX_VALUE) * 4));
        props3.setProperty("test.name", "with limit=Long.MAX_VALUE - 1024");
        props3.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props3.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Long.MAX_VALUE - 1024));
        properties = Collections.unmodifiableList(Arrays.asList(props1, props2, props3));
        try {
            Class<?> metteredStreamClass = Class.forName(FileHandler.class.getName() + "$MeteredStream");
            limitField = FileHandler.class.getDeclaredField("limit");
            limitField.setAccessible(true);
            meterField = FileHandler.class.getDeclaredField("meter");
            meterField.setAccessible(true);
            writtenField = metteredStreamClass.getDeclaredField("written");
            writtenField.setAccessible(true);
            outField = metteredStreamClass.getDeclaredField("out");
            outField.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException x) {
            throw new ExceptionInInitializerError(x);
        }
    }

    private static class TestOutputStream extends OutputStream {

        final OutputStream delegate;

        TestOutputStream(OutputStream delegate) {
            this.delegate = delegate;
        }

        @Override
        public void write(int b) throws IOException {
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }

        @Override
        public void flush() throws IOException {
            delegate.flush();
        }
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

    private static void checkException(Class<? extends Exception> type, FileHandlerSupplier test) {
        Throwable t = null;
        FileHandler f = null;
        try {
            f = test.test();
        } catch (Throwable x) {
            t = x;
        }
        try {
            if (type != null && t == null) {
                throw new RuntimeException("Expected " + type.getName() + " not thrown");
            } else if (type != null && t != null) {
                if (type.isInstance(t)) {
                    System.out.println("Recieved expected exception: " + t);
                } else {
                    throw new RuntimeException("Exception type mismatch: " + type.getName() + " expected, " + t.getClass().getName() + " received.", t);
                }
            } else if (t != null) {
                throw new RuntimeException("Unexpected exception received: " + t, t);
            }
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (Throwable x) {
                }
                ;
            }
        }
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

    private static long getLimit(FileHandler handler) throws Exception {
        return Configure.callPrivileged((Callable<Long>) () -> {
            return limitField.getLong(handler);
        });
    }

    private static OutputStream getMeteredOutput(FileHandler handler) throws Exception {
        return Configure.callPrivileged((Callable<OutputStream>) () -> {
            final OutputStream metered = OutputStream.class.cast(meterField.get(handler));
            return metered;
        });
    }

    private static TestOutputStream setTestOutputStream(OutputStream metered) throws Exception {
        return Configure.callPrivileged((Callable<TestOutputStream>) () -> {
            outField.set(metered, new TestOutputStream(OutputStream.class.cast(outField.get(metered))));
            return TestOutputStream.class.cast(outField.get(metered));
        });
    }

    private static long getWritten(OutputStream metered) throws Exception {
        return Configure.callPrivileged((Callable<Long>) () -> {
            return writtenField.getLong(metered);
        });
    }

    private static long setWritten(OutputStream metered, long newValue) throws Exception {
        return Configure.callPrivileged((Callable<Long>) () -> {
            writtenField.setLong(metered, newValue);
            return writtenField.getLong(metered);
        });
    }

    public static FileHandler testFileHandlerLimit(FileHandlerSupplier supplier, long limit) throws Exception {
        Configure.doPrivileged(() -> {
            try {
                Files.deleteIfExists(Paths.get(PREFIX));
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        });
        final FileHandler fh = supplier.test();
        try {
            assertEquals(limit, getLimit(fh), "limit");
            OutputStream metered = getMeteredOutput(fh);
            setTestOutputStream(metered);
            assertEquals(0, getWritten(metered), "written");
            String msg = "this is at least 10 chars long";
            LogRecord record = new LogRecord(Level.SEVERE, msg);
            fh.publish(record);
            fh.flush();
            long w = getWritten(metered);
            long offset = getWritten(metered);
            System.out.println("first offset is:  " + offset);
            fh.publish(record);
            fh.flush();
            offset = getWritten(metered) - w;
            w = getWritten(metered);
            System.out.println("second offset is: " + offset);
            fh.publish(record);
            fh.flush();
            offset = getWritten(metered) - w;
            w = getWritten(metered);
            System.out.println("third offset is:  " + offset);
            fh.publish(record);
            fh.flush();
            offset = getWritten(metered) - w;
            System.out.println("fourth offset is: " + offset);
            assertEquals(limit - 2 * offset + 10, setWritten(metered, limit - 2 * offset + 10), "written");
            w = getWritten(metered);
            fh.publish(record);
            fh.flush();
            assertEquals(w + offset, getWritten(metered), "written");
            if (getMeteredOutput(fh) != metered) {
                throw new RuntimeException("Log should not have rotated");
            }
            fh.publish(record);
            fh.flush();
            fh.publish(record);
            fh.flush();
            if (getMeteredOutput(fh) == metered) {
                throw new RuntimeException("Log should have rotated");
            }
            return fh;
        } catch (Error | Exception x) {
            try {
                fh.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            throw x;
        }
    }

    public static void test(String name, Properties props, long limit) throws Exception {
        System.out.println("Testing: " + name);
        Class<? extends Exception> expectedException = null;
        if (userDirWritable || expectedException != null) {
            checkException(expectedException, () -> new FileHandler());
            checkException(expectedException, () -> {
                final FileHandler fh = new FileHandler();
                assertEquals(limit, getLimit(fh), "limit");
                return fh;
            });
            checkException(expectedException, () -> testFileHandlerLimit(() -> new FileHandler(), limit));
            checkException(expectedException, () -> testFileHandlerLimit(() -> new FileHandler(PREFIX, Long.MAX_VALUE, 1, true), Long.MAX_VALUE));
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

        static final Policy DEFAULT_POLICY = Policy.getPolicy();

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
            return permissions.implies(permission) || DEFAULT_POLICY.implies(domain, permission);
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
