import java.util.function.Predicate;

abstract class InferenceRegressionTest01 {

    void f(String r) {
        a(r, c(o(p(s -> s.isEmpty()))));
    }

    abstract <U> U o(U u);

    abstract <E> Predicate<E> c(Predicate<E> xs);

    abstract <S> void a(S a, Predicate<S> m);

    abstract <V> Predicate<V> p(Predicate<V> p);
}
