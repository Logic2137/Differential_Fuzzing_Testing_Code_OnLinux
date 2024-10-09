



import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.Map;


public class DateFormatSymbolsProviderTests {
    private static final Map<Locale, String> data = Map.of(
        Locale.forLanguageTag("en-AA"),                 "foo",
        Locale.forLanguageTag("en-US-u-rg-aazzzz"),     "foo",
        Locale.forLanguageTag("en-US-u-ca-japanese"),   "bar"
    );

    public static void main(String... args) {
        data.forEach((l, e) -> {
            DateFormatSymbols dfs = DateFormatSymbols.getInstance(l);
            String[] months = dfs.getMonths();
            System.out.printf("January string for locale %s is %s.%n", l.toString(), months[0]);
            if (!months[0].equals(e)) {
                throw new RuntimeException("DateFormatSymbols provider is not called for" + l);
            }
        });
    }
}
