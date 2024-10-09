import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class IntersectionTypeReceiverTest {

    public static void main(String[] args) {
        String output = valueOfKey(Size.class, LocalDate.now()).toString();
        if (!"Optional[S]".equals(output))
            throw new AssertionError("Unexpected output: " + output);
    }

    enum Size implements Supplier<LocalDate> {

        S, M, L;

        @Override
        public LocalDate get() {
            return LocalDate.now();
        }
    }

    public static <K, T extends Enum<T> & Supplier<K>> Optional<T> valueOfKey(Class<T> enumType, K key) {
        return valueOf(enumType, key, T::get);
    }

    public static <K, T extends Enum<T>> Optional<T> valueOf(Class<T> enumType, K key, Function<T, K> keyExtractor) {
        return EnumSet.allOf(enumType).stream().filter(t -> Objects.equals(keyExtractor.apply(t), key)).findFirst();
    }
}
