


import java.awt.font.*;
import java.text.*;
import java.util.*;

public class Bug7051769 {

    static {
        if (System.getProperty("preloadBidi", "").equals("true")) {
            
            try {
                Class.forName("sun.text.bidi.BidiBase");
                System.out.println("BidiBase class has been pre-loaded.");
            } catch (ClassNotFoundException e) {
                System.out.println("BidiBase class could not be pre-loaded.");
            }
        }
    }

    private static boolean err = false;

    public static void main(String[] args) {
        testNumericShaping();

        if (err) {
            throw new RuntimeException("Failed");
        } else {
            System.out.println("Passed.");
        }
    }

    private static void testNumericShaping() {
        Map attrNS = new HashMap();
        attrNS.put(TextAttribute.NUMERIC_SHAPING,
                   NumericShaper.getContextualShaper(NumericShaper.ARABIC));
        attrNS.put(TextAttribute.RUN_DIRECTION,
                   TextAttribute.RUN_DIRECTION_RTL);

        String text = "\u0623\u0643\u062a\u0648\u0628\u0631 10";
        String expected = "sun.text.bidi.BidiBase[dir: 2 baselevel: 1 length: 9 runs: [1 1 1 1 1 1 1 2 2] text: [0x623 0x643 0x62a 0x648 0x628 0x631 0x20 0x661 0x660]]";

        AttributedString as = new AttributedString(text, attrNS);
        AttributedCharacterIterator itr = as.getIterator();
        itr.last();
        itr.next();
        Bidi bidi = new Bidi(itr);
        String got = bidi.toString();

        if (!got.equals(expected)) {
            err = true;
            System.err.println("Wrong toString() output: " +
                               "\n\tExpected=" + expected +
                               "\n\tGot=" + got);
        }
    }

}
