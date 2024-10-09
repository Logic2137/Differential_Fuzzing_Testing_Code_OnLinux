public class MostSpecific18 {

    public static void main(String[] args) {
        new MostSpecific18().test();
    }

    interface F1 {

        <X extends Number> Object apply(X arg);
    }

    interface F2 {

        <Y extends Number> String apply(Y arg);
    }

    static void m1(F1 f) {
        throw new AssertionError("Less-specific method invocation.");
    }

    static void m1(F2 f) {
    }

    static String foo(Object in) {
        return "a";
    }

    void test() {
        m1(MostSpecific18::foo);
    }
}
