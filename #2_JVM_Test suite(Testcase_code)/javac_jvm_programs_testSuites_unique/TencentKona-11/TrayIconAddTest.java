

import java.awt.*;
import java.awt.image.BufferedImage;




public class TrayIconAddTest {

    public static void main(String[] args) throws Exception {
        if (! SystemTray.isSupported()) {
            System.out.println("SystemTray not supported on the platform under test. " +
                    "Marking the test passed");
        } else {
            new TrayIconAddTest().doTest();
        }
    }

    void doTest() throws Exception {
        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(null);
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException thrown correctly when add(null) called");
        }

        TrayIcon icon = new TrayIcon(new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB));

        tray.add(icon);

        try {
            tray.add(icon);
        } catch (IllegalArgumentException iae) {
            System.out.println("IllegalArgumentException rightly thrown when tray icon is added twice");
        }
    }
}
