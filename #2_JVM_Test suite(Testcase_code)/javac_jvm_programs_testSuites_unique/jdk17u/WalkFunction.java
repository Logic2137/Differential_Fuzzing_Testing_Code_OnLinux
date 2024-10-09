import java.lang.StackWalker.StackFrame;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class WalkFunction {

    private static final StackWalker walker = StackWalker.getInstance();

    public static void main(String... args) throws Exception {
        testFunctions();
        testWildcards();
        walker.walk(counter());
        walker.walk(wildCounter());
    }

    private static void testFunctions() {
        walker.walk(Stream::count);
        try {
            walker.walk(null);
            throw new RuntimeException("NPE expected");
        } catch (NullPointerException e) {
        }
        Optional<StackFrame> result = walker.walk(WalkFunction::reduce);
        if (!result.get().getClassName().equals(WalkFunction.class.getName())) {
            throw new RuntimeException(result.get() + " expected: " + WalkFunction.class.getName());
        }
    }

    static Optional<StackFrame> reduce(Stream<StackFrame> stream) {
        return stream.reduce((r, f) -> r.getClassName().compareTo(f.getClassName()) > 0 ? f : r);
    }

    private static void testWildcards() {
        Function<? super Stream<? extends StackFrame>, Void> f1 = WalkFunction::function;
        Function<? super Stream<? super StackFrame>, Void> f2 = WalkFunction::function;
        Function<? super Stream<StackFrame>, Void> f3 = WalkFunction::function;
        Function<Stream<? extends StackFrame>, Void> f4 = WalkFunction::function;
        Function<Stream<? super StackFrame>, Void> f5 = WalkFunction::function;
        Function<Stream<StackFrame>, Void> f6 = WalkFunction::function;
        walker.walk(f1);
        walker.walk(f2);
        walker.walk(f3);
        walker.walk(f4);
        walker.walk(f5);
        walker.walk(f6);
    }

    private static Void function(Stream<?> s) {
        return null;
    }

    private static Function<Stream<?>, Long> wildCounter() {
        return Stream::count;
    }

    private static <T> Function<Stream<T>, Long> counter() {
        return Stream::count;
    }
}
