import java.awt.AWTError;
import java.awt.Toolkit;

public class BadDisplayTest {

    public static void main(String[] args) {
        if (Boolean.getBoolean("java.awt.headless")) {
            return;
        }
        Throwable th = null;
        try {
            Toolkit.getDefaultToolkit();
        } catch (Throwable x) {
            th = x;
        }
        if (!(th instanceof AWTError)) {
            System.exit(1);
        }
    }
}
