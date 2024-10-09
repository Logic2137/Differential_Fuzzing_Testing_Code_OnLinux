import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class bug6510999 {

    private static JScrollPane s;

    private static void createGui() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DefaultListModel dlm = new DefaultListModel();
        for (int i = 0; i < 100; i++) dlm.addElement(i + " listItemlistItemlistItemlistItemItem");
        JList l = new JList();
        l.setModel(dlm);
        s = new JScrollPane(l);
        l.setSelectedIndex(50);
        l.ensureIndexIsVisible(50);
        frame.add(s);
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(10);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                bug6510999.createGui();
            }
        });
        robot.waitForIdle();
        Point viewPosition = s.getViewport().getViewPosition();
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.waitForIdle();
        if (!s.getViewport().getViewPosition().equals(viewPosition)) {
            throw new RuntimeException("JScrollPane was unexpectedly scrolled");
        }
    }
}
