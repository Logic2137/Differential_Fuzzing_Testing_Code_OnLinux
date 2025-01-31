
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
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;


public class HandlersOnComplexResetUpdate {

    
    public static enum TestCase {
        UNSECURE, SECURE;
        public void run(List<Properties> properties) throws Exception {
            System.out.println("Running test case: " + name());
            Configure.setUp(this, properties.get(0));
            test(this.name(), properties);
        }
    }


    private static final String PREFIX =
            "FileHandler-" + UUID.randomUUID() + ".log";
    private static final String userDir = System.getProperty("user.dir", ".");
    private static final boolean userDirWritable = Files.isWritable(Paths.get(userDir));

    private static final List<Properties> properties;
    static {
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

        Properties props1 = new Properties();
        props1.setProperty("test.name", "parent logger with handler");
        props1.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props1.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Integer.MAX_VALUE));
        props1.setProperty(FileHandler.class.getName() + ".level", "ALL");
        props1.setProperty(FileHandler.class.getName() + ".formatter", "java.util.logging.SimpleFormatter");
        props1.setProperty("com.foo.handlers", FileHandler.class.getName());
        props1.setProperty("test.checkHandlersOnParent", "true");
        props1.setProperty("test.checkHandlersOn", "com.foo");
        props1.setProperty("com.bar.level", "FINEST");

        Properties props2 = new Properties();
        props2.setProperty("java.util.logging.LogManager.reconfigureHandlers", "true");
        props2.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props2.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Integer.MAX_VALUE));
        props2.setProperty(FileHandler.class.getName() + ".level", "ALL");
        props2.setProperty(FileHandler.class.getName() + ".formatter", "java.util.logging.SimpleFormatter");
        props2.setProperty("com.foo.handlers", FileHandler.class.getName());
        props2.setProperty("test.checkHandlersOnParent", "true");
        props2.setProperty("test.checkHandlersOn", "com.foo");
        props2.setProperty("com.bar.level", "FINEST");

        Properties props3 = new Properties();
        props3.setProperty("test.name", "parent logger with handler");
        props3.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props3.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Integer.MAX_VALUE));
        props3.setProperty(FileHandler.class.getName() + ".level", "ALL");
        props3.setProperty(FileHandler.class.getName() + ".formatter", "java.util.logging.SimpleFormatter");
        props3.setProperty("com.foo.handlers", FileHandler.class.getName());
        props3.setProperty("test.checkHandlersOnParent", "true");
        props3.setProperty("test.checkHandlersOn", "com.foo");
        props3.setProperty("com.bar.level", "FINEST");

        Properties props4 = new Properties();
        props4.setProperty("java.util.logging.LogManager.reconfigureHandlers", "true");
        props4.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props4.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Integer.MAX_VALUE));
        props4.setProperty(FileHandler.class.getName() + ".level", "ALL");
        props4.setProperty(FileHandler.class.getName() + ".formatter", "java.util.logging.SimpleFormatter");
        props4.setProperty("test.checkHandlersOnParent", "true");
        props4.setProperty("test.checkHandlersOn", "com.foo");
        props4.setProperty("com.foo.handlers", FileHandler.class.getName());

        Properties props5 = new Properties();
        props5.setProperty("test.name", "parent logger with handler");
        props5.setProperty(FileHandler.class.getName() + ".pattern", PREFIX);
        props5.setProperty(FileHandler.class.getName() + ".limit", String.valueOf(Integer.MAX_VALUE));
        props5.setProperty(FileHandler.class.getName() + ".level", "ALL");
        props5.setProperty(FileHandler.class.getName() + ".formatter", "java.util.logging.SimpleFormatter");
        props5.setProperty("test.checkHandlersOnParent", "false");
        props5.setProperty("test.checkHandlersOn", "com");
        props5.setProperty("com.handlers", FileHandler.class.getName());

        properties = Collections.unmodifiableList(Arrays.asList(
                    props1, props2, props3, props4, props5));
    }

    
    static void test(String name, List<Properties> properties)
            throws Exception {

        System.out.println("\nTesting: " + name);
        if (!userDirWritable) {
            throw new RuntimeException("Not writable: "+userDir);
        }

        
        Logger fooChild = Logger.getLogger("com.foo.child");
        fooChild.info("hello world");
        Logger barChild = Logger.getLogger("com.bar.child");
        barChild.info("hello world");

        ReferenceQueue<Logger> queue = new ReferenceQueue();
        WeakReference<Logger> fooRef = new WeakReference<>(Logger.getLogger("com.foo"), queue);
        if (fooRef.get() != fooChild.getParent()) {
            throw new RuntimeException("Unexpected parent logger: "
                    + fooChild.getParent() +"\n\texpected: " + fooRef.get());
        }
        WeakReference<Logger> barRef = new WeakReference<>(Logger.getLogger("com.bar"), queue);
        if (barRef.get() != barChild.getParent()) {
            throw new RuntimeException("Unexpected parent logger: "
                    + barChild.getParent() +"\n\texpected: " + barRef.get());
        }
        Reference<? extends Logger> ref2;
        int max = 3;
        barChild = null;
        while ((ref2 = queue.poll()) == null) {
            System.gc();
            Thread.sleep(100);
            if (--max == 0) break;
        }

        Throwable failed = null;
        try {
            if (ref2 != null) {
                String refName = ref2 == fooRef ? "fooRef" : ref2 == barRef ? "barRef" : "unknown";
                if (ref2 != barRef) {
                    throw new RuntimeException("Unexpected logger reference cleared: " + refName);
                } else {
                    System.out.println("Reference " + refName + " cleared as expected");
                }
            } else if (ref2 == null) {
                throw new RuntimeException("Expected 'barRef' to be cleared");
            }
            
            
            Properties previousProps  = properties.get(0);
            int expectedHandlersCount = 1;
            boolean checkHandlersOnParent = Boolean.parseBoolean(
                    previousProps.getProperty("test.checkHandlersOnParent", "true"));
            String checkHandlersOn = previousProps.getProperty("test.checkHandlersOn", null);
            for (int i=1; i<properties.size(); i++) {
                System.out.println("\n*** Reconfiguration with properties["+i+"]\n");
                Properties nextProps = properties.get(i);
                boolean reconfigureHandlers = true;

                if (checkHandlersOnParent) {
                    assertEquals(expectedHandlersCount,
                            fooChild.getParent().getHandlers().length,
                            "fooChild.getParent().getHandlers().length");
                }
                if (checkHandlersOn != null) {
                    Logger loggerWithHandlers = LogManager.getLogManager().getLogger(checkHandlersOn);
                    if (loggerWithHandlers == null) {
                        throw new RuntimeException("Logger with handlers not found: " + checkHandlersOn);
                    }
                    assertEquals(expectedHandlersCount,
                            loggerWithHandlers.getHandlers().length,
                            checkHandlersOn + ".getHandlers().length");
                }

                
                Configure.doPrivileged(() -> LogManager.getLogManager().reset());
                assertEquals(0, fooChild.getParent().getHandlers().length, "fooChild.getParent().getHandlers().length");
                if (checkHandlersOn != null) {
                    Logger loggerWithHandlers = LogManager.getLogManager().getLogger(checkHandlersOn);
                    if (loggerWithHandlers == null) {
                        throw new RuntimeException("Logger with handlers not found: " + checkHandlersOn);
                    }
                    assertEquals(0, loggerWithHandlers.getHandlers().length,
                            checkHandlersOn + ".getHandlers().length");
                }

                if (i == 4) {
                    System.out.println("Last configuration...");
                }
                
                Configure.doPrivileged(() -> Configure.updateConfigurationWith(nextProps, false));

                expectedHandlersCount = reconfigureHandlers ? 1 : 0;
                checkHandlersOnParent = Boolean.parseBoolean(
                    nextProps.getProperty("test.checkHandlersOnParent", "true"));
                checkHandlersOn = nextProps.getProperty("test.checkHandlersOn", null);

                if (checkHandlersOnParent) {
                    assertEquals(expectedHandlersCount,
                        fooChild.getParent().getHandlers().length,
                        "fooChild.getParent().getHandlers().length");
                } else {
                    assertEquals(0,
                        fooChild.getParent().getHandlers().length,
                        "fooChild.getParent().getHandlers().length");
                }
                if (checkHandlersOn != null) {
                    Logger loggerWithHandlers = LogManager.getLogManager().getLogger(checkHandlersOn);
                    if (loggerWithHandlers == null) {
                        throw new RuntimeException("Logger with handlers not found: " + checkHandlersOn);
                    }
                    assertEquals(expectedHandlersCount,
                            loggerWithHandlers.getHandlers().length,
                            checkHandlersOn + ".getHandlers().length");
                }
            }
        } catch (Throwable t) {
            failed = t;
        } finally {
            final Throwable suppressed = failed;
            Configure.doPrivileged(() -> LogManager.getLogManager().reset());
            Configure.doPrivileged(() -> {
                try {
                    StringBuilder builder = new StringBuilder();
                    Files.list(Paths.get(userDir))
                        .filter((f) -> f.toString().contains(PREFIX))
                        .filter((f) -> f.toString().endsWith(".lck"))
                        .forEach((f) -> {
                                builder.append(f.toString()).append('\n');
                        });
                    if (!builder.toString().isEmpty()) {
                        throw new RuntimeException("Lock files not cleaned:\n"
                                + builder.toString());
                    }
                } catch(RuntimeException | Error x) {
                    if (suppressed != null) x.addSuppressed(suppressed);
                    throw x;
                } catch(Exception x) {
                    if (suppressed != null) x.addSuppressed(suppressed);
                    throw new RuntimeException(x);
                }
            });
            fooChild = null;
            System.out.println("Setting fooChild to: " + fooChild);
            while ((ref2 = queue.poll()) == null) {
                System.gc();
                Thread.sleep(1000);
            }
            if (ref2 != fooRef) {
                throw new RuntimeException("Unexpected reference: "
                        + ref2 +"\n\texpected: " + fooRef);
            }
            if (ref2.get() != null) {
                throw new RuntimeException("Referent not cleared: " + ref2.get());
            }
            System.out.println("Got fooRef after reset(), fooChild is " + fooChild);

        }
        if (failed != null) {
            
            throw new RuntimeException(failed);
        }

    }

    public static void main(String... args) throws Exception {


        if (args == null || args.length == 0) {
            args = new String[] {
                TestCase.UNSECURE.name(),
                TestCase.SECURE.name(),
            };
        }

        try {
            for (String testName : args) {
                TestCase test = TestCase.valueOf(testName);
                test.run(properties);
            }
        } finally {
            if (userDirWritable) {
                Configure.doPrivileged(() -> {
                    
                    try {
                        Files.list(Paths.get(userDir))
                            .filter((f) -> f.toString().contains(PREFIX))
                            .forEach((f) -> {
                                try {
                                    System.out.println("deleting " + f);
                                    Files.delete(f);
                                } catch(Throwable t) {
                                    System.err.println("Failed to delete " + f + ": " + t);
                                }
                            });
                    } catch(Throwable t) {
                        System.err.println("Cleanup failed to list files: " + t);
                        t.printStackTrace();
                    }
                });
            }
        }
    }

    static class Configure {
        static Policy policy = null;
        static final ThreadLocal<AtomicBoolean> allowAll = new ThreadLocal<AtomicBoolean>() {
            @Override
            protected AtomicBoolean initialValue() {
                return  new AtomicBoolean(false);
            }
        };
        static void setUp(TestCase test, Properties propertyFile) {
            switch (test) {
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
                updateConfigurationWith(propertyFile, false);
            });
        }

        static void updateConfigurationWith(Properties propertyFile, boolean append) {
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                propertyFile.store(bytes, propertyFile.getProperty("test.name"));
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes.toByteArray());
                Function<String, BiFunction<String,String,String>> remapper =
                        append ? (x) -> ((o, n) -> n == null ? o : n)
                               : (x) -> ((o, n) -> n);
                LogManager.getLogManager().updateConfiguration(bais, remapper);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        static void doPrivileged(Runnable run) {
            final boolean old = allowAll.get().getAndSet(true);
            try {
                run.run();
            } finally {
                allowAll.get().set(old);
            }
        }
        static <T> T callPrivileged(Callable<T> call) throws Exception {
            final boolean old = allowAll.get().getAndSet(true);
            try {
                return call.call();
            } finally {
                allowAll.get().set(old);
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
            throw new TestAssertException("Unexpected result for " + msg
                    + ".\n\texpected: " + expected
                    +  "\n\tactual:   " + received);
        } else {
            System.out.println("Got expected " + msg + ": " + received);
        }
    }

    final static class PermissionsBuilder {
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
        final ThreadLocal<AtomicBoolean> allowAll; 
        public SimplePolicy(TestCase test, ThreadLocal<AtomicBoolean> allowAll) {
            this.allowAll = allowAll;
            permissions = new Permissions();
            permissions.add(new LoggingPermission("control", null));
            permissions.add(new FilePermission(PREFIX+".lck", "read,write,delete"));
            permissions.add(new FilePermission(PREFIX, "read,write"));

            
            allPermissions = new Permissions();
            allPermissions.add(new java.security.AllPermission());

        }

        @Override
        public boolean implies(ProtectionDomain domain, Permission permission) {
            if (allowAll.get().get()) return allPermissions.implies(permission);
            return permissions.implies(permission);
        }

        @Override
        public PermissionCollection getPermissions(CodeSource codesource) {
            return new PermissionsBuilder().addAll(allowAll.get().get()
                    ? allPermissions : permissions).toPermissions();
        }

        @Override
        public PermissionCollection getPermissions(ProtectionDomain domain) {
            return new PermissionsBuilder().addAll(allowAll.get().get()
                    ? allPermissions : permissions).toPermissions();
        }
    }

}
