class T6467183a<T> {

    class A<S> {
    }

    class B extends A<Integer> {
    }

    class C<X> extends A<X> {
    }

    void cast1(B b) {
        Object o = (A<T>) b;
    }

    void cast2(B b) {
        Object o = (A<? extends Number>) b;
    }

    void cast3(A<Integer> a) {
        Object o = (C<? extends Number>) a;
    }

    void cast4(A<Integer> a) {
        Object o = (C<? extends Integer>) a;
    }
}
