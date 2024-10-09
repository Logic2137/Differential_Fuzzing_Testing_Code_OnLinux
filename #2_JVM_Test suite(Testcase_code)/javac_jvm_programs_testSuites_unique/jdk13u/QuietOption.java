import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuietOption {

    final File javadoc;

    final File testSrc;

    final String thisClassName;

    public QuietOption() {
        File javaHome = new File(System.getProperty("java.home"));
        if (javaHome.getName().endsWith("jre"))
            javaHome = javaHome.getParentFile();
        javadoc = new File(new File(javaHome, "bin"), "javadoc");
        testSrc = new File(System.getProperty("test.src"));
        thisClassName = QuietOption.class.getName();
    }

    public static void main(String... args) throws Exception {
        QuietOption test = new QuietOption();
        test.run1();
        test.run2();
    }

    void run1() throws Exception {
        List<String> output = doTest(javadoc.getPath(), 
        "-classpath", ".", "-quiet", new File(testSrc, thisClassName + ".java").getPath());
        if (!output.isEmpty()) {
            System.out.println(output);
            throw new Exception("run1: Shhh!, very chatty javadoc!.");
        }
    }

    void run2() throws Exception {
        List<String> output = doTest(javadoc.getPath(), 
        "-classpath", ".", new File(testSrc, thisClassName + ".java").getPath());
        if (output.isEmpty()) {
            System.out.println(output);
            throw new Exception("run2: speak up and please be heard!.");
        }
    }

    List<String> doTest(String... args) throws Exception {
        List<String> output = new ArrayList<>();
        Process p = new ProcessBuilder().command(args).redirectErrorStream(true).start();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line = in.readLine();
            while (line != null) {
                output.add(line.trim());
                line = in.readLine();
            }
        }
        int rc = p.waitFor();
        if (rc != 0) {
            throw new Exception("javadoc failed, rc:" + rc);
        }
        return output;
    }
}
