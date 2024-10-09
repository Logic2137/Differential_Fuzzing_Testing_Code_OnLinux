



import java.util.List;

class UnboundAndBoundByObjectTest {
    void f(List<? extends Object> x) {}
    void g(List<?> x) {}

    void h(List<String> x) {
        f((List) x);
        g((List) x);
    }
}
