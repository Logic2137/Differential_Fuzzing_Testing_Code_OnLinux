class CaptureUpperBoundDeref {

    interface Wrapper<T> {

        I<T> get();
    }

    interface I<T> {
    }

    <T> T m(I<? super T> arg) {
        return null;
    }

    void test(Wrapper<? super String> w) {
        m(w.get()).substring(0);
    }
}
