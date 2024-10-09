
class MostSpecific25 {
    interface F1<T> { <X> T apply(Integer arg); }
    interface F2 { <Y> Class<? super Y> apply(Integer arg); }

    static <T> T m1(F1<T> f) { return null; }
    static Object m1(F2 f) { return null; }

    static Class<Object> foo(Object in) { return Object.class; }

    void test() {
        m1(MostSpecific25::foo);
    }

}
