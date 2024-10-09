abstract class T8148213 {

    interface R<E> {
    }

    interface Q<T> {
    }

    interface T {

        <E> Q<E> n(R<E> r);
    }

    abstract <T> T isA(Class<T> t);

    abstract <T> S<T> w(T t);

    interface S<T> {

        S<T> t(T value);
    }

    void f(T t, Q<String> q) {
        w(t.n(isA(R.class))).t(q);
    }
}
