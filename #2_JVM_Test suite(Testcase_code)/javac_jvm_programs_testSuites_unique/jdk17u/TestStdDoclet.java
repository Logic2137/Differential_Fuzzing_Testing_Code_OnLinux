import java.io.*;
import java.util.*;

public class TestStdDoclet {

    public static void main(String... args) throws Exception {
        new TestStdDoclet().run();
    }

    void run() throws Exception {
        File javaHome = new File(System.getProperty("java.home"));
        File javadoc = new File(new File(javaHome, "bin"), "javadoc");
        File testSrc = new File(System.getProperty("test.src"));
        String thisClassName = TestStdDoclet.class.getName();
        List<String> cmdArgs = new ArrayList<>();
        cmdArgs.add(javadoc.getPath());
        cmdArgs.addAll(
        Arrays.asList("-classpath", ".", "-Xdoclint:none", "-package", new File(testSrc, thisClassName + ".java").getPath()));
        Process p = new ProcessBuilder().command(cmdArgs).redirectErrorStream(true).start();
        int actualDocletWarnCount = 0;
        int reportedDocletWarnCount = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.err.println(line);
                if (line.contains("TestStdDoclet.java") && line.contains("DoesNotExist")) {
                    actualDocletWarnCount++;
                }
                if (line.matches("[0-9]+ warning(s)?")) {
                    reportedDocletWarnCount = Integer.valueOf(line.substring(0, line.indexOf(" ")));
                }
            }
        } finally {
            in.close();
        }
        int rc = p.waitFor();
        if (rc != 0)
            System.err.println("javadoc failed, rc:" + rc);
        int expectedDocletWarnCount = 2;
        checkEqual("actual", actualDocletWarnCount, "expected", expectedDocletWarnCount);
        checkEqual("actual", actualDocletWarnCount, "reported", reportedDocletWarnCount);
    }

    private void checkEqual(String l1, int i1, String l2, int i2) throws Exception {
        if (i1 != i2)
            throw new Exception(l1 + " warn count, " + i1 + ", does not match " + l2 + " warn count, " + i2);
    }
}
