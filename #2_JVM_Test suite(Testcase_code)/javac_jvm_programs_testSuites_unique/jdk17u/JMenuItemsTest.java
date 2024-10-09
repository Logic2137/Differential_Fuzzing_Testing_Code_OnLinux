import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JMenuItemsTest {

    private static JFrame mainFrame;

    private static JMenu disabledMenu;

    private static JMenuItem disabledMenuItem;

    public JMenuItemsTest() {
        createUI();
    }

    private void createUI() {
        mainFrame = new JFrame("Test");
        disabledMenu = new JMenu("Disabled Menu");
        disabledMenu.setForeground(Color.BLUE);
        disabledMenu.setEnabled(false);
        disabledMenuItem = new JMenuItem("Disabled MenuItem");
        disabledMenuItem.setForeground(Color.BLUE);
        disabledMenuItem.setEnabled(false);
        JMenuBar menuBar = new JMenuBar();
        menuBar = new JMenuBar();
        menuBar.add(disabledMenu);
        menuBar.add(disabledMenuItem);
        mainFrame.add(menuBar);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void dispose() {
        mainFrame.dispose();
    }

    private void testDisabledStateOfJMenu() {
        Rectangle rect = disabledMenu.getBounds();
        BufferedImage image = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
        disabledMenu.paint(image.getGraphics());
        int y = image.getHeight() / 2;
        for (int x = 0; x < image.getWidth(); x++) {
            Color c = new Color(image.getRGB(x, y));
            if (c.equals(Color.BLUE)) {
                dispose();
                throw new RuntimeException("JMenu Disabled" + " State not Valid.");
            }
        }
    }

    private void testDisabledStateOfJMenuItem() {
        Rectangle rect = disabledMenuItem.getBounds();
        BufferedImage image = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
        disabledMenuItem.paint(image.getGraphics());
        int y = image.getHeight() / 2;
        for (int x = 0; x < image.getWidth(); x++) {
            Color c = new Color(image.getRGB(x, y));
            if (c.equals(Color.BLUE)) {
                dispose();
                throw new RuntimeException("JMenuItem Disabled" + " State not Valid.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        SwingUtilities.invokeAndWait(() -> {
            try {
                JMenuItemsTest obj = new JMenuItemsTest();
                obj.testDisabledStateOfJMenu();
                obj.testDisabledStateOfJMenuItem();
                obj.dispose();
            } catch (Exception ex) {
                throw ex;
            }
        });
    }
}
