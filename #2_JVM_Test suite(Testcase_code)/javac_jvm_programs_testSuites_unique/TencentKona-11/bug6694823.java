



import javax.swing.*;
import java.awt.*;
import java.security.Permission;

public class bug6694823 {
    private static JFrame frame;
    private static JPopupMenu popup;
    private static Toolkit toolkit;
    private static Insets screenInsets;
    private static Robot robot;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        toolkit = Toolkit.getDefaultToolkit();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                createGui();
            }
        });

        robot.waitForIdle();

        
        screenInsets = toolkit.getScreenInsets(frame.getGraphicsConfiguration());
        if (screenInsets.bottom == 0) {
            
            return;
        }

        System.setSecurityManager(new SecurityManager(){

            @Override
            public void checkPermission(Permission perm) {
                if (perm.getName().equals("setWindowAlwaysOnTop") ) {
                    throw new SecurityException();
                }
            }

        });

        
        
        checkPopup();

    }

    private static void createGui() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setUndecorated(true);

        popup = new JPopupMenu("Menu");
        for (int i = 0; i < 7; i++) {
            popup.add(new JMenuItem("MenuItem"));
        }
        JPanel panel = new JPanel();
        panel.setComponentPopupMenu(popup);
        frame.add(panel);

        frame.setSize(200, 200);
    }

    private static void checkPopup() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                
                Dimension screenSize = toolkit.getScreenSize();
                frame.setLocation(screenSize.width / 2,
                        screenSize.height - frame.getHeight() - screenInsets.bottom);
                frame.setVisible(true);
            }
        });

        
        robot.waitForIdle();

        final Point point = new Point();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                
                point.x = 0;
                point.y = frame.getHeight() - popup.getPreferredSize().height + screenInsets.bottom;
                popup.show(frame, point.x, point.y);
            }
        });

        
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                Point frameLoc = frame.getLocationOnScreen();
                if (popup.getLocationOnScreen().equals(new Point(frameLoc.x, frameLoc.y + point.y))) {
                    throw new RuntimeException("Popup is not shifted");
                }
                popup.setVisible(false);
                frame.dispose();
            }
        });
    }
}
