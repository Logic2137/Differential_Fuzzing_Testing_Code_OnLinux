



import java.util.Map;

class T8177097a {
    interface X<O> {
        Map<?, O> apply();
    }

    <O> void go(X<O> x) { }

    static <I> Map<?, Integer> a() {
        return null;
    }

    void test() {
        go(T8177097a::a);
    }
}
