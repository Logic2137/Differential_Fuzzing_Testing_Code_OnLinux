



package test.java.lang.invoke;

import java.lang.invoke.MethodType;
import java.security.AccessControlException;
import java.security.Permission;

public class MethodTypeSecurityManager {
    private static boolean hasClassLoaderAccess;
    public static void main(String... args) throws Throwable {
        ClassLoader platformLoader = ClassLoader.getPlatformClassLoader();
        ClassLoader appLoader = ClassLoader.getSystemClassLoader();
        hasClassLoaderAccess = args.length == 1 && "access".equals(args[0]);

        assert hasClassLoaderAccess || System.getSecurityManager() == null;
        if (!hasClassLoaderAccess) {
            System.setSecurityManager(new SecurityManager());
        }

        
        throwACC("()Ljdk/internal/misc/VM;", null);
        
        throwACC("()Ljdk/internal/misc/VM;", appLoader);

        
        MethodType.fromMethodDescriptorString("()Ljdk/internal/misc/VM;", platformLoader);
    }

    private static void throwACC(String desc, ClassLoader loader) {
        try {
            MethodType.fromMethodDescriptorString(desc, loader);
            throw new RuntimeException("should never leak JDK internal class");
        } catch (AccessControlException e) {
            System.out.println(e.getMessage());
            Permission perm = e.getPermission();
            if (!(perm instanceof RuntimePermission)) throw e;
            
            switch (perm.getName()) {
                case "getClassLoader":
                    if (!hasClassLoaderAccess) break;
                case "accessClassInPackage.jdk.internal.misc":
                    break;
                default:
                    throw e;
            }
        }
    }
}
