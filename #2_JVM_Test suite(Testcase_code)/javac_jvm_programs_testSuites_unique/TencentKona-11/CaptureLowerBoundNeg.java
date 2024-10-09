

public class CaptureLowerBoundNeg {

    static class D<T> {
        void take(T arg) {}
        static <T> D<T> make(Class<? extends T> c) { return new D<T>(); }
    }

    void test(Object o) {
        D.make(o.getClass()).take(o);
    }

}
