import java.io.ByteArrayInputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.PropertyPermission;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.LogManager;
import java.util.logging.LoggingPermission;

public class TestConfigurationListeners {

    public static enum TestCase {

        UNSECURE, SECURE, PERMISSION;

        public void run(String name) throws Exception {
            System.out.println("Running test case: " + name());
            switch(this) {
                case UNSECURE:
                    testUnsecure(name);
                    break;
                case SECURE:
                    testSecure(name);
                    break;
                case PERMISSION:
                    testPermission(name);
                    break;
                default:
                    throw new Error("Unknown test case: " + this);
            }
        }

        public String loggerName(String name) {
            return name;
        }
    }

    public static void main(String... args) throws Exception {
        if (args == null || args.length == 0) {
            args = new String[] { TestCase.UNSECURE.name(), TestCase.SECURE.name() };
        }
        for (String testName : args) {
            TestCase test = TestCase.valueOf(testName);
            test.run(test.loggerName("foo.bar"));
        }
    }

    public static void testUnsecure(String loggerName) throws Exception {
        if (System.getSecurityManager() != null) {
            throw new Error("Security manager is set");
        }
        test(loggerName);
    }

    public static void testSecure(String loggerName) throws Exception {
        if (System.getSecurityManager() != null) {
            throw new Error("Security manager is already set");
        }
        Policy.setPolicy(new SimplePolicy(TestCase.SECURE));
        System.setSecurityManager(new SecurityManager());
        test(loggerName);
    }

    public static void testPermission(String loggerName) {
        TestConfigurationListener run = new TestConfigurationListener(TestCase.PERMISSION.toString());
        if (System.getSecurityManager() != null) {
            throw new Error("Security manager is already set");
        }
        Policy.setPolicy(new SimplePolicy(TestCase.PERMISSION));
        System.setSecurityManager(new SecurityManager());
        try {
            LogManager.getLogManager().addConfigurationListener(run);
            throw new RuntimeException("addConfigurationListener: Permission not checked!");
        } catch (AccessControlException x) {
            boolean ok = false;
            if (x.getPermission() instanceof LoggingPermission) {
                if ("control".equals(x.getPermission().getName())) {
                    System.out.println("addConfigurationListener: Got expected exception: " + x);
                    ok = true;
                }
            }
            if (!ok) {
                throw new RuntimeException("addConfigurationListener: Unexpected exception: " + x, x);
            }
        }
        try {
            LogManager.getLogManager().removeConfigurationListener(run);
            throw new RuntimeException("removeConfigurationListener: Permission not checked!");
        } catch (AccessControlException x) {
            boolean ok = false;
            if (x.getPermission() instanceof LoggingPermission) {
                if ("control".equals(x.getPermission().getName())) {
                    System.out.println("removeConfigurationListener: Got expected exception: " + x);
                    ok = true;
                }
            }
            if (!ok) {
                throw new RuntimeException("removeConfigurationListener: Unexpected exception: " + x, x);
            }
        }
        try {
            LogManager.getLogManager().addConfigurationListener(null);
            throw new RuntimeException("addConfigurationListener(null): Expected NPE not thrown.");
        } catch (NullPointerException npe) {
            System.out.println("Got expected NPE: " + npe);
        }
        try {
            LogManager.getLogManager().removeConfigurationListener(null);
            throw new RuntimeException("removeConfigurationListener(null): Expected NPE not thrown.");
        } catch (NullPointerException npe) {
            System.out.println("Got expected NPE: " + npe);
        }
    }

    static class TestConfigurationListener implements Runnable {

        final AtomicLong count = new AtomicLong(0);

        final String name;

        TestConfigurationListener(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            final long times = count.incrementAndGet();
            System.out.println("Configured \"" + name + "\": " + times);
        }
    }

    static class ConfigurationListenerException extends RuntimeException {

        public ConfigurationListenerException(String msg) {
            super(msg);
        }

        @Override
        public String toString() {
            return this.getClass().getName() + ": " + getMessage();
        }
    }

    static class ConfigurationListenerError extends Error {

        public ConfigurationListenerError(String msg) {
            super(msg);
        }

        @Override
        public String toString() {
            return this.getClass().getName() + ": " + getMessage();
        }
    }

    static class ThrowingConfigurationListener extends TestConfigurationListener {

        final boolean error;

        public ThrowingConfigurationListener(String name, boolean error) {
            super(name);
            this.error = error;
        }

        @Override
        public void run() {
            if (error)
                throw new ConfigurationListenerError(name);
            else
                throw new ConfigurationListenerException(name);
        }

        @Override
        public String toString() {
            final Class<? extends Throwable> type = error ? ConfigurationListenerError.class : ConfigurationListenerException.class;
            return type.getName() + ": " + name;
        }
    }

    private static void expect(TestConfigurationListener listener, long value) {
        final long got = listener.count.longValue();
        if (got != value) {
            throw new RuntimeException(listener.name + " expected " + value + ", got " + got);
        }
    }

    public interface ThrowingConsumer<T, I extends Exception> {

        public void accept(T t) throws I;
    }

    public static class ReadConfiguration implements ThrowingConsumer<LogManager, IOException> {

        @Override
        public void accept(LogManager t) throws IOException {
            t.readConfiguration();
        }
    }

