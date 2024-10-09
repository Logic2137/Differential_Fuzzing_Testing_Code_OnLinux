


class T8063054b {
    void test(Box<? extends Box<Number>> b) {
        Number n = b.<Number>map(Box::get).get();
    }

    interface Func<S,T> { T apply(S arg); }

    interface Box<T> {
        T get();
        <R> Box<R> map(Func<T,R> f);
    }
}
