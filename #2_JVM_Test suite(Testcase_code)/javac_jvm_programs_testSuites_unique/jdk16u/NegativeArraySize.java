



import java.util.regex.Pattern;

public class NegativeArraySize {
    public static void main(String[] args) {
        try {
            Pattern.compile("\\Q" + "a".repeat(42 + Integer.MAX_VALUE / 3));
            throw new AssertionError("expected to throw");
        } catch (OutOfMemoryError expected) {
        }
    }
}
