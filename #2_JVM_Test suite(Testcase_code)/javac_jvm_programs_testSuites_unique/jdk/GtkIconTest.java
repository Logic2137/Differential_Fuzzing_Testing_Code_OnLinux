



import java.awt.Toolkit;
import sun.awt.UNIXToolkit;

public class GtkIconTest {
    public static void main(String[] args) throws Exception {
        UNIXToolkit utk = (UNIXToolkit)Toolkit.getDefaultToolkit();
        if (utk.loadGTK()) {
            for (String s : new String[]{ "abc", "\u3042" }) {
                Object obj = utk.getGTKIcon(s);
            }
        }
    }
}
