import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Bug6609740 {

    public static void main(String[] args) {
        double dNumber = -3456.349347;
        String fOutput = "(3,456.35)";
        String[] validCases = { "#,##0.0#;(#,##0.0#)", "#,##0.0#;(#)", "#,##0.0#;(#,##0)" };
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        for (String pattern : validCases) {
            formatOnPattern(nf, pattern, dNumber, fOutput);
        }
        String parseString = "(3,456.35)";
        Number pOutput = -3456.35;
        for (String pattern : validCases) {
            parseOnPattern(nf, pattern, parseString, pOutput);
        }
        String[] invalidParseCases = { "#,##0.0#;0", "#,##0.0#;()" };
        for (String pattern : invalidParseCases) {
            if (nf instanceof DecimalFormat) {
                ((DecimalFormat) nf).applyPattern(pattern);
            }
            try {
                nf.parse(parseString);
            } catch (ParseException ex) {
                continue;
            }
            throw new RuntimeException("[FAILED: Should throw" + " ParseException for pattern: " + pattern + " and input: " + parseString + "]");
        }
        String[] invalidPatterns = { ";(#,##0.0#)", "#,##0.0#0;(#)", "#,##0.0.#", "#,##0%%", ".#,##0" };
        for (String pattern : invalidPatterns) {
            if (nf instanceof DecimalFormat) {
                try {
                    ((DecimalFormat) nf).applyPattern(pattern);
                } catch (IllegalArgumentException ex) {
                    continue;
                }
                throw new RuntimeException("[FAILED: Should throw" + " IllegalArgumentException for invalid pattern: " + pattern + "]");
            }
        }
    }

    private static void formatOnPattern(NumberFormat nf, String pattern, double number, String expected) {
        if (nf instanceof DecimalFormat) {
            ((DecimalFormat) nf).applyPattern(pattern);
        }
        String formatted = nf.format(number);
        if (!formatted.equals(expected)) {
            throw new RuntimeException("[FAILED: Unable to format the number" + " based on the pattern: '" + pattern + "', Expected : '" + expected + "', Found: '" + formatted + "']");
        }
    }

    private static void parseOnPattern(NumberFormat nf, String pattern, String parseString, Number expected) {
        if (nf instanceof DecimalFormat) {
            ((DecimalFormat) nf).applyPattern(pattern);
        }
        try {
            Number output = nf.parse(parseString);
            if (expected.doubleValue() != output.doubleValue()) {
                throw new RuntimeException("[FAILED: Unable to parse the number" + " based on the pattern: '" + pattern + "', Expected : '" + expected + "', Found: '" + output + "']");
            }
        } catch (ParseException ex) {
            throw new RuntimeException("[FAILED: Unable to parse the pattern:" + " '" + pattern + "']", ex);
        }
    }
}
