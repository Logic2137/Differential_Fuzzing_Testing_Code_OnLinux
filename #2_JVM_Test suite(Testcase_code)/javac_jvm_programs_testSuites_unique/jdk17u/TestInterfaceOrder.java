import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class TestInterfaceOrder {

    static List<Class<?>> cInitOrder = new ArrayList<>();

    public static void main(java.lang.String[] args) {
        C c = new C();
        List<Class<?>> expectedCInitOrder = Arrays.asList(I.class, J.class, A.class, K.class, B.class, L.class, C.class);
        if (!cInitOrder.equals(expectedCInitOrder)) {
            throw new RuntimeException(String.format("Class initialization order %s not equal to expected order %s", cInitOrder, expectedCInitOrder));
        }
    }

    interface I {

        boolean v = TestInterfaceOrder.out(I.class);

        default void i() {
        }
    }

    interface J extends I {

        boolean v = TestInterfaceOrder.out(J.class);

        default void j() {
        }
    }

    static class A implements J {

        static boolean v = TestInterfaceOrder.out(A.class);
    }

    interface K extends I {

        boolean v = TestInterfaceOrder.out(K.class);

        default void k() {
        }
    }

    static class B extends A implements K {

        static boolean v = TestInterfaceOrder.out(B.class);
    }

    interface L {

        boolean v = TestInterfaceOrder.out(L.class);

        default void l() {
        }
    }

    static class C extends B implements L {

        static boolean v = TestInterfaceOrder.out(C.class);
    }

    static boolean out(Class c) {
        System.out.println("#: initializing " + c.getName());
        cInitOrder.add(c);
        return true;
    }
}
