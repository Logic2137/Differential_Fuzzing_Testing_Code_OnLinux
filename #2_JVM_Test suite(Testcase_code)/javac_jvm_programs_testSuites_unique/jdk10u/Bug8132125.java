



import java.text.*;
import java.util.*;

public class Bug8132125 {
    public static void main(String[] args) {
        Locale deCH = new Locale("de", "CH");
        NumberFormat nf = NumberFormat.getInstance(deCH);

        String expected = "54'839'483.142"; 
        String actual = nf.format(54839483.1415);
        if (!actual.equals(expected)) {
            throw new RuntimeException("correct for de_CH: " + expected + " vs. actual " + actual);
        }
    }
}
