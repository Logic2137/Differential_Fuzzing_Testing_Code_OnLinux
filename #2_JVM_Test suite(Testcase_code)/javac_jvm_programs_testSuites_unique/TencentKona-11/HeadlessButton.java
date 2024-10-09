

import java.awt.*;



public class HeadlessButton {

    public static void main(String args[]) {
        Button b;

        boolean exceptions = false;
        try {
            b = new Button();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw HeadlessException");


        exceptions = false;
        try {
            b = new Button("Press me");
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw HeadlessException");
    }
}
