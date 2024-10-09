class Neg09a {

    class Member<X> {
    }

    void testSimple() {
        Member<?> m1 = new Member<>() {
        };
    }
}
