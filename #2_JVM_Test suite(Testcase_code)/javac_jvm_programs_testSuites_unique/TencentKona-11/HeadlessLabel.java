

import java.awt.*;



public class HeadlessLabel {
    public static void main(String args[]) {
        Label l;

        boolean exceptions = false;
        try {
            l = new Label();
        } catch (java.awt.HeadlessException java_awt_HeadlessException) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");

        exceptions = false;
        try {
            l = new Label("Label me blue");
        } catch (java.awt.HeadlessException java_awt_HeadlessException) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");

        exceptions = false;
        try {
            l = new Label("Label me blue", 200);
        } catch (java.awt.HeadlessException java_awt_HeadlessException) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
