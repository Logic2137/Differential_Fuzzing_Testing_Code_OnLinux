



import java.util.Optional;
import java.util.stream.Stream;

public abstract class MethodRefStuck2 {

    abstract void f(long c);

    interface I {
        I g(String o);
    }

    private void test(Stream<I> is, Optional<String> o) {
        f(
                is.map(
                                i -> {
                                    o.ifPresent(i::g);
                                    return null;
                                })
                        .count());
    }
}
