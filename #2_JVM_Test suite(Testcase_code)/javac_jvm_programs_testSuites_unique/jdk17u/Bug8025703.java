import java.util.*;
import java.util.Locale.LanguageRange;

public class Bug8025703 {

    public static void main(String[] args) {
        boolean err = false;
        String[][] mappings = { { "ilw", "gal" }, { "meg", "cir" }, { "pcr", "adx" }, { "xia", "acn" }, { "yos", "zom" } };
        for (int i = 0; i < mappings.length; i++) {
            List<LanguageRange> got = LanguageRange.parse(mappings[i][0]);
            ArrayList<LanguageRange> expected = new ArrayList<>();
            expected.add(new LanguageRange(mappings[i][0], 1.0));
            expected.add(new LanguageRange(mappings[i][1], 1.0));
            if (!expected.equals(got)) {
                err = true;
                System.err.println("Incorrect language ranges. ");
                for (LanguageRange lr : expected) {
                    System.err.println("  Expected: range=" + lr.getRange() + ", weight=" + lr.getWeight());
                }
                for (LanguageRange lr : got) {
                    System.err.println("  Got:      range=" + lr.getRange() + ", weight=" + lr.getWeight());
                }
            }
        }
        if (err) {
            throw new RuntimeException("Failed.");
        }
    }
}
