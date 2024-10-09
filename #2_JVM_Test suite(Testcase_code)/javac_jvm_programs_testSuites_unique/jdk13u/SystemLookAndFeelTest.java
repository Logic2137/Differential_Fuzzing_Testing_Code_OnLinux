public class SystemLookAndFeelTest {

    public static void main(String[] args) {
        String laf = javax.swing.UIManager.getSystemLookAndFeelClassName();
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("OS is " + os);
        System.out.println("Reported System LAF is " + laf);
        String expLAF = null;
        if (os.contains("windows")) {
            expLAF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        } else if (os.contains("macos")) {
            expLAF = "com.apple.laf.AquaLookAndFeel";
        } else if (os.contains("linux") || os.contains("sunos")) {
            String gnome = System.getenv("GNOME_DESKTOP_SESSION_ID");
            String desktop = System.getenv("XDG_CURRENT_DESKTOP");
            System.out.println("Gnome desktop session ID is " + gnome);
            System.out.println("XDG_CURRENT_DESKTOP is set to " + desktop);
            if (gnome != null || (desktop != null && desktop.toLowerCase().contains("gnome"))) {
                expLAF = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            } else {
                expLAF = "javax.swing.plaf.metal.MetalLookAndFeel";
            }
        }
        System.out.println("Expected System LAF is " + expLAF);
        if (expLAF == null) {
            System.out.println("No match for expected LAF, unknown OS ?");
            return;
        }
        if (!(laf.equals(expLAF))) {
            throw new RuntimeException("LAF not as expected");
        }
    }
}
