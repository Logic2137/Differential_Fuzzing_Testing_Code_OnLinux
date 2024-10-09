

class Neg09c {
    class Member<X> {}

    void testQualified() {
        Member<?> m1 = this.new Member<>() {};
    }
}
