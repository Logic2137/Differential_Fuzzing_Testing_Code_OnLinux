abstract class T8147546b {

    interface I<O> {

        void t(O clazz);
    }

    abstract <B> B b(Class<B> t);

    abstract <C> C c(C a);

    abstract Object d(Runnable r);

    Object f(Iterable<Object> xs) {
        return c(d(() -> {
            class Foo {

                <T> void g(Class<T> clazz, I<T> i) {
                    i.t(b(clazz));
                }
            }
        }));
    }
}
