

import java.awt.*;
import java.awt.image.BufferedImage;



public class GetTrayIcons {

    Image image;

    public static void main(String[] args) throws Exception {
        if (! SystemTray.isSupported())
            System.out.println("SystemTray not supported on the platform under test. " +
                    "Marking the test passed");
        else
            new GetTrayIcons().doTest();
    }

    GetTrayIcons() {
        image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
    }

    void doTest() throws Exception {
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon[] icons = tray.getTrayIcons();
        if (icons == null || icons.length > 0)
            throw new RuntimeException("FAIL: getTrayIcons() returned incorrect " +
                    "value when no icons are added " + icons);

        TrayIcon icon = new TrayIcon(image);
        tray.add(icon);

        icons = tray.getTrayIcons();
        if (icons == null || icons.length != 1)
            throw new RuntimeException("FAIL: getTrayIcons() returned incorrect value " +
                    "when one icon present " + icons);

        icon = new TrayIcon(image);
        tray.add(icon);

        icons = tray.getTrayIcons();
        if (icons == null || icons.length != 2)
            throw new RuntimeException("FAIL: getTrayIcons() returned incorrect value " +
                    "when two icons present " + icons);

        icons = tray.getTrayIcons();
        if (icons != null) {
            for (int i = 0; i < icons.length; i++) {
                tray.remove(icons[i]);
            }

            TrayIcon[] newList = tray.getTrayIcons();

            if (newList == null || newList.length != 0)
                throw new RuntimeException("FAIL: Incorrect value returned by getTrayIcons " +
                        "after icons are added and then removed " + newList);
        }
    }
}
