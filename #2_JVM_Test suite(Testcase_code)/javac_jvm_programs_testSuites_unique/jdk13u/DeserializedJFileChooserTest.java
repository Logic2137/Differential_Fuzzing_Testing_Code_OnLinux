import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeserializedJFileChooserTest {

    private static int state = -1;

    private static JFileChooser deserialized;

    public static void main(String[] args) throws Exception {
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
                state = deserialized.showOpenDialog(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Robot robot = new Robot();
        robot.setAutoDelay(50);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.waitForIdle();
        robot.delay(1000);
        if (state != JFileChooser.APPROVE_OPTION) {
            deserialized.cancelSelection();
            throw new RuntimeException("Failed");
        }
    }
}
