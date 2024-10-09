
package p3;

import java.io.FilePermission;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessControlException;
import java.security.Permission;
import java.util.Set;

public class NoAccess {

    private static final Module M3 = NoAccess.class.getModule();

    private static final Path MODS_DIR1 = Paths.get("mods1");

    private static final Path MODS_DIR2 = Paths.get("mods2");

    public static void main(String[] args) throws Exception {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            System.setSecurityManager(null);
        }
        ModuleFinder finder = ModuleFinder.of(Paths.get("mods1"), Paths.get("mods2"));
        ModuleLayer bootLayer = ModuleLayer.boot();
        Configuration parent = bootLayer.configuration();
        Configuration cf = parent.resolveAndBind(finder, ModuleFinder.of(), Set.of("m1", "m2"));
        ClassLoader scl = ClassLoader.getSystemClassLoader();
        ModuleLayer layer = bootLayer.defineModulesWithManyLoaders(cf, scl);
        if (sm != null) {
            System.setSecurityManager(sm);
        }
        Module m1 = bootLayer.findModule("m1").get();
        Module m2 = bootLayer.findModule("m2").get();
        Module m3 = bootLayer.findModule("m3").get();
        findClass(m1, "p1.internal.B");
        findClass(m2, "p2.C");
        findClass(m3, "p3.internal.Foo");
        findClass(m1, "p1.A");
        findClass(m1, "p1.internal.B");
        findClass(m2, "p2.C");
        findClass(m3, "p3.internal.Foo");
        m1 = layer.findModule("m1").get();
        m2 = layer.findModule("m2").get();
        m3 = layer.findModule("m3").get();
        findClass(m1, "p1.A");
        findClass(m3, "p3.internal.Foo");
        Path path = MODS_DIR1.resolve("p1").resolve("internal").resolve("B.class");
        findClass(m1, "p1.internal.B", new FilePermission(path.toString(), "read"));
        path = MODS_DIR2.resolve("p2").resolve("C.class");
        findClass(m2, "p2.C", new FilePermission(path.toString(), "read"));
    }

    static Class<?> findClass(Module module, String cn) {
        return findClass(module, cn, null);
    }

    static Class<?> findClass(Module module, String cn, Permission perm) {
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
            if (e.getPermission().equals(perm))
                return null;
            throw e;
        }
    }
}
