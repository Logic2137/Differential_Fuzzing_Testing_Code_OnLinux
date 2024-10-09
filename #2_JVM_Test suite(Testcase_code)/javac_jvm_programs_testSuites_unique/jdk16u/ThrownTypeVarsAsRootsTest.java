



import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.util.function.Function;
import java.util.Optional;
import java.util.function.BiFunction;

class ThrownTypeVarsAsRootsTest {
    void foo() {
        try {
            BiFunction<String, String, Optional<String>> function = rethrowBiFunction((x, y) -> {
                    return Optional.of(x).map(rethrowFunction(z -> createZ(z)));
            });
        } catch (Exception ex) {}
    }

    public static String createZ(String x) throws UnsupportedEncodingException, BindException {
        return null;
    }

    public static <T, R, E extends Exception> Function<T, R> rethrowFunction(ThrowingFunction<T, R, E> function) throws E {
        return null;
    }

    public static <T, U, R, E extends Exception> BiFunction<T, U, R> rethrowBiFunction(
            ThrowingBiFunction<T, U, R, E> function) throws E {
        return null;
    }

    public interface ThrowingBiFunction<T, U, R, E extends Exception> {
        R apply(T t, U u) throws E;
    }

    public interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }
}
