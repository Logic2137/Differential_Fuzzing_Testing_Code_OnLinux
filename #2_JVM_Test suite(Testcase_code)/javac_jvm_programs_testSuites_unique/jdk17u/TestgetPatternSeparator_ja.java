import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TestgetPatternSeparator_ja {

    public static void main(String[] argv) throws Exception {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.JAPAN);
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        if (dfs.getPatternSeparator() != ';') {
            throw new Exception("DecimalFormatSymbols.getPatternSeparator doesn't return ';' in ja locale");
        }
    }
}
