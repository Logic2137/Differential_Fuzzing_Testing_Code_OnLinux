import java.awt.EventQueue;
import java.awt.Robot;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static javax.swing.UIManager.getInstalledLookAndFeels;

public class DeserializedJFileChooserTest {

    private static volatile JButton defaultSet;

    private static JFileChooser deserialized;

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            EventQueue.invokeAndWait(() -> setLookAndFeel(laf));
            SwingUtilities.invokeLater(() -> {
                try {
                    JFileChooser jfc = new JFileChooser();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(jfc);
                    oos.close();
                    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    deserialized = (JFileChooser) ois.readObject();
                    deserialized.showOpenDialog(null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Robot robot = new Robot();
            robot.waitForIdle();
            EventQueue.invokeAndWait(() -> {
                defaultSet = deserialized.getRootPane().getDefaultButton();
                deserialized.setVisible(false);
                Thread.currentThread().interrupt();
            });
            robot.waitForIdle();
            if (defaultSet == null) {
                throw new RuntimeException("default button is null");
            }
        }
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            System.out.println("laf = " + laf);
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
