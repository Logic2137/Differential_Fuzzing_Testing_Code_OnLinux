import java.io.File;
import java.io.FileInputStream;
import java.util.Currency;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class CurrencyFormat {

    public static void main(String[] args) throws Exception {
        testFormatting();
        testSymbols();
    }

    static void testFormatting() {
        boolean failed = false;
        Locale[] locales = { Locale.US, Locale.JAPAN, Locale.GERMANY, Locale.ITALY, new Locale("it", "IT", "EURO") };
        Currency[] currencies = { null, Currency.getInstance("USD"), Currency.getInstance("JPY"), Currency.getInstance("DEM"), Currency.getInstance("EUR") };
        String[][] expecteds = { { "$1,234.56", "$1,234.56", "JPY1,235", "DEM1,234.56", "EUR1,234.56" }, { "\uFFE51,235", "USD1,234.56", "\uFFE51,235", "DEM1,234.56", "EUR1,234.56" }, { "1.234,56 \u20AC", "1.234,56 USD", "1.235 JPY", "1.234,56 DM", "1.234,56 \u20AC" }, { "\u20AC 1.234,56", "USD 1.234,56", "JPY 1.235", "DEM 1.234,56", "\u20AC 1.234,56" }, { "\u20AC 1.234,56", "USD 1.234,56", "JPY 1.235", "DEM 1.234,56", "\u20AC 1.234,56" } };
        for (int i = 0; i < locales.length; i++) {
            Locale locale = locales[i];
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);
            for (int j = 0; j < currencies.length; j++) {
                Currency currency = currencies[j];
                String expected = expecteds[i][j];
                if (currency != null) {
                    format.setCurrency(currency);
                    int digits = currency.getDefaultFractionDigits();
                    format.setMinimumFractionDigits(digits);
                    format.setMaximumFractionDigits(digits);
                }
                String result = format.format(1234.56);
                if (!result.equals(expected)) {
                    failed = true;
                    System.out.println("FAIL: Locale " + locale + (currency == null ? ", default currency" : (", currency: " + currency)) + ", expected: " + expected + ", actual: " + result);
                }
            }
        }
        if (failed) {
            throw new RuntimeException();
        }
    }

    static void testSymbols() throws Exception {
        FileInputStream stream = new FileInputStream(new File(System.getProperty("test.src", "."), "CurrencySymbols.properties"));
        Properties props = new Properties();
        props.load(stream);
        SimpleDateFormat format = null;
        Locale[] locales = NumberFormat.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {
            Locale locale = locales[i];
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
            String result = symbols.getCurrencySymbol();
            String expected = (String) props.get(locale.toString());
            if (expected == null) {
                System.out.println("Warning: No expected currency symbol defined for locale " + locale);
            } else {
                if (expected.contains(";")) {
                    StringTokenizer tokens = new StringTokenizer(expected, ";");
                    int tokensCount = tokens.countTokens();
                    if (tokensCount == 3) {
                        expected = tokens.nextToken();
                        if (format == null) {
                            format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
                            format.setTimeZone(TimeZone.getTimeZone("GMT"));
                            format.setLenient(false);
                        }
                        if (format.parse(tokens.nextToken()).getTime() < System.currentTimeMillis()) {
                            expected = tokens.nextToken();
                        }
                    }
                }
                if (!expected.equals(result)) {
                    throw new RuntimeException("Wrong currency symbol for locale " + locale + ", expected: " + expected + ", got: " + result);
                }
            }
        }
    }
}
