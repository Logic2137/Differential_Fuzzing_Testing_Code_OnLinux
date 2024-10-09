import java.text.*;
import java.util.*;

public class Bug4740757 {

    private static boolean err = false;

    public static void main(String[] args) {
        Locale defaultLocale = Locale.getDefault();
        if (defaultLocale.getLanguage().equals("th")) {
            Locale.setDefault(Locale.KOREA);
            test4740757();
            Locale.setDefault(defaultLocale);
        } else {
            test4740757();
        }
        if (err) {
            throw new RuntimeException("Incorrect Line-breaking");
        }
    }

    private static void test4740757() {
        String source = "\uc548\ub155\ud558\uc138\uc694? \uc88b\uc740 \uc544\uce68, \uc5ec\ubcf4\uc138\uc694! \uc548\ub155. End.";
        String expected = "\uc548/\ub155/\ud558/\uc138/\uc694? /\uc88b/\uc740 /\uc544/\uce68, /\uc5ec/\ubcf4/\uc138/\uc694! /\uc548/\ub155. /End./";
        BreakIterator bi = BreakIterator.getLineInstance(Locale.KOREAN);
        bi.setText(source);
        int start = bi.first();
        int end = bi.next();
        StringBuilder sb = new StringBuilder();
        for (; end != BreakIterator.DONE; start = end, end = bi.next()) {
            sb.append(source.substring(start, end));
            sb.append('/');
        }
        if (!expected.equals(sb.toString())) {
            System.err.println("Failed: Hangul line-breaking failed." + "\n\tExpected: " + expected + "\n\tGot:      " + sb + "\nin " + Locale.getDefault() + " locale.");
            err = true;
        }
    }
}
