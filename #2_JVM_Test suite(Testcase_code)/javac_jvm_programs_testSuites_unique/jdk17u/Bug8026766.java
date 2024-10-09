import java.util.Locale.LanguageRange;

public class Bug8026766 {

    public static void main(String[] args) {
        LanguageRange lr1 = new LanguageRange("ja", 1.0);
        LanguageRange lr2 = new LanguageRange("fr", 0.0);
        if (!lr1.toString().equals("ja") || !lr2.toString().equals("fr;q=0.0")) {
            throw new RuntimeException("LanguageRange.toString() returned an unexpected result.");
        }
    }
}
