



import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UIManagerSetLookAndFeelNPETest {
    public static void main(String[] args)
            throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException
    {
        boolean NPEThrown = false;
        try {
            UIManager.setLookAndFeel((String)null);
        } catch (NullPointerException e) {
            NPEThrown = true;
        }

        if (!NPEThrown) {
            throw new RuntimeException("A NullPointerException is expected " +
                    "from setLookAndFeel(String), if the className is null");
        }
    }
}
