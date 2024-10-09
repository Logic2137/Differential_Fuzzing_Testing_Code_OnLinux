


abstract class T8147546a {

    interface I<O> { void t(O clazz); }
    abstract <A> I<A> a(Class<A> clazz);
    abstract <B> B b(Class<B> t);
    abstract <C> C c(C a);

    Object f(Iterable<Object> xs) {
        return c(c(new Object() {
            <T> void g(Class<T> clazz) {
                a(clazz).t(b(clazz));
            }
        }));
    }
}
