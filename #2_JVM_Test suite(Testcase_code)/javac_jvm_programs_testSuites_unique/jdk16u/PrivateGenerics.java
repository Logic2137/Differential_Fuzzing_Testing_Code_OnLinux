



public class PrivateGenerics {

    interface I<T> {
        private T foo() { return null; };
        default void m(T t) {
            T t1 = t;
            T t2 = foo();
        }
    }

    interface J {
        private <M> M foo() { return null; }
        default <N> void m(N n) {
            N n1 = n;
            N n2 = foo();
        }
    }

    public static void main(String[] args) {
        I<String> i = new I<>() {};
        i.m("string");
        String s = i.foo();

        J j = new J() {};
        j.m("string");
        s = j.foo();
    }
}
