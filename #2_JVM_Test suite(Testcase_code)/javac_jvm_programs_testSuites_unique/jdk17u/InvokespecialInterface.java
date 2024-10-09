import java.util.function.*;
import java.util.*;

public class InvokespecialInterface {

    interface I {

        default void imethod() {
            System.out.println("I::imethod");
        }
    }

    static class C implements I {

        public void foo() {
            I.super.imethod();
        }

        public void bar() {
            I i = this;
            i.imethod();
        }

        public void doSomeInvokedynamic() {
            String str = "world";
            Supplier<String> foo = () -> "hello, " + str;
            String res = foo.get();
            System.out.println(res);
        }
    }

    public static void main(java.lang.String[] unused) {
        C c = new C();
        c.foo();
        c.bar();
        c.doSomeInvokedynamic();
    }
}
