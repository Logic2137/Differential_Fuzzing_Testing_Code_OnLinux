class T8176265<T> {

    static class Sup<E> {
    }

    static class Sub<E> extends Sup<E> {
    }

    void method(Sup<? super T> f) {
    }

    void method(Sub<? super T> f) {
    }

    static <Z> void m(T8176265<? extends Z> test, Sub<Z> sz) {
        test.method(sz);
    }
}
