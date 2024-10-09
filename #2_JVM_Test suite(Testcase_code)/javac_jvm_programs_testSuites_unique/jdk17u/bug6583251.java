import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class bug6583251 {

    private static JFrame frame;

    private static JPopupMenu menu;

    private static void createGui() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        menu = new JPopupMenu();
        menu.add(new JMenuItem("item"));
        panel.setComponentPopupMenu(menu);
        frame.add(panel);
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        if (SystemTray.isSupported()) {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    createGui();
                }
            });
            Robot robot = new Robot();
            robot.waitForIdle();
            menu.show(frame, 0, 0);
            robot.waitForIdle();
            TrayIcon trayIcon = new TrayIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
            MouseEvent ev = new MouseEvent(new JButton(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1, false);
            ev.setSource(trayIcon);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(ev);
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    frame.dispose();
                }
            });
        } else {
            System.out.println("SystemTray not supported. " + "Skipping the test.");
        }
    }
}
