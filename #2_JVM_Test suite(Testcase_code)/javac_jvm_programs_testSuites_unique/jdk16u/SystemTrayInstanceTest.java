

import java.awt.*;



public class SystemTrayInstanceTest {

    private static boolean supported = false;

    public static void main(String[] args) throws Exception {
        String sysTraySupport = System.getProperty("SystemTraySupport");
        if (sysTraySupport == null)
            throw new RuntimeException("SystemTray support status unknown!");

        if ("TRUE".equals(sysTraySupport)) {
            System.out.println("System tray is supported on the platform under test");
            supported = true;
        }

        new SystemTrayInstanceTest().doTest();
    }

    private void doTest() throws Exception {
        boolean flag = SystemTray.isSupported();
        if (supported != flag)
            throw new RuntimeException("FAIL: isSupported did not return the correct value"+
                    (supported ?
                            "SystemTray is supported on the platform under test" :
                            "SystemTray is not supported on the platform under test") +
                    "SystemTray.isSupported() method returned " + flag);

        if (supported) {
            SystemTray tray = SystemTray.getSystemTray();
        } else {
            try {
                SystemTray tray = SystemTray.getSystemTray();
            } catch (UnsupportedOperationException uoe) {
                System.out.println("UnsupportedOperationException thrown correctly");
            }
        }
    }
}
