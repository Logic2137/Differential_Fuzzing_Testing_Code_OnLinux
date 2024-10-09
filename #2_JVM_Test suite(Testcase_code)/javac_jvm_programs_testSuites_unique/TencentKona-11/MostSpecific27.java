


public class MostSpecific27 {
    public static void main(String[] args) {
        new MostSpecific27().test();
    }

    interface F1<T> { <X extends Iterable<T> & Runnable> Object apply(T arg); }
    interface F2 { <Y extends Iterable<Number> & Runnable> String apply(Number arg); }

    static <T> T m1(F1<T> f) {
        throw new AssertionError("Less-specific method invocation.");
    }
    static Object m1(F2 f) { return null; }

    static String foo(Object in) { return "a"; }

    void test() {
        m1(MostSpecific27::foo);
    }

}
