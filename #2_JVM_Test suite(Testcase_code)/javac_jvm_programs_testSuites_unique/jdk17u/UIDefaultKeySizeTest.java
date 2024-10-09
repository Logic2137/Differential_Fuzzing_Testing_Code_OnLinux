import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.Enumeration;
import java.util.Iterator;

public class UIDefaultKeySizeTest {

    static Enumeration e;

    static Iterator itr;

    static boolean defaultTestFail, writeTestFail;

    public static void main(String[] args) throws Exception {
        UIManager.LookAndFeelInfo[] installedLookAndFeels;
        installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo LF : installedLookAndFeels) {
            try {
                UIManager.setLookAndFeel(LF.getClassName());
                defaultTestFail = keySizeTest();
                SwingUtilities.invokeAndWait(() -> {
                    UIManager.getDefaults().put("TestKey", "TestValue");
                });
                writeTestFail = keySizeTest();
                if (defaultTestFail && writeTestFail) {
                    throw new RuntimeException("Default key count and Write " + "key count both are not same in keys() and" + " keySet()");
                } else if (defaultTestFail || writeTestFail) {
                    if (defaultTestFail) {
                        throw new RuntimeException("Default key count is not" + " same in keys() and keySet()");
                    } else {
                        throw new RuntimeException("Write key count is not" + " same in keys() and keySet()");
                    }
                }
                SwingUtilities.invokeAndWait(() -> {
                    UIManager.getDefaults().remove("TestKey");
                });
            } catch (UnsupportedLookAndFeelException e) {
                System.out.println("    Note: LookAndFeel " + LF.getClassName() + " is not supported on this configuration");
            }
        }
    }

    private static boolean keySizeTest() throws Exception {
        int keysCount = 0;
        int keySetCount = 0;
        SwingUtilities.invokeAndWait(() -> {
            e = UIManager.getDefaults().keys();
            itr = UIManager.getDefaults().keySet().iterator();
        });
        while (e.hasMoreElements()) {
            keysCount++;
            e.nextElement();
        }
        while (itr.hasNext()) {
            keySetCount++;
            itr.next();
        }
        return !(keysCount == keySetCount);
    }
}
