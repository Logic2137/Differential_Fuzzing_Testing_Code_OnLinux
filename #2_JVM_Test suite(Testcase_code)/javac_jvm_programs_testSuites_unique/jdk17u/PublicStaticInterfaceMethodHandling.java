class TestClass implements InterfaceWithStaticAndDefaultMethods {
}

interface InterfaceWithStaticAndDefaultMethods {

    public static String get() {
        return "Hello from StaticMethodInInterface.get()";
    }

    default void default_method() {
        System.out.println("Default method FunctionalInterface:default_method()");
    }
}

public class PublicStaticInterfaceMethodHandling {

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        tc.default_method();
    }
}
