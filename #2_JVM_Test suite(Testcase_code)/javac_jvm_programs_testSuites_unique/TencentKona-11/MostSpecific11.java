


public class MostSpecific11 {

    public static void main(String[] args) {
        new MostSpecific11().test();
    }

    interface I { Object run(); }
    interface J { String run(); }

    void m(I arg) {
        throw new RuntimeException("Less-specific method invocation.");
    }
    void m(J arg) {}

    void test() {
        m(() -> { throw new RuntimeException(); });
    }

}
