import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

abstract class T {

    abstract <T> T r(Function<String, Supplier<T>> x);

    enum E {

        ONE
    }

    abstract <T> Supplier<T> f(Function<T, Supplier<T>> x);

    public void updateAcl(E e, Supplier<Void> v) {
        r((String t) -> {
            switch(e) {
                case ONE:
                    return f(a -> {
                        Collection<String> m = null;
                        return v;
                    });
                default:
                    return v;
            }
        });
    }
}
