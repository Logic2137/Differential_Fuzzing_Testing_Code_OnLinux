





public class TestNullCheck {

    int f;

    static int test1(TestNullCheck o) {
        return o.f;
    }

    static TestNullCheck static_obj = new TestNullCheck();

    static int test2() {
        return static_obj.f;
    }

    static public void main(String[] args) {
        TestNullCheck o = new TestNullCheck();
        for (int i = 0; i < 20000; i++) {
            test1(o);
            test2();
        }
        try {
            test1(null);
        } catch (NullPointerException npe) {}
        static_obj = null;
        try {
            test2();
        } catch (NullPointerException npe) {}
    }
}
