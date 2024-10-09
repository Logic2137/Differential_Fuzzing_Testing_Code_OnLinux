
class MostSpecific26 {
    interface F1<T> { <X extends Iterable<T> & Runnable> Object apply(T arg); }
    interface F2 { <Y extends Iterable<Number> & Runnable> String apply(Integer arg); }

    static <T> T m1(F1<T> f) { return null; }
    static Object m1(F2 f) { return null; }

    static String foo(Object in) { return "a"; }

    void test() {
        m1(MostSpecific26::foo);
    }

}
