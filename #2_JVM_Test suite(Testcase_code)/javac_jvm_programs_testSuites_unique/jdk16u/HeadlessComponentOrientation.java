

import java.awt.*;
import java.util.Locale;



public class HeadlessComponentOrientation {
    public static void main(String args[]) {
        ComponentOrientation.LEFT_TO_RIGHT.isHorizontal();
        ComponentOrientation.RIGHT_TO_LEFT.isHorizontal();
        ComponentOrientation.UNKNOWN.isHorizontal();
        ComponentOrientation.LEFT_TO_RIGHT.isLeftToRight();
        ComponentOrientation.RIGHT_TO_LEFT.isLeftToRight();
        ComponentOrientation.UNKNOWN.isLeftToRight();

        for (Locale locale : Locale.getAvailableLocales())
            ComponentOrientation.getOrientation(locale);
    }
}
