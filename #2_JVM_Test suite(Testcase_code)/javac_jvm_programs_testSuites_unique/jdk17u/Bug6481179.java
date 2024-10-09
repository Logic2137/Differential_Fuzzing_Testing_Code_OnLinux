import java.text.MessageFormat;
import java.text.ParseException;

public class Bug6481179 {

    public static void main(String[] args) {
        boolean err = false;
        try {
            MessageFormat.format("Testdata {1,invalid_format_type}", new Object[] { "val0", "val1" });
            System.err.println("Error: IllegalArgumentException should be thrown.");
            err = true;
        } catch (IllegalArgumentException e) {
            String expected = "unknown format type: invalid_format_type";
            String got = e.getMessage();
            if (!expected.equals(got)) {
                System.err.println("Error: Unexpected error message: " + got);
                err = true;
            }
        } catch (Exception e) {
            System.err.println("Error: Unexpected exception was thrown: " + e);
            err = true;
        }
        if (err) {
            throw new RuntimeException("Failed.");
        }
    }
}
