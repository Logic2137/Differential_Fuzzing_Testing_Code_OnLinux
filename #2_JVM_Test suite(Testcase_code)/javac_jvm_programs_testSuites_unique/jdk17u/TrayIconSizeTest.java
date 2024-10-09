import java.awt.*;
import java.awt.image.BufferedImage;

public class TrayIconSizeTest {

    public static void main(String[] args) throws Exception {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray not supported on the platform under test. " + "Marking the test passed");
        } else {
            new TrayIconSizeTest().doTest();
        }
    }

    void doTest() throws Exception {
        SystemTray tray = SystemTray.getSystemTray();
        Dimension dim = tray.getTrayIconSize();
        if (dim.width <= 5 || dim.height <= 5)
            throw new RuntimeException("FAIL: value returned by getTrayIconSize is not correct: " + dim);
        TrayIcon icon = new TrayIcon(new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB));
        if (!icon.getSize().equals(dim))
            throw new RuntimeException("FAIL: TrayIcon.getSize did not return the same value as " + "getTrayIconSize when TrayIcon not added" + "\n" + "SystemTray.getTrayIconSize(): " + dim + "\n" + "TrayIcon.getSize(): " + icon.getSize());
        tray.add(icon);
        if (icon.getSize().width <= 5 || icon.getSize().height <= 5)
            throw new RuntimeException("FAIL: value returned by TrayIcon.getSize is not correct: " + icon.getSize());
    }
}
