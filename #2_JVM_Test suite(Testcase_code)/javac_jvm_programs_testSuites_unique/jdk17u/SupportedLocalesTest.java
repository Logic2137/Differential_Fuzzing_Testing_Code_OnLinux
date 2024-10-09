import java.util.*;
import java.util.spi.LocaleServiceProvider;

public class SupportedLocalesTest {

    private static final Locale[] GOOD_ONES = { Locale.forLanguageTag("ja-JP-x-lvariant-JP"), Locale.forLanguageTag("th-TH-x-lvariant-TH"), Locale.US };

    private static final Locale[] BAD_ONES = { Locale.GERMAN, Locale.GERMANY, Locale.CANADA, Locale.TAIWAN };

    public static void main(String[] args) {
        LocaleServiceProvider provider = new TestLocaleServiceProvider();
        List<Locale> locs = new ArrayList<>();
        locs.addAll(Arrays.asList(GOOD_ONES));
        locs.addAll(Arrays.asList(provider.getAvailableLocales()));
        for (Locale locale : locs) {
            if (!provider.isSupportedLocale(locale)) {
                throw new RuntimeException(locale + " is NOT supported.");
            }
        }
        for (Locale locale : BAD_ONES) {
            if (provider.isSupportedLocale(locale)) {
                throw new RuntimeException(locale + " should NOT be supported.");
            }
        }
    }

    private static class TestLocaleServiceProvider extends LocaleServiceProvider {

        private static final Locale[] locales = { new Locale("ja", "JP", "JP"), new Locale("th", "TH", "TH"), Locale.forLanguageTag("en-US-u-ca-buddhist") };

        @Override
        public Locale[] getAvailableLocales() {
            return locales.clone();
        }
    }
}
