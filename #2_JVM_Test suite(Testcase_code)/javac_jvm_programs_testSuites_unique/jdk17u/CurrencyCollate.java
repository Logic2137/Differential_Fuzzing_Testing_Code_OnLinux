import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.text.CollationKey;

public class CurrencyCollate {

    static Collator myCollation = Collator.getInstance(Locale.US);

    public static void main(String[] args) {
        String[] DATA = { "\u20AC", 
        ">", 
        "$", "\u20AC", 
        "<", 
        "\u00A3", "\u00a4", 
        "<", 
        "\u0e3f", "\u0e3f", 
        "<", 
        "\u00a2", "\u00a2", 
        "<", 
        "\u20a1", "\u20a1", 
        "<", 
        "\u20a2", "\u20a2", 
        "<", 
        "\u0024", "\u0024", 
        "<", 
        "\u20ab", "\u20ab", 
        "<", 
        "\u20a3", "\u20a3", 
        "<", 
        "\u20a4", "\u20a4", 
        "<", 
        "\u20a5", "\u20a5", 
        "<", 
        "\u20a6", "\u20a6", 
        "<", 
        "\u20a7", "\u20a7", 
        "<", 
        "\u00a3", "\u00a3", 
        "<", 
        "\u20a8", "\u20a8", 
        "<", 
        "\u20aa", "\u20aa", "<", "\u20a9", "\u20a9", "<", "\u00a5" };
        for (int i = 0; i < DATA.length; i += 3) {
            int expected = DATA[i + 1].equals(">") ? 1 : (DATA[i + 1].equals("<") ? -1 : 0);
            int actual = myCollation.compare(DATA[i], DATA[i + 2]);
            if (actual != expected) {
                throw new RuntimeException("Collation of " + DATA[i] + " vs. " + DATA[i + 2] + " yields " + actual + "; expected " + expected);
            }
        }
        System.out.println("Ok");
    }
}
