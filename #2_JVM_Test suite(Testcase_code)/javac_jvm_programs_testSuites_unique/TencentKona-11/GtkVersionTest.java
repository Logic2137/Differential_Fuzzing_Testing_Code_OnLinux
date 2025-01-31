



import sun.awt.UNIXToolkit;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GtkVersionTest {
    public static class LoadGtk {
        public static void main(String[] args) {
            ((UNIXToolkit)Toolkit.getDefaultToolkit()).loadGTK();
        }
    }

    public static void main(String[] args) throws Exception {
        test(null, "3");
        test("2", "2");
        test("2.2", "2");
        test("3", "3");
    }

    private static void test(String version, String expect) throws Exception {
        System.out.println( "Test " +
                (version == null ? "no" : " GTK" + version) + " preference.");
        Process p = Runtime.getRuntime().exec(System.getProperty("java.home") +
                "/bin/java " +
                (version == null ? "" : "-Djdk.gtk.version=" + version) +
                " -Djdk.gtk.verbose=true " +
                "--add-exports=java.desktop/sun.awt=ALL-UNNAMED " +
                "-cp " + System.getProperty("java.class.path", ".") +
                " GtkVersionTest$LoadGtk");
        p.waitFor();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getErrorStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Looking for GTK" + expect + " library")) {
                    return;
                } else if (line.contains("Looking for GTK")) {
                    break;
                }
            }
            throw new RuntimeException("Wrong GTK library version: \n" + line);
        }
    }

}
