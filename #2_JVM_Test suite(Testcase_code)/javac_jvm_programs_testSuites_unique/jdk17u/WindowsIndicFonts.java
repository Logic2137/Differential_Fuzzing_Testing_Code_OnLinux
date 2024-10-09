import java.awt.Font;
import java.io.File;

public class WindowsIndicFonts {

    static boolean failed = false;

    static Font dialog = new Font(Font.DIALOG, Font.PLAIN, 12);

    static String windowsFontDir = "c:\\windows\\fonts";

    public static void main(String[] args) {
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            return;
        }
        String sysRootDir = System.getenv("SYSTEMROOT");
        System.out.println("SysRootDir=" + sysRootDir);
        if (sysRootDir != null) {
            windowsFontDir = sysRootDir + "\\fonts";
        }
        test("\u0905", "Devanagari", "mangal.ttf");
        test("\u0985", "Bengali", "vrinda.ttf");
        test("\u0a05", "Gurmukhi", "raavi.ttf");
        test("\u0a85", "Gujurati", "shruti.ttf");
        test("\u0b05", "Oriya", "kalinga.ttf");
        test("\u0b85", "Tamil", "latha.ttf");
        test("\u0c05", "Telugu", "gautami.ttf");
        test("\u0c85", "Kannada", "tunga.ttf");
        test("\u0d05", "Malayalam", "kartika.ttf");
        test("\u0c05", "Sinhala", "iskpota.ttf");
        test("\u0e05", "Thai", "dokchamp.ttf");
        test("\u0e87", "Lao", "dokchamp.ttf");
        test("\u0e05", "Khmer", "khmerui.ttf");
        test("\u1820", "Mongolian", "monbaiti.ttf");
        if (failed) {
            throw new RuntimeException("Missing support for a script");
        }
    }

    static void test(String text, String script, String filename) {
        File f = new File(windowsFontDir, filename);
        if (!f.exists()) {
            System.out.println("Can't find required font file: " + filename);
            return;
        }
        System.out.println("found:" + f + " for " + script);
        if (dialog.canDisplayUpTo(text) != -1) {
            failed = true;
            System.out.println("No codepoint for " + script);
        }
    }
}
