



import java.lang.module.ModuleDescriptor;

public class PublicConstructor {
    public static void main(String... args) {
        testConstructor();
        testConstructorWithModule();
    }

    static void testConstructor() {
        StackTraceElement ste = new StackTraceElement("com.acme.Widget",
                                                      "frobnicate",
                                                      "Widget.java", 42);
        if (!(ste.getClassName().equals("com.acme.Widget")  &&
                ste.getFileName().equals("Widget.java") &&
                ste.getMethodName().equals("frobnicate") &&
                ste.getLineNumber() == 42))
            throw new RuntimeException("1");

        if (ste.isNativeMethod())
            throw new RuntimeException("2");

        assertEquals(ste.toString(),
                     "com.acme.Widget.frobnicate(Widget.java:42)");

        StackTraceElement ste1 = new StackTraceElement("com.acme.Widget",
                                                       "frobnicate",
                                                       "Widget.java",
                                                       -2);
        if (!ste1.isNativeMethod())
            throw new RuntimeException("3");

        assertEquals(ste1.toString(),
                     "com.acme.Widget.frobnicate(Native Method)");
    }

    static void testConstructorWithModule() {
        StackTraceElement ste = new StackTraceElement("app",
                                                      "jdk.module",
                                                      "9.0",
                                                      "com.acme.Widget",
                                                      "frobnicate",
                                                      "Widget.java",
                                                      42);
        if (!(ste.getClassName().equals("com.acme.Widget")  &&
                ste.getModuleName().equals("jdk.module") &&
                ste.getModuleVersion().equals("9.0") &&
                ste.getClassLoaderName().equals("app") &&
                ste.getFileName().equals("Widget.java") &&
                ste.getMethodName().equals("frobnicate") &&
                ste.getLineNumber() == 42))
            throw new RuntimeException("3");

        if (ste.isNativeMethod())
            throw new RuntimeException("4");

        assertEquals(ste.toString(),
                     "app/jdk.module@9.0/com.acme.Widget.frobnicate(Widget.java:42)");
    }

    static void assertEquals(String s, String expected) {
        if (!s.equals(expected)) {
            throw new RuntimeException("Expected: " + expected + " but found: " + s);
        }
    }
}
