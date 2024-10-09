

package p3;

import java.security.AccessControlException;
import java.security.Permission;


public class NoGetClassLoaderAccess {
    private static final Module m3 = NoGetClassLoaderAccess.class.getModule();
    private static final Permission GET_CLASSLOADER_PERMISSION = new RuntimePermission("getClassLoader");

    public static void main(String[] args) throws Exception {
        ModuleLayer boot = ModuleLayer.boot();

        System.setSecurityManager(new SecurityManager());
        Module m1 = boot.findModule("m1").get();
        Module m2 = boot.findModule("m2").get();
        findClass(m1, "p1.A");
        findClass(m1, "p1.internal.B");
        findClass(m2, "p2.C");
        findClass(m3, "p3.internal.Foo");
    }

    static Class<?> findClass(Module module, String cn) {
        try {
            Class<?> c = Class.forName(module, cn);
            if (c == null) {
                throw new RuntimeException(cn + " not found in " + module);
            }
            if (c.getModule() != module) {
                throw new RuntimeException(c.getModule() + " != " + module);
            }
            return c;
        } catch (AccessControlException e) {
            if (module != m3) {
                if (e.getPermission().equals(GET_CLASSLOADER_PERMISSION))
                    return null;
            }
            throw e;
        }
    }
}
