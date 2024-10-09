

import java.awt.*;



public class HeadlessWindow {
    public static void main(String args[]) {
        boolean exceptions = false;
        try {
            Window b = new Window(new Frame("Hi there"));
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
