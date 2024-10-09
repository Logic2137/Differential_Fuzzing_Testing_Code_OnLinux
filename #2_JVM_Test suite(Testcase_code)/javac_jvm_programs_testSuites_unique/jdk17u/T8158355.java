import java.util.List;

class T8158355 {

    <Z> List<Z> m() {
        return null;
    }

    void test() {
        List<String> ls = m();
    }
}
