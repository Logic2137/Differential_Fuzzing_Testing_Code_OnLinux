



import java.io.*;

public class CanonPath {

    private static boolean windows;

    private static final String WIN_FOOBAR = "\\foo\\bar\\";
    private static final String UNIX_FOOBAR = "/foo/bar/";

    private static final String WIN_FOO = "\\foo.txt";
    private static final String UNIX_FOO = "/foo.txt";

    private static final String WIN_BAR = "bar\\bar.txt";
    private static final String UNIX_BAR = "bar/bar.txt";

    private static final String WIN_SLASH = "\\";
    private static final String UNIX_SLASH = "/";

    private static void printCanonPath(String label, String path)
                throws Exception {

        File f = new File(path);
        System.out.println(label + " path = " + f.getCanonicalPath());
    }

    public static void main(String[] args) throws Exception {

        if (System.getProperty("os.name").startsWith("Windows")) {
            windows = true;
            System.out.println("Testing on Windows");
        } else {
            System.out.println("Testing on Unix");
        }


        System.out.println();
        System.out.println("\\\\foo\\\\bar\\\\- versus /foo/bar/-");
        FilePermission w = new FilePermission(WIN_FOOBAR + "-", "read");
        FilePermission u = new FilePermission(UNIX_FOOBAR + "-", "read");
        printCanonPath("WIN_FOOBAR", WIN_FOOBAR);
        printCanonPath("UNIX_FOOBAR", UNIX_FOOBAR);
        if (windows) {
            if (!w.implies(u) || !u.implies(w)) {
                throw new Exception("FOOBAR test failed");
            }
        } else {
            if (w.implies(u) || u.implies(w)) {
                throw new Exception("FOOBAR test failed");
            }
        }



        System.out.println();
        System.out.println("\\\\foo.txt versus /foo.txt");
        w = new FilePermission(WIN_FOO, "read");
        u = new FilePermission(UNIX_FOO, "read");
        printCanonPath("WIN_FOO", WIN_FOO);
        printCanonPath("UNIX_FOO", UNIX_FOO);
        if (windows) {
            if (!w.implies(u) || !u.implies(w)) {
                throw new Exception("FOO test failed");
            }
        } else {
            if (w.implies(u) || u.implies(w)) {
                throw new Exception("FOO test failed");
            }
        }



        System.out.println();
        System.out.println("bar\\\\bar.txt versus bar/bar.txt");
        w = new FilePermission(WIN_BAR, "read");
        u = new FilePermission(UNIX_BAR, "read");
        printCanonPath("WIN_BAR", WIN_BAR);
        printCanonPath("UNIX_BAR", UNIX_BAR);
        if (windows) {
            if (!w.implies(u) || !u.implies(w)) {
                throw new Exception("BAR test failed");
            }
        } else {
            if (w.implies(u) || u.implies(w)) {
                throw new Exception("BAR test failed");
            }
        }



        System.out.println();
        System.out.println("\\\\ versus /");
        w = new FilePermission(WIN_SLASH, "read");
        u = new FilePermission(UNIX_SLASH, "read");
        printCanonPath("WIN_SLASH", WIN_SLASH);
        printCanonPath("UNIX_SLASH", UNIX_SLASH);
        if (windows) {
            if (!w.implies(u) || !u.implies(w)) {
                throw new Exception("SLASH test failed");
            }
        } else {
            if (w.implies(u) || u.implies(w)) {
                throw new Exception("SLASH test failed");
            }
        }



        System.out.println();
        System.out.println("\\\\- versus /-");
        w = new FilePermission(WIN_SLASH + "-", "read");
        u = new FilePermission(UNIX_SLASH + "-", "read");
        printCanonPath("WIN_SLASH", WIN_SLASH);
        printCanonPath("UNIX_SLASH", UNIX_SLASH);
        if (windows) {
            if (!w.implies(u) || !u.implies(w)) {
                throw new Exception("SLASH/- test failed");
            }
        } else {

            
            
            

            
            
            
        }



        System.out.println();
        System.out.println("- versus -");
        w = new FilePermission("-", "read");
        u = new FilePermission("-", "read");
        printCanonPath("WIN_DASH", "");
        printCanonPath("UNIX_DASH", "");
        if (windows) {
            if (!w.implies(u) || !u.implies(w)) {
                throw new Exception("- test failed");
            }
        } else {
            if (!w.implies(u) || !u.implies(w)) {
                throw new Exception("- test failed");
            }
        }



        System.out.println();
        System.out.println("- versus *");
        w = new FilePermission("-", "read");
        u = new FilePermission("*", "read");
        printCanonPath("WIN_DASH", "");
        printCanonPath("UNIX_STAR", "");
        if (windows) {

            
            
            

            if (!w.implies(u) || u.implies(w)) {
                throw new Exception("- test failed");
            }
        } else {

            
            
            

            if (!w.implies(u) || u.implies(w)) {
                throw new Exception("- test failed");
            }
        }



        System.out.println();
        System.out.println("\\\\* versus /*");
        w = new FilePermission(WIN_SLASH + "*", "read");
        u = new FilePermission(UNIX_SLASH + "*", "read");
        printCanonPath("WIN_SLASH", WIN_SLASH);
        printCanonPath("UNIX_SLASH", UNIX_SLASH);
        if (windows) {
            if (!w.implies(u) || !u.implies(w)) {
                throw new Exception("SLASH/* test failed");
            }
        } else {
            if (w.implies(u) || u.implies(w)) {
                throw new Exception("SLASH/* test failed");
            }
        }



        System.out.println();
        System.out.println("\\\\foo\\\\bar\\\\- versus /foo/bar/foobar/w.txt");
        w = new FilePermission(WIN_FOOBAR + "-", "read");
        u = new FilePermission("/foo/bar/foobar/w.txt", "read");
        printCanonPath("FOOBAR", WIN_FOOBAR);
        printCanonPath("W.TXT", "/foo/bar/foobar/w.txt");
        if (windows) {
            if (!w.implies(u) || u.implies(w)) {
                throw new Exception("w.txt (-) test failed");
            }
        } else {
            if (w.implies(u) || u.implies(w)) {
                throw new Exception("w.txt (-) test failed");
            }
        }



        System.out.println();
        System.out.println("\\\\foo\\\\bar\\\\* versus /foo/bar/w.txt");
        w = new FilePermission(WIN_FOOBAR + "*", "read");
        u = new FilePermission("/foo/bar/w.txt", "read");
        printCanonPath("FOOBAR", WIN_FOOBAR);
        printCanonPath("W.TXT", "/foo/bar/w.txt");
        if (windows) {
            if (!w.implies(u) || u.implies(w)) {
                throw new Exception("w.txt (*) test failed");
            }
        } else {
            if (w.implies(u) || u.implies(w)) {
                throw new Exception("w.txt (*) test failed");
            }
        }


        

        System.out.println();
        System.out.println("/ versus /- and /*");
        File file = new File(UNIX_SLASH);
        FilePermission recursive = new FilePermission
                                (file.getCanonicalPath() +
                                File.separatorChar +
                                "-",
                                "read");
        FilePermission wild = new FilePermission
                                (file.getCanonicalPath() +
                                File.separatorChar +
                                "*",
                                "read");
        FilePermission standard = new FilePermission
                                (file.getCanonicalPath(),
                                "read");
        if (standard.implies(recursive) || standard.implies(wild)) {
            throw new Exception("standard vs directory test failed");
        }
    }
}
