



import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

public class JFrameMenuSerializationTest {
    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame();
            frame.setJMenuBar(new JMenuBar());
            frame.setVisible(true);
            frame.getAccessibleContext().addPropertyChangeListener(evt -> {});
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        robot.delay(200);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(10000);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        SwingUtilities.invokeAndWait(() -> {
            try {
                oos.writeObject(frame);
            } catch (NotSerializableException e) {
                throw new RuntimeException("Test failed", e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        SwingUtilities.invokeLater(frame::dispose);

    }
}
