import java.text.DecimalFormat;
import java.util.Locale;

public class Bug8165466 {

    public static void main(String[] args) {
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getPercentInstance(Locale.US);
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(0);
        nf.setMultiplier(1);
        double d = 0.005678;
        String result = nf.format(d);
        if (!result.equals("0.006%")) {
            throw new RuntimeException("[Failed while formatting the double" + " value: " + d + " Expected: 0.006%, Found: " + result + "]");
        }
        d = 0.00;
        result = nf.format(d);
        if (!result.equals("0%")) {
            throw new RuntimeException("[Failed while formatting the double" + " value: " + d + " Expected: 0%, Found: " + result + "]");
        }
        d = 0.005678;
        result = nf.format(d);
        if (!result.equals("0.006%")) {
            throw new RuntimeException("[Failed while formatting the double" + " value: " + d + " Expected: 0.006%, Found: " + result + "]");
        }
        d = 0.005678;
        result = nf.format(d);
        if (!result.equals("0.006%")) {
            throw new RuntimeException("[Failed while formatting the double" + " value: " + d + " Expected: 0.006%, Found: " + result + "]");
        }
        d = 9.00;
        result = nf.format(d);
        if (!result.equals("9%")) {
            throw new RuntimeException("[Failed while formatting the double" + " value: " + d + " Expected: 9%, Found: " + result + "]");
        }
        d = 0.005678;
        result = nf.format(d);
        if (!result.equals("0.006%")) {
            throw new RuntimeException("[Failed while formatting the double" + " value: " + d + " Expected: 0.006%, Found: " + result + "]");
        }
    }
}
