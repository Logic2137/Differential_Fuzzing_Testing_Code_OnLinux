



abstract class T8168134 {
    interface W<A> {}
    abstract <B> B f(W<B> e);
    abstract <C> C g(C b, long i);

    void h(W<Long> i) {
        g("", f(i));
    }
}
