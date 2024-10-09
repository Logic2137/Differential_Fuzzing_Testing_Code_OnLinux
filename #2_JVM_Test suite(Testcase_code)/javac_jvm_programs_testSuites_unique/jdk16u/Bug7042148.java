


import java.awt.font.*;
import java.text.*;
import java.util.*;

public class Bug7042148 {

    private static boolean err = false;

    public static void main(String[] args) {
        testDirection();

        if (err) {
            throw new RuntimeException("Failed");
        } else {
            System.out.println("Passed.");
        }
    }

    private static void testDirection() {
        Map attrLTR = new HashMap();
        attrLTR.put(TextAttribute.RUN_DIRECTION,
                    TextAttribute.RUN_DIRECTION_LTR);
        Map attrRTL = new HashMap();
        attrRTL.put(TextAttribute.RUN_DIRECTION,
                    TextAttribute.RUN_DIRECTION_RTL);

        String str1 = "A\u05e0";
        String str2 = "\u05e0B";

        test(str1, attrLTR, Bidi.DIRECTION_LEFT_TO_RIGHT);
        test(str1, attrRTL, Bidi.DIRECTION_RIGHT_TO_LEFT);
        test(str2, attrLTR, Bidi.DIRECTION_LEFT_TO_RIGHT);
        test(str2, attrRTL, Bidi.DIRECTION_RIGHT_TO_LEFT);
    }

    private static void test(String text, Map attr, int dirFlag) {
        boolean expected = (dirFlag == Bidi.DIRECTION_LEFT_TO_RIGHT);

        Bidi bidi = new Bidi(text, dirFlag);
        boolean got = bidi.baseIsLeftToRight();
        if (got != expected) {
            err = true;
            System.err.println("wrong Bidi(String, int).baseIsLeftToRight() value: " +
                               "\n\ttext=" + text +
                               "\n\tExpected=" + expected +
                               "\n\tGot=" + got);
        }

        AttributedString as = new AttributedString(text, attr);
        AttributedCharacterIterator itr = as.getIterator();
        itr.last();
        itr.next();
        bidi = new Bidi(itr);
        got = bidi.baseIsLeftToRight();
        if (got != expected) {
            err = true;
            System.err.println("Wrong Bidi(AttributedCharacterIterator).baseIsLeftToRight() value: " +
                               "\n\ttext=" + text +
                               "\n\tExpected=" + expected +
                               "\n\tGot=" + got);
        }
    }

}
