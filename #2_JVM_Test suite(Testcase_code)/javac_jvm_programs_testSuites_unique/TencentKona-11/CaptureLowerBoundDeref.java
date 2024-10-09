

class CaptureLowerBoundDeref {

    interface Wrapper<T> {
        I<T> get();
    }

    interface I<T> {}

    interface K<T> { void take(T arg); }

    <T> K<T> m(I<? extends T> arg) { return null; }

    void test(Wrapper<?> w) {
        m(w.get()).take(new Object());
    }
}
