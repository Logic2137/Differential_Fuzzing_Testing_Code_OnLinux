class T8203436a<X> {

    class Inner {
    }

    void test(T8203436a<?> outer) {
        outer.new Inner() {
        };
    }
}
