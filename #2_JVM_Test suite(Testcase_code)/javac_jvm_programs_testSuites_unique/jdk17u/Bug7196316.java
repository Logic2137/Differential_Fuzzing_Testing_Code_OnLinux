import java.io.*;
import java.math.*;
import java.text.*;

public class Bug7196316 {

    private static final String filename = "bug7196316.ser";

    public static void main(String[] args) throws Exception {
        DecimalFormat df;
        RoundingMode mode = RoundingMode.DOWN;
        double given = 6.6;
        String expected;
        String actual;
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename))) {
            df = new DecimalFormat("#");
            df.setRoundingMode(mode);
            expected = df.format(given);
            os.writeObject(df);
        }
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename))) {
            df = (DecimalFormat) is.readObject();
        }
        RoundingMode newMode = df.getRoundingMode();
        if (mode != newMode) {
            throw new RuntimeException("Unexpected roundig mode: " + newMode);
        } else {
            actual = df.format(given);
            if (!expected.equals(actual)) {
                throw new RuntimeException("Unexpected formatted result: \"" + actual + "\"");
            } else {
                System.out.println("Passed: Expected rounding mode (" + newMode + ") & formatted result: \"" + actual + "\"");
            }
        }
    }
}
