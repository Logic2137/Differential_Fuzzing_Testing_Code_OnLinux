public class MostSpecific22 {

    public static void main(String[] args) {
        new MostSpecific22().test();
    }

    interface F1<T> {

        <X extends T> Object apply(T arg);
    }

    interface F2 {

        <Y extends Number> String apply(Number arg);
    }

    static <T> T m1(F1<T> f) {
        throw new AssertionError("Less-specific method invocation.");
    }

    static Object m1(F2 f) {
        return null;
    }

    static String foo(Object in) {
        return "a";
    }

    void test() {
        m1(MostSpecific22::foo);
    }
}
