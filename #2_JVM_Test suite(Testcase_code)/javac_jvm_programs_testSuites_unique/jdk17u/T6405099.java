import java.io.*;

public class T6405099 {

    public static void main(String[] args) {
        File bad = new File("bad");
        try {
            bad.mkdir();
            bad.setReadable(false);
            bad.setExecutable(false);
            test(bad);
        } finally {
            bad.setExecutable(true);
            bad.setReadable(true);
        }
    }

    static void test(File dir) {
        String[] args = { "-source", "8", "-target", "8", "-extdirs", dir.getPath(), "-d", ".", new File(System.getProperty("test.src", "."), "T6405099.java").getPath() };
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        int rc = com.sun.tools.javac.Main.compile(args, pw);
        pw.close();
        System.out.println(sw);
        if (rc != 0)
            throw new Error("compilation failed");
    }
}
