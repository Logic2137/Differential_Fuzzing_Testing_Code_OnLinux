

import javax.swing.JFrame;
import java.awt.HeadlessException;



public class HeadlessJFrame {
    public static void main(String args[]) {
        boolean exceptions = false;
        try {
            JFrame b = new JFrame();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");

        exceptions = false;
        try {
            JFrame b = new JFrame("Swingin' in the window");
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
