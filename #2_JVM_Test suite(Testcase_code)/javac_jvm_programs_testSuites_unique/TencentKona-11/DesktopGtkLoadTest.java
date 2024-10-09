




import java.awt.*;
import java.io.*;

public class DesktopGtkLoadTest {
    public static class RunDesktop {
        public static void main(String[] args) {
            Desktop.getDesktop();
        }
    }

    public static void main(String[] args) throws Exception {
        Process p = Runtime.getRuntime().exec(System.getProperty("java.home") +
                "/bin/java -Djdk.gtk.version=3 -Djdk.gtk.verbose=true " +
                "-cp " + System.getProperty("java.class.path", ".") +
                " DesktopGtkLoadTest$RunDesktop");
        p.waitFor();
        try (BufferedReader br = new BufferedReader(
                                   new InputStreamReader(p.getErrorStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Looking for GTK2 library")) {
                    break;
                }
                if (line.contains("Looking for GTK3 library")) {
                    return;
                }
            }
            throw new RuntimeException("Wrong GTK library version: \n" + line);
        }

    }

}
