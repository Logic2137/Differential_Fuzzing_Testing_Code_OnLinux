



import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.stream.IntStream;

public class FormatLocale {

    static final float src = 3.14f;
    static final List<Locale> formatLocale = List.of(Locale.US, Locale.FRANCE);
    static final List<String> expected = List.of("3.14", "3,14");

    public static void main(String [] args) {
        IntStream.range(0, formatLocale.size()).forEach(i -> {
            Locale.setDefault(Locale.Category.FORMAT, formatLocale.get(i));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new PrintStream(baos).format("%.2f", src);
            if (!baos.toString().equals(expected.get(i))) {
                throw new RuntimeException(
                    "Wrong conversion with PrintStream.format() in locale "
                    + formatLocale.get(i) +
                    ". Expected: " + expected.get(i) +
                    " Returned: " + baos.toString());
            }
        });
    }
}
