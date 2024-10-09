import java.io.File;
import java.io.IOException;

public class WinDeviceName {

    private static String[] devnames = { "CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9", "CLOCK$" };

    public static void main(String[] args) {
        String osName = System.getProperty("os.name");
        if (!osName.startsWith("Windows")) {
            return;
        }
        for (int i = 0; i < devnames.length; i++) {
            String[] names = { devnames[i], devnames[i] + ".TXT", devnames[i].toLowerCase(), devnames[i].toLowerCase() + ".txt" };
            for (String name : names) {
                File f = new File(name);
                if (f.isFile()) {
                    if ("CLOCK$".equals(devnames[i]) && (osName.startsWith("Windows 9") || osName.startsWith("Windows Me"))) {
                        continue;
                    }
                    throw new RuntimeException("isFile() returns true for " + "Device name " + devnames[i]);
                }
                if (!"CLOCK$".equals(devnames[i])) {
                    try {
                        System.out.println((new File(name)).getCanonicalPath());
                    } catch (IOException ie) {
                        throw new RuntimeException("Fail to get canonical " + "path for " + name);
                    }
                }
            }
        }
    }
}
