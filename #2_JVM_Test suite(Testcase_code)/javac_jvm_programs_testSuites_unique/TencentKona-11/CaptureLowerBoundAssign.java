



class CaptureLowerBoundAssign {

    static class C<T> {}

    <T> C<T> m(C<? extends T> x) { return null; }

    void test(C<? extends Number> arg) {
        C<Number> c = m(arg);
    }

}
