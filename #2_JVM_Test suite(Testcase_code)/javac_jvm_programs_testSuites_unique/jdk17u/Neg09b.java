class Neg09b {

    static class Nested<X> {
    }

    void testSimple() {
        Nested<?> m2 = new Nested<>() {
        };
    }
}
