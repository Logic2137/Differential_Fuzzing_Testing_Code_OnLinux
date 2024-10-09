



import java.lang.reflect.Field;
import java.security.AccessControlException;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class  AtomicUpdaters {
    static final Policy DEFAULT_POLICY = Policy.getPolicy();

    enum TYPE { INT, LONG, REF }

    static class Config {
        final Class<?> clazz;
        final String field;
        final String access;
        final boolean reflectOk;
        final boolean updaterOk;
        final String desc;
        final TYPE type;

        Config(Class<?> clazz, String field, String access,
               boolean reflectOk, boolean updaterOk, String desc, TYPE type) {
            this.clazz = clazz;
            this.field = field;
            this.access = access;
            this.reflectOk = reflectOk;
            this.updaterOk = updaterOk;
            this.desc = desc;
            this.type =type;
        }

        public String toString() {
            return desc + ": " + access + " " + clazz.getName() + "." + field;
        }
    }

    static Config[] tests;

    static void initTests(boolean hasSM) {
        tests = new Config[] {
            new Config(AtomicUpdaters.class, "pub_int", "public", true, true, "public int field of current class", TYPE.INT),
            new Config(AtomicUpdaters.class, "priv_int", "private", true, true, "private int field of current class", TYPE.INT),
            new Config(AtomicUpdaters.class, "pub_long", "public", true, true, "public long field of current class", TYPE.LONG),
            new Config(AtomicUpdaters.class, "priv_long", "private", true, true, "private long field of current class", TYPE.LONG),
            new Config(AtomicUpdaters.class, "pub_ref", "public", true, true, "public ref field of current class", TYPE.REF),
            new Config(AtomicUpdaters.class, "priv_ref", "private", true, true, "private ref field of current class", TYPE.REF),

            
            
            new Config(AtomicInteger.class, "value", "private", hasSM ? false : true, false, "private int field of class in different package", TYPE.INT),
            new Config(AtomicLong.class, "value", "private", hasSM ? false : true, false, "private long field of class in different package", TYPE.LONG),
            new Config(AtomicReference.class, "value", "private", hasSM ? false : true, false, "private reference field of class in different package", TYPE.REF),
        };
    }

    public volatile int pub_int;
    private volatile int priv_int;
    public volatile long pub_long;
    private volatile long priv_long;
    public volatile Object pub_ref;
    private volatile Object priv_ref;


    
    
    
    static boolean verbose;

    public static void main(String[] args) throws Throwable {
        boolean hasSM = false;
        for (String arg : args) {
            if ("-v".equals(arg)) {
                verbose = true;
            }
            else if ("UseSM".equals(arg)) {
                
                Policy.setPolicy(new NoPermissionsPolicy());
                SecurityManager m = System.getSecurityManager();
                if (m != null)
                    throw new RuntimeException("No security manager should initially be installed");
                System.setSecurityManager(new java.lang.SecurityManager());
                hasSM = true;
            }
            else {
                throw new IllegalArgumentException("Unexpected option: " + arg);
            }
        }
        initTests(hasSM);

        int failures = 0;

        System.out.printf("Testing with%s a SecurityManager present\n", hasSM ? "" : "out");
        for (Config c : tests) {
            System.out.println("Testing: " + c);
            Error reflectionFailure = null;
            Error updaterFailure = null;
            Class<?> clazz = c.clazz;
            
            System.out.println(" - testing getDeclaredField");
            try {
                Field f = clazz.getDeclaredField(c.field);
                if (!c.reflectOk)
                    reflectionFailure = new Error("Unexpected reflective access: " + c);
            }
            catch (AccessControlException e) {
                if (c.reflectOk)
                    reflectionFailure = new Error("Unexpected reflective access failure: " + c, e);
                else if (verbose) {
                    System.out.println("Got expected reflection exception: " + e);
                    e.printStackTrace(System.out);
                }
            }

            if (reflectionFailure != null) {
                reflectionFailure.printStackTrace(System.out);
            }

            
            Object u = null;
            try {
                switch (c.type) {
                case INT:
                    System.out.println(" - testing AtomicIntegerFieldUpdater");
                    u = AtomicIntegerFieldUpdater.newUpdater(clazz, c.field);
                    break;
                case LONG:
                    System.out.println(" - testing AtomicLongFieldUpdater");
                    u = AtomicLongFieldUpdater.newUpdater(clazz, c.field);
                    break;
                case REF:
                    System.out.println(" - testing AtomicReferenceFieldUpdater");
                    u = AtomicReferenceFieldUpdater.newUpdater(clazz, Object.class, c.field);
                    break;
                }

                if (!c.updaterOk)
                    updaterFailure =  new Error("Unexpected updater access: " + c);
            }
            catch (Exception e) {
                if (c.updaterOk)
                    updaterFailure = new Error("Unexpected updater access failure: " + c, e);
                else if (verbose) {
                    System.out.println("Got expected updater exception: " + e);
                    e.printStackTrace(System.out);
                }
            }

            if (updaterFailure != null) {
                updaterFailure.printStackTrace(System.out);
            }

            if (updaterFailure != null || reflectionFailure != null) {
                failures++;

            }
        }

        if (failures > 0) {
            throw new Error("Some tests failed - see previous stacktraces");
        }
    }

    
    private static class NoPermissionsPolicy extends Policy {
        @Override
        public PermissionCollection getPermissions(CodeSource cs) {
            return Policy.UNSUPPORTED_EMPTY_COLLECTION;
        }

        @Override
        public PermissionCollection getPermissions(ProtectionDomain pd) {
            return Policy.UNSUPPORTED_EMPTY_COLLECTION;
        }

        @Override
        public boolean implies(ProtectionDomain pd, Permission p) {
            return Policy.UNSUPPORTED_EMPTY_COLLECTION.implies(p) ||
                    DEFAULT_POLICY.implies(pd, p);
        }
    }
}
