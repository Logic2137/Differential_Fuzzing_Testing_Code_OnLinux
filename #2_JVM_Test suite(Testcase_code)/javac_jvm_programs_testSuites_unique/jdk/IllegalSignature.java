




class IllegalSignature<X> {
    class Inner { }

    void test(IllegalSignature<?> outer) {
        outer.new Inner() { };
    }
}
