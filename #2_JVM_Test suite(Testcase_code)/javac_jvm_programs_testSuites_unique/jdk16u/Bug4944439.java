



import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Locale;

public class Bug4944439 {

    static boolean err = false;
    static DecimalFormat df;

    public static void main(String[] args) throws Exception {

        Locale defaultLoc = Locale.getDefault();
        Locale.setDefault(Locale.US);

        df = new DecimalFormat();
        String s = "-9223372036854775809";      
        check_Double(s);

        test(Long.MIN_VALUE, Long.MIN_VALUE+10);
        test(-10, 10);
        test(Long.MAX_VALUE-10, Long.MAX_VALUE-1);

        s = "9223372036854775807.00";   
        check_Long(s);
        s = "9223372036854775808";      
        check_Double(s);

        s = "-0.0";
        check_Double(s);
        s = "0.0";
        check_Long(s);

        Locale.setDefault(defaultLoc);

        if (err) {
            throw new RuntimeException("Wrong parsing with DecimalFormat");
        }
    }

    private static void test(long from, long to) throws Exception {
        for (long l = from; l <= to; l++) {
            check_Long(Long.toString(l) + ".00");
        }
    }

    private static void check_Long(String s) throws Exception {
        Number number = df.parse(s);
        if (!(number instanceof Long)) {
            err = true;
            System.err.println("Failed: DecimalFormat.parse(\"" + s +
                "\") should return a Long, but returned a " +
                number.getClass().getName());
        }

        int index = s.indexOf('.');
        Long l = Long.valueOf(s.substring(0, index));
        if (!l.equals(number)) {
            err = true;
            System.err.println("Failed: DecimalFormat.parse(" + s +
                ") should return a Long(" + l + "), but returned " + number);
        }
    }

    private static void check_Double(String s) throws Exception {
        Number number = df.parse(s);
        if (!(number instanceof Double)) {
            err = true;
            System.err.println("Failed: DecimalFormat.parse(\"" + s +
                "\") should return a Double, but returned a " +
                number.getClass().getName());
        }

        Double d = Double.valueOf(s);
        if (!d.equals(number)) {
            err = true;
            System.err.println("Failed: DecimalFormat.parse(" + s +
                ") should return a Double(" + d + "), but returned " + number);
        }
    }
}
