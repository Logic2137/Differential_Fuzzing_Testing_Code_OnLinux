

import java.lang.reflect.Field;
import java.lang.reflect.ReflectPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;


public class ClassDeclaredFieldsTest {

    
    public static enum TestCase {
        UNSECURE, SECURE;
        public void run() throws Exception {
            System.out.println("Running test case: " + name());
            Configure.setUp(this);
            test(this);
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.version"));
        if (args == null || args.length == 0) {
            args = new String[] { "SECURE" };
        } else if (args.length != 1) {
            throw new IllegalArgumentException("Only one arg expected: "
                    + Arrays.asList(args));
        }
        TestCase.valueOf(args[0]).run();
    }

    static void test(TestCase test) {
        for (Field f : Class.class.getDeclaredFields()) {
            f.setAccessible(true);
            System.out.println("Field "+f.getName()+" is now accessible.");
            if (f.getName().equals("classLoader")) {
                throw new RuntimeException("Found "+f.getName()+" field!");
            }
        }
        try {
            Class.class.getDeclaredField("classLoader");
            throw new RuntimeException("Expected NoSuchFieldException for"
                    + " 'classLoader' field not raised");
        } catch(NoSuchFieldException x) {
            System.out.println("Got expected exception: " + x);
        }
        System.out.println("Passed "+test);
    }

    
    
    static class Configure {
        static Policy policy = null;
        static final ThreadLocal<AtomicBoolean> allowAll = new ThreadLocal<AtomicBoolean>() {
            @Override
            protected AtomicBoolean initialValue() {
                return  new AtomicBoolean(false);
            }
        };
        static void setUp(TestCase test) {
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
                    throw new InternalError("No such testcase: " + test);
            }
        }
        static void doPrivileged(Runnable run) {
            allowAll.get().set(true);
            try {
                run.run();
            } finally {
                allowAll.get().set(false);
            }
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
        final ThreadLocal<AtomicBoolean> allowAll; 
        public SimplePolicy(TestCase test, ThreadLocal<AtomicBoolean> allowAll) {
            this.allowAll = allowAll;
            
            
            
            permissions = new Permissions();
            permissions.add(new RuntimePermission("accessDeclaredMembers"));
            permissions.add(new ReflectPermission("suppressAccessChecks"));

            
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
