
class MostSpecific24 {
    interface F1<T> { <X> Object apply(Class<T> arg); }
    interface F2 { <Y> String apply(Class<Y> arg); }

    static <T> T m1(F1<T> f) { return null; }
    static Object m1(F2 f) { return null; }

    static String foo(Object in) { return "a"; }

    void test() {
        m1(MostSpecific24::foo);
    }

}
