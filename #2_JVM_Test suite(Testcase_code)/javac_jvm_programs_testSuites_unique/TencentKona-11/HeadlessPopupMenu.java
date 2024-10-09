

import java.awt.*;



public class HeadlessPopupMenu {
    public static void main(String args[]) {
            PopupMenu pm;
        boolean exceptions = false;
        try {
            pm = new PopupMenu();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");

        exceptions = false;
        try {
            pm = new PopupMenu("Popup menu");
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
