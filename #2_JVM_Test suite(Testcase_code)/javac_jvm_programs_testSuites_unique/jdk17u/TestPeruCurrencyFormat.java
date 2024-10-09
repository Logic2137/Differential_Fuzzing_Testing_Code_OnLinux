import java.text.NumberFormat;
import java.util.Locale;

public class TestPeruCurrencyFormat {

    public static void main(String[] args) {
        final String expected = "S/.1,234.56";
        NumberFormat currencyFmt = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));
        String s = currencyFmt.format(1234.56);
        if (!s.equals(expected)) {
            throw new RuntimeException("Currency format for Peru failed, expected " + expected + ", got " + s);
        }
    }
}
