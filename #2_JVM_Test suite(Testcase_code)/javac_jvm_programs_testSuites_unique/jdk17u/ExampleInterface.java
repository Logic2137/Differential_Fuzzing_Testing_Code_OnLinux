
package jdk.deprcases.members;

public interface ExampleInterface {

    @Deprecated
    static final int DEP_FIELD1 = 1;

    @Deprecated
    static final int DEP_FIELD2 = 2;

    @Deprecated
    void interfaceMethod1();

    @Deprecated
    void interfaceMethod2();

    @Deprecated
    default void defaultMethod() {
    }

    @Deprecated
    static void staticmethod2() {
    }
}
