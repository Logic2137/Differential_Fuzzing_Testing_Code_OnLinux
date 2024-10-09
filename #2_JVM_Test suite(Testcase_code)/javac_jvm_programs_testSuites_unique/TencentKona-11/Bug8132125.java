



import java.text.*;
import java.util.*;

public class Bug8132125 {
    public static void main(String[] args) {
        Locale deCH = new Locale("de", "CH");
        NumberFormat nf = NumberFormat.getInstance(deCH);

        String expected = "54\u2019839\u2019483.142"; 
        String actual = nf.format(54839483.1415);
        if (!actual.equals(expected)) {
            throw new RuntimeException("incorrect for de_CH: " + expected + " vs. actual " + actual);
        }
    }
}
