

import java.awt.*;
import java.awt.image.BufferedImage;



public class NoPermissionTest {

    public static void main(String[] args) {
        if (! SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported on this platform. Marking the test passed");
        } else {

            BufferedImage im = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics gr = im.createGraphics();
            gr.setColor(Color.white);
            gr.fillRect(0, 0, 16, 16);

            try {
                SystemTray.getSystemTray();
                throw new RuntimeException("FAIL: SecurityException not thrown by getSystemTray method");
            } catch (SecurityException ex) {
                if (!ex.getMessage().matches(".+java.awt.AWTPermission.+accessSystemTray.*"))
                    throw new RuntimeException("FAIL: Security exception thrown due to unexpected reason");
            }

            try {
                TrayIcon icon = new TrayIcon(im, "Caption");
                throw new RuntimeException("FAIL: SecurityException not thrown by TrayIcon constructor");
            } catch (SecurityException ex) {
                if (!ex.getMessage().matches(".+java.awt.AWTPermission.+accessSystemTray.*"))
                    throw new RuntimeException("FAIL: Security exception thrown due to unexpected reason");
            }
        }
    }

}
