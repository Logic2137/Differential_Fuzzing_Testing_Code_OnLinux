import java.util.List;

class TestArr {

    <Z> List<? super Z[]> m(List<Z> z) {
        return null;
    }

    void test(List<? extends Number> l) {
        var v = m(l);
    }
}
