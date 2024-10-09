import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

public class bug6738668 {

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
            String tmpdir = System.getProperty("java.io.tmpdir");
            System.out.println("tmp dir " + tmpdir);
            new JFileChooser(new File(tmpdir + "/temp"));
            System.out.println("Test passed for LookAndFeel " + lookAndFeelInfo.getClassName());
        }
    }
}
