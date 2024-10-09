




import java.awt.*;

public class BadDisplayTest{
   public static void main(String[] args) {

        Throwable th = null;
        try {
            Toolkit.getDefaultToolkit();
        } catch (Throwable x) {
            th = x;
        }
        if ( !(th instanceof AWTError)) {
            System.exit(1);
        }
    }
}