    public static void test(String loggerName) throws Exception {
        System.out.println("Starting test for " + loggerName);
        test("m.readConfiguration()", (m) -> m.readConfiguration());
        test("m.readConfiguration(new ByteArrayInputStream(new byte[0]))", (m) -> m.readConfiguration(new ByteArrayInputStream(new byte[0])));
        System.out.println("Test passed for " + loggerName);
    }

    public static void test(String testName, ThrowingConsumer<LogManager, IOException> readConfiguration) throws Exception {
        System.out.println("\nBEGIN " + testName);
        LogManager m = LogManager.getLogManager();
        final TestConfigurationListener l1 = new TestConfigurationListener("l#1");
        final TestConfigurationListener l2 = new TestConfigurationListener("l#2");
        final TestConfigurationListener l3 = new ThrowingConfigurationListener("l#3", false);
        final TestConfigurationListener l4 = new ThrowingConfigurationListener("l#4", true);
        final TestConfigurationListener l5 = new ThrowingConfigurationListener("l#5", false);
        final Set<String> expectedExceptions = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(l3.toString(), l4.toString(), l5.toString())));
        m.addConfigurationListener(l1);
        m.addConfigurationListener(l2);
        expect(l1, 0);
        expect(l2, 0);
        readConfiguration.accept(m);
        expect(l1, 1);
        expect(l2, 1);
        m.addConfigurationListener(l1);
        expect(l1, 1);
        expect(l2, 1);
        readConfiguration.accept(m);
        expect(l1, 2);
        expect(l2, 2);
        m.removeConfigurationListener(l1);
        expect(l1, 2);
        expect(l2, 2);
        readConfiguration.accept(m);
        expect(l1, 2);
        expect(l2, 3);
        m.removeConfigurationListener(l1);
        expect(l1, 2);
        expect(l2, 3);
        readConfiguration.accept(m);
        expect(l1, 2);
        expect(l2, 4);
        m.removeConfigurationListener(l2);
        expect(l1, 2);
        expect(l2, 4);
        readConfiguration.accept(m);
        expect(l1, 2);
        expect(l2, 4);
        m.removeConfigurationListener(l1);
        m.removeConfigurationListener(l1);
        m.removeConfigurationListener(l2);
        m.removeConfigurationListener(l2);
        expect(l1, 2);
        expect(l2, 4);
        readConfiguration.accept(m);
        expect(l1, 2);
        expect(l2, 4);
        m.addConfigurationListener(l1);
        m.addConfigurationListener(l2);
        expect(l1, 2);
        expect(l2, 4);
        readConfiguration.accept(m);
        expect(l1, 3);
        expect(l2, 5);
        m.removeConfigurationListener(l1);
        m.removeConfigurationListener(l2);
        expect(l1, 3);
        expect(l2, 5);
        readConfiguration.accept(m);
        expect(l1, 3);
        expect(l2, 5);
        m.addConfigurationListener(l4);
        m.addConfigurationListener(l1);
        m.addConfigurationListener(l2);
        m.addConfigurationListener(l3);
        m.addConfigurationListener(l5);
        try {
            readConfiguration.accept(m);
            throw new RuntimeException("Excpected exception/error not raised");
        } catch (ConfigurationListenerException | ConfigurationListenerError t) {
            final Set<String> received = new HashSet<>();
            received.add(t.toString());
            for (Throwable s : t.getSuppressed()) {
                received.add(s.toString());
            }
            System.out.println("Received exceptions: " + received);
            if (!expectedExceptions.equals(received)) {
                throw new RuntimeException("List of received exceptions differs from expected:" + "\n\texpected: " + expectedExceptions + "\n\treceived: " + received);
            }
        }
        expect(l1, 4);
        expect(l2, 6);
        m.removeConfigurationListener(l1);
        m.removeConfigurationListener(l2);
        m.removeConfigurationListener(l3);
        m.removeConfigurationListener(l4);
        m.removeConfigurationListener(l5);
        readConfiguration.accept(m);
        expect(l1, 4);
        expect(l2, 6);
        try {
            m.addConfigurationListener(null);
            throw new RuntimeException("addConfigurationListener(null): Expected NPE not thrown.");
        } catch (NullPointerException npe) {
            System.out.println("Got expected NPE: " + npe);
        }
        try {
            m.removeConfigurationListener(null);
            throw new RuntimeException("removeConfigurationListener(null): Expected NPE not thrown.");
        } catch (NullPointerException npe) {
            System.out.println("Got expected NPE: " + npe);
        }
        System.out.println("END " + testName + "\n");
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

        public SimplePolicy(TestCase test) {
            permissions = new Permissions();
            if (test != TestCase.PERMISSION) {
                permissions.add(new LoggingPermission("control", null));
                permissions.add(new PropertyPermission("java.util.logging.config.class", "read"));
                permissions.add(new PropertyPermission("java.util.logging.config.file", "read"));
                permissions.add(new PropertyPermission("java.home", "read"));
                permissions.add(new FilePermission("<<ALL FILES>>", "read"));
            }
        }

        @Override
        public boolean implies(ProtectionDomain domain, Permission permission) {
            return permissions.implies(permission) || DEFAULT_POLICY.implies(domain, permission);
        }

        @Override
        public PermissionCollection getPermissions(CodeSource codesource) {
            return new PermissionsBuilder().addAll(permissions).toPermissions();
        }

        @Override
        public PermissionCollection getPermissions(ProtectionDomain domain) {
            return new PermissionsBuilder().addAll(permissions).toPermissions();
        }
    }
}
