import java.util.function.Function;

public class ImplicitEnclosingInstanceTest {

    static String cookie = "deadbeef";

    static Object f(Function<String, Object> f) {
        return f.apply("feed");
    }

    class S {

        S(Object s) {
            cookie += "face";
        }
    }

    class A {

        A(String s) {
            cookie = s;
        }
    }

    class B extends S {

        B() {
            super(f(A::new));
        }
    }

    public static void main(String[] args) {
        new ImplicitEnclosingInstanceTest().new B();
        if (!cookie.equals("feedface"))
            throw new AssertionError("Incorrect cookie!");
    }
}
