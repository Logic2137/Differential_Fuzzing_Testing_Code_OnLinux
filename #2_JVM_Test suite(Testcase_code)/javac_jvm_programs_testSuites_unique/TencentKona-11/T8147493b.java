



abstract class T8147493b {

    abstract <A> A f(A t);
    abstract <B> Class<B> g(Class<B> x, String y);
    abstract <C> void h(C t);

    void m(Class raw) {
      h(g(raw, f(null)));
    }
}
