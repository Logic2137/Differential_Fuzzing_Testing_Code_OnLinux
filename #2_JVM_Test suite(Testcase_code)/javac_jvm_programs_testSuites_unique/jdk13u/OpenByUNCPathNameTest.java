import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class OpenByUNCPathNameTest {

    private static boolean validatePlatform() {
        String osName = System.getProperty("os.name");
        if (osName == null) {
            throw new RuntimeException("Name of the current OS could not be retrieved.");
        }
        return osName.startsWith("Windows");
    }

    private static void openFile() throws IOException {
        if (!Desktop.isDesktopSupported()) {
            System.out.println("java.awt.Desktop is not supported on this platform.");
        } else {
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.OPEN)) {
                System.out.println("Action.OPEN is not supported on this platform.");
                return;
            }
            File file = File.createTempFile("Read Me File", ".txt");
            try {
                desktop.open(file);
                Robot robot = null;
                try {
                    Thread.sleep(5000);
                    robot = new Robot();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pressAltF4Keys(robot);
                String uncFilePath = "\\\\127.0.0.1\\" + file.getAbsolutePath().replace(':', '$');
                File uncFile = new File(uncFilePath);
                if (!uncFile.exists()) {
                    throw new RuntimeException(String.format("File with UNC pathname '%s' does not exist.", uncFilePath));
                }
                desktop.open(uncFile);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                pressAltF4Keys(robot);
            } finally {
                file.delete();
            }
        }
    }

    private static void pressAltF4Keys(Robot robot) {
        if (robot != null) {
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_F4);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_F4);
            robot.keyRelease(KeyEvent.VK_ALT);
        }
    }

    public static void main(String[] args) throws IOException {
        if (!validatePlatform()) {
            System.out.println("This test is only for MS Windows OS.");
        } else {
            openFile();
        }
    }
}
