
class MostSpecific32 {

    interface A<T> {}
    interface B<T> extends A<T> {}

    interface F1<S> { A<S> apply(); }
    interface F2<S> { B<S> apply(); }

    static void m1(F1<? extends Number> f1) {}
    static void m1(F2<? extends Number> f2) {}

    void test() {
        m1(() -> null); 
    }

}
