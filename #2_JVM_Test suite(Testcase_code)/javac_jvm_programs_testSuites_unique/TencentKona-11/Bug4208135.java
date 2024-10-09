



import java.math.*;
import java.text.*;
import java.util.*;

public class Bug4208135 {

    static DecimalFormat df;

    static boolean err = false;

    static public void main(String[] args){

        Locale defaultLoc = Locale.getDefault();
        Locale.setDefault(Locale.US);

        df = new DecimalFormat();

        df.applyPattern("0.#E0");

        df.setDecimalSeparatorAlwaysShown(true);
        checkFormat(0.0, "0.E0");
        checkFormat(10.0, "1.E1");
        checkFormat(1000.0, "1.E3");
        checkFormat(0L, "0.E0");
        checkFormat(10L, "1.E1");
        checkFormat(1000L, "1.E3");
        checkFormat(new BigDecimal("0.0"), "0.E0");
        checkFormat(new BigDecimal("10.0"), "1.E1");
        checkFormat(new BigDecimal("1000.0"), "1.E3");
        checkFormat(new BigInteger("00"), "0.E0");
        checkFormat(new BigInteger("10"), "1.E1");
        checkFormat(new BigInteger("1000"), "1.E3");

        df.setDecimalSeparatorAlwaysShown(false);
        checkFormat(0.0, "0E0");
        checkFormat(10.0, "1E1");
        checkFormat(1000.0, "1E3");
        checkFormat(0L, "0E0");
        checkFormat(10L, "1E1");
        checkFormat(1000L, "1E3");
        checkFormat(new BigDecimal("0.0"), "0E0");
        checkFormat(new BigDecimal("10.0"), "1E1");
        checkFormat(new BigDecimal("1000.0"), "1E3");
        checkFormat(new BigInteger("0"), "0E0");
        checkFormat(new BigInteger("10"), "1E1");
        checkFormat(new BigInteger("1000"), "1E3");

        df.applyPattern("0.###");

        df.setDecimalSeparatorAlwaysShown(true);
        checkFormat(0.0, "0.");
        checkFormat(10.0, "10.");
        checkFormat(1000.0, "1000.");
        checkFormat(0L, "0.");
        checkFormat(10L, "10.");
        checkFormat(1000L, "1000.");
        checkFormat(new BigDecimal("0.0"), "0.");
        checkFormat(new BigDecimal("10.0"), "10.");
        checkFormat(new BigDecimal("1000.0"), "1000.");
        checkFormat(new BigInteger("0"), "0.");
        checkFormat(new BigInteger("10"), "10.");
        checkFormat(new BigInteger("1000"), "1000.");

        df.setDecimalSeparatorAlwaysShown(false);
        checkFormat(0.0, "0");
        checkFormat(10.0, "10");
        checkFormat(1000.0, "1000");
        checkFormat(0L, "0");
        checkFormat(10L, "10");
        checkFormat(1000L, "1000");
        checkFormat(new BigDecimal("0.0"), "0");
        checkFormat(new BigDecimal("10.0"), "10");
        checkFormat(new BigDecimal("1000.0"), "1000");
        checkFormat(new BigInteger("0"), "0");
        checkFormat(new BigInteger("10"), "10");
        checkFormat(new BigInteger("1000"), "1000");

        Locale.setDefault(defaultLoc);

        if (err) {
            throw new RuntimeException("Wrong format/parse with DecimalFormat");
        }
    }

    static void checkFormat(Number num, String expected) {
        String got = df.format(num);
        if (!got.equals(expected)) {
            err = true;
            System.err.println("    DecimalFormat format(" +
                               num.getClass().getName() +
                               ") error:" +
                               "\n\tnumber:           " + num +
                               "\n\tSeparatorShown? : " + df.isDecimalSeparatorAlwaysShown() +
                               "\n\tgot:              " + got +
                               "\n\texpected:         " + expected);
        }
    }
}
