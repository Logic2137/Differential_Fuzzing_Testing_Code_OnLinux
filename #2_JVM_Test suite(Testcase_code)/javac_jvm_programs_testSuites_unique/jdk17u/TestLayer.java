import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class TestLayer {

    private static final Path MODS_DIR = Paths.get("mods");

    private static final Set<String> modules = Set.of("m1", "m2");

    public static void main(String[] args) throws Exception {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            System.setSecurityManager(null);
        }
        ModuleFinder finder = ModuleFinder.of(MODS_DIR);
        Configuration parent = ModuleLayer.boot().configuration();
        Configuration cf = parent.resolveAndBind(ModuleFinder.of(), finder, modules);
        ClassLoader scl = ClassLoader.getSystemClassLoader();
        ModuleLayer layer = ModuleLayer.boot().defineModulesWithManyLoaders(cf, scl);
        Module m1 = layer.findModule("m1").get();
        Module m2 = layer.findModule("m2").get();
        if (sm != null) {
            System.setSecurityManager(sm);
        }
        findClass(m1, "p1.A");
        findClass(m1, "p1.internal.B");
        findClass(m2, "p2.C");
        ClassLoader ld = TestLayer.class.getClassLoader();
        findClass(ld.getUnnamedModule(), "TestDriver");
        Class<?> c = Class.forName(m1, "p1.Initializer");
        Method m = c.getMethod("isInited");
        Boolean isClinited = (Boolean) m.invoke(null);
        if (isClinited.booleanValue()) {
            throw new RuntimeException("clinit should not be invoked");
        }
    }

    static Class<?> findClass(Module module, String cn) {
        Class<?> c = Class.forName(module, cn);
        if (c == null) {
            throw new RuntimeException(cn + " not found in " + module);
        }
        if (c.getModule() != module) {
            throw new RuntimeException(c.getModule() + " != " + module);
        }
        return c;
    }
}
