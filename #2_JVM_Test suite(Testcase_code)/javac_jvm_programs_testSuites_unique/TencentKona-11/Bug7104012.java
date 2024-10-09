


import java.text.*;
import java.util.*;

public class Bug7104012 {

    public static void main(String[] args) {
        boolean err = false;

        List<String> data = new ArrayList<>();
        data.add("\udb40");
        data.add(" \udb40");
        data.add("\udc53");
        data.add(" \udc53");
        data.add(" \udb40\udc53");
        data.add("\udb40\udc53");
        data.add("ABC \udb40\udc53 123");
        data.add("\udb40\udc53 ABC \udb40\udc53");

        for (Locale locale : Locale.getAvailableLocales()) {
            List<BreakIterator> breakIterators = new ArrayList<>();
            breakIterators.add(BreakIterator.getCharacterInstance(locale));
            breakIterators.add(BreakIterator.getLineInstance(locale));
            breakIterators.add(BreakIterator.getSentenceInstance(locale));
            breakIterators.add(BreakIterator.getWordInstance(locale));

            for (BreakIterator bi : breakIterators) {
                for (String str : data) {
                    try {
                        bi.setText(str);
                        bi.first();
                        while (bi.next() != BreakIterator.DONE) { }
                        bi.last();
                        while (bi.previous() != BreakIterator.DONE) { }
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("    " + data.indexOf(str)
                            + ": BreakIterator(" + locale
                            + ") threw AIOBE.");
                        err = true;
                    }
                }
            }
        }

        if (err) {
            throw new RuntimeException("Unexpected exeption.");
        }
    }

}
