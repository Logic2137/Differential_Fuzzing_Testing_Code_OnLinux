
package jdk.deprcases.members;

public class ExampleClass {

    public ExampleClass() {
    }

    @Deprecated
    public ExampleClass(boolean deprecatedConstructor) {
    }

    @Deprecated
    public void method1() {
    }

    @Deprecated
    public void method2() {
    }

    @Deprecated
    public int field1 = 0;

    @Deprecated
    public int field2 = 0;

    @Deprecated
    public static void staticmethod1() {
    }

    @Deprecated
    public static int staticfield3 = 0;
}
