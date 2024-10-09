









import java.util.function.*;

class BoundUnboundMethRefSearch {
    public String foo(Object o) { return "foo"; }
    public static String foo(String o) { return "bar"; }

    void m() {
        Function<String, String> f = BoundUnboundMethRefSearch::foo;
    }
}
