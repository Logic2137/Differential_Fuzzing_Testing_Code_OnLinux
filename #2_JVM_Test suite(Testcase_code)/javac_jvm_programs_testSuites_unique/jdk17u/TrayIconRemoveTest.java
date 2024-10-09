import java.awt.*;
import java.awt.image.BufferedImage;

public class TrayIconRemoveTest {

    public static void main(String[] args) throws Exception {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray not supported on the platform under test. " + "Marking the test passed");
        } else {
            new TrayIconRemoveTest().doTest();
        }
    }

    private void doTest() throws Exception {
        Image image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(null);
        TrayIcon icon1 = new TrayIcon(image);
        tray.add(icon1);
        tray.remove(icon1);
        TrayIcon[] icons = tray.getTrayIcons();
        if (icons.length != 0)
            throw new RuntimeException("FAIL: There are icons still present even after " + "removing the added icon" + "\n" + "No. of icons present: " + icons.length);
        TrayIcon icon2 = new TrayIcon(image);
        tray.remove(icon2);
        TrayIcon icon3 = new TrayIcon(image);
        tray.add(icon3);
        TrayIcon newIcon = new TrayIcon(image);
        tray.remove(newIcon);
        tray.remove(null);
    }
}
