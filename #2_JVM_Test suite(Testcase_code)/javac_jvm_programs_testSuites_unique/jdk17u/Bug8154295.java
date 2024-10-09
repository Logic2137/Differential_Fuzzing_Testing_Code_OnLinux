import java.util.Currency;

public class Bug8154295 {

    public static void main(String[] args) {
        String numericCode = Currency.getInstance("AFA").getNumericCodeAsString();
        if (!numericCode.equals("004")) {
            throw new RuntimeException("[Expected 004, " + "found " + numericCode + " for AFA]");
        }
        numericCode = Currency.getInstance("AUD").getNumericCodeAsString();
        if (!numericCode.equals("036")) {
            throw new RuntimeException("[Expected 036, " + "found " + numericCode + " for AUD]");
        }
        numericCode = Currency.getInstance("USD").getNumericCodeAsString();
        if (!numericCode.equals("840")) {
            throw new RuntimeException("[Expected 840, " + "found " + numericCode + " for USD]");
        }
    }
}
