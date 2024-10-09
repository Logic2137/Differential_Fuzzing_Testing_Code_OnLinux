import java.applet.Applet;
import java.awt.HeadlessException;

public class HeadlessApplet {

    public static void main(String[] args) {
        boolean noExceptions = true;
        try {
            Applet a = new Applet();
        } catch (HeadlessException e) {
            noExceptions = false;
        }
        if (noExceptions) {
            throw new RuntimeException("No HeadlessException occured when creating Applet in headless mode");
        }
    }
}
