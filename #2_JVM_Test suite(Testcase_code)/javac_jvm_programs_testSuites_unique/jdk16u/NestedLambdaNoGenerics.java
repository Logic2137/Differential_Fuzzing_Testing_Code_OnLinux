

 
import java.util.concurrent.Callable;

class NestedLambdaNoGenerics {
    void test() {
        m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null,
                () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null,
                () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null,
                () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null,
                () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null, () -> m(null,
                (Callable<String>)null)))))))))))))))))))))))))))))));
    }
    static class A0 { }
    static class A1 { }
    static class A2 { }
    static class A3 { }
    static class A4 { }
    String m(A0 t, Callable<A0> ct) { return ""; }
    String m(A1 t, Callable<A1> ct) { return ""; }
    String m(A2 t, Callable<A2> ct) { return ""; }
    String m(A3 t, Callable<A3> ct) { return ""; }
    String m(A4 t, Callable<A4> ct) { return ""; }
    String m(Object o, Callable<String> co) { return ""; }
}
