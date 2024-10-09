class MethodRedundantTypeargs {

    <Z> Z id(Z z) {
        return z;
    }

    void test() {
        String s = this.<String>id("");
    }
}
