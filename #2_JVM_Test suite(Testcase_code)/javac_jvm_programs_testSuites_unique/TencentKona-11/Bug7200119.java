


import java.text.*;
import java.util.*;

public class Bug7200119 {
    public static void main(String[] args) {
        List<Locale> avail = Arrays.asList(Collator.getAvailableLocales());

        if (!avail.contains(Locale.US)) {
            throw new RuntimeException("Failed.");
        }
    }
}
