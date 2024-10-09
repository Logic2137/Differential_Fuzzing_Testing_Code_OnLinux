import javax.swing.JApplet;
import java.awt.HeadlessException;

public class HeadlessJApplet {

    public static void main(String[] args) {
        boolean exceptions = false;
        try {
            new JApplet();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
