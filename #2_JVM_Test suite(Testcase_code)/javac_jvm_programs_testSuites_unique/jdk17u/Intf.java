
package pkg1;

public interface Intf {

    void visibleInterfaceMethod();

    void invisibleInterfaceMethod();

    default void visibleDefaultMethod() {
    }

    default void invisibleDefaultMethod() {
    }
}
