

import java.awt.*;



public class HeadlessChoice {

    public static void main(String args[]) {
        boolean exceptions = false;
        try {
            Choice c = new Choice();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("Constructor did not throw HeadlessException");
    }
}
