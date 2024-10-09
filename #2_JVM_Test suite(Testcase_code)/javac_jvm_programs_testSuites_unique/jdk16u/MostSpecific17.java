


public class MostSpecific17 {

    public static void main(String[] args) {
        new MostSpecific17().test();
    }

    interface A<T> {}
    interface B<T> extends A<T> {}

    interface F1 { <X> A<? super X> apply(Object arg); }
    interface F2 { <Y> B<? super Y> apply(Object arg); }

    static void m1(F1 f) {
        throw new AssertionError("Less-specific method invocation.");
    }
    static void m1(F2 f) {}

    static B<Object> foo(Object in) { return null; }

    void test() {
        m1(MostSpecific17::foo);
    }
}
