


import java.util.function.Function;

class T8156954 {

    <T, R> void m1(Function<R, T> f1, Function<T, R> f2, R r) { }
    <T, R> void m2(Function<T, R> f1, Function<R, T> f2, R r) { }

    void m(Integer intValue) {
        m1(o -> o, o -> o , intValue);
        m2(o -> o, o -> o , intValue);
    }
}
