

import java.io.PrintStream;


public class CustomLoader extends ClassLoader {
    private static PrintStream out = System.out;
    public  static ClassLoader INSTANCE;

    public CustomLoader(ClassLoader classLoader) {
        super("CustomSystemLoader", classLoader);
        assert INSTANCE == null;
        INSTANCE = this;

        
        
        testEnumValueOf();
    }

    static void testEnumValueOf() {
        TestEnum e = java.lang.Enum.valueOf(TestEnum.class, "C1");
        if (e != TestEnum.C1) {
            throw new RuntimeException("Expected: " + TestEnum.C1 + " got: " + e);
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        out.println("CustomLoader: loading class: " + name);
        return super.loadClass(name);
    }

    static enum TestEnum {
        C1, C2, C3
    }
}
