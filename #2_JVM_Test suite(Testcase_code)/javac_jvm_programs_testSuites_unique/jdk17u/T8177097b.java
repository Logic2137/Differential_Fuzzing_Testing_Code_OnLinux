import java.util.Map;

class T8177097b {

    interface X<O> {

        O apply(Class<Map<Integer, ?>> m2);
    }

    <O> void go(X<O> x) {
    }

    static <I> I a(Class<I> c) {
        return null;
    }

    void test() {
        go(T8177097b::a);
    }
}
