import java.lang.reflect.Method;

public class TestMain {

    public static void main(String[] args) throws Exception {
        ModuleLayer boot = ModuleLayer.boot();
        Module m1 = boot.findModule("m1").get();
        Module m2 = boot.findModule("m2").get();
        findClass(m1, "p1.A");
        findClass(m1, "p1.internal.B");
        findClass(m2, "p2.C");
        ClassLoader loader = TestMain.class.getClassLoader();
        findClass(loader.getUnnamedModule(), "TestDriver");
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
