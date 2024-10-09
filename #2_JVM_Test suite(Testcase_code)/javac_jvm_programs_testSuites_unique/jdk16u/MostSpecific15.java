


public class MostSpecific15 {
    public static void main(String[] args) {
        new MostSpecific15().test();
    }

    interface F1 { <X> Object apply(X arg); }
    interface F2 { <Y> String apply(Y arg); }

    static void m1(F1 f) {
        throw new AssertionError("Less-specific method invocation.");
    }
    static void m1(F2 f) {}

    static String foo(Object in) { return "a"; }

    void test() {
        m1(MostSpecific15::foo);
    }
}
