



import java.util.*;
import java.util.function.*;
import java.util.logging.*;

class T8178150 {

    public static void test(List<List<String>> testList, Logger LOGGER) {
        testList.forEach(T8178150.bind(cast(LOGGER::info), iterable -> ""));
        testList.forEach(T8178150.bind_transitive(cast_transitive(LOGGER::info), iterable -> ""));
    }

    private static <T1, T2> TestProcedure<T1, T2> bind(Consumer<T2> delegate, Function<? super T1, T2> function) {
        return null;
    }

    private static <C> Consumer<C> cast(Consumer<C> consumer) {
        return consumer;
    }

    private static <T1, T2, U extends T2> TestProcedure<T1, T2> bind_transitive(Consumer<U> delegate, Function<? super T1, T2> function) {
        return null;
    }

    private static <C> C cast_transitive(C consumer) {
        return consumer;
    }

    private static final class TestProcedure<X1, X2> implements Consumer<X1> {
        @Override
        public void accept(final X1 t1) { }
    }
}
