



import java.text.*;
import java.util.*;

public class MultipleNumberScriptTest {

    static Locale[] locales = {
        new Locale("ar"),
        new Locale("ar", "EG"),
        new Locale("ar", "DZ"),
        Locale.forLanguageTag("ar-EG-u-nu-arab"),
        Locale.forLanguageTag("ar-EG-u-nu-latn"),
        Locale.forLanguageTag("ar-DZ-u-nu-arab"),
        Locale.forLanguageTag("ar-DZ-u-nu-latn"),
        Locale.forLanguageTag("ee"),
        Locale.forLanguageTag("ee-GH"),
        Locale.forLanguageTag("ee-GH-u-nu-latn"),
        new Locale("th", "TH", "TH"),
        Locale.forLanguageTag("th-TH"),
        Locale.forLanguageTag("th-TH-u-nu-thai"),
        Locale.forLanguageTag("th-TH-u-nu-hoge"),
    };

    
    static String[] expectedNumSystem = {
        "latn", 
        "latn", 
        "latn", 
        "arab", 
        "latn", 
        "arab", 
        "latn", 
        "latn", 
        "latn", 
        "latn", 
        "thai", 
        "latn", 
        "thai", 
        "latn", 
    };

    public static void main(String[] args) {
        int num = 123456;

        for (int i = 0; i < locales.length; i++) {
            NumberFormat nf = NumberFormat.getIntegerInstance(locales[i]);
            String formatted = nf.format(num);
            System.out.printf("%s is %s in %s locale (expected in %s script).\n",
                num, formatted, locales[i], expectedNumSystem[i]);
            if (!checkResult(formatted, expectedNumSystem[i])) {
                throw new RuntimeException("test failed. expected number system was not returned.");
            }
        }
    }

    static boolean checkResult(String formatted, String numSystem) {
        switch (numSystem) {
            case "arab":
                return formatted.charAt(0) == '\u0661';
            case "latn":
                return formatted.charAt(0) == '1';
            case "thai":
                return formatted.charAt(0) == '\u0e51';
            default:
                return false;
        }
    }
}
