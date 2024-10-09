import java.awt.FocusTraversalPolicy;
import java.awt.Window;

public class InitialFTP {

    public static void test(Window win, Class<? extends FocusTraversalPolicy> expectedPolicy) {
        FocusTraversalPolicy ftp = win.getFocusTraversalPolicy();
        System.out.println("==============" + "\n" + "Tested window:    " + win + "\n" + "Expected policy:  " + expectedPolicy + "\n" + "Effective policy: " + ftp.getClass());
        if (!expectedPolicy.equals(ftp.getClass())) {
            throw new RuntimeException("Test failed: wrong effective focus policy");
        }
    }
}
