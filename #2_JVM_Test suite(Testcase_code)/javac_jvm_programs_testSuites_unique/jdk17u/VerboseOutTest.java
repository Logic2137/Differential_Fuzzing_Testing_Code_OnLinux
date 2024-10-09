import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.spi.ToolProvider;

public class VerboseOutTest {

    public static void main(String... args) throws Exception {
        new VerboseOutTest().run();
    }

    void run() throws Exception {
        String className = getClass().getName();
        Path testSrc = Paths.get(System.getProperty("test.src"));
        Path file = testSrc.resolve(className + ".java");
        ToolProvider javac = ToolProvider.findFirst("javac").orElseThrow();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        int rc = javac.run(pw, pw, "-d", ".", "-verbose", file.toString());
        String log = sw.toString();
        System.out.println(log);
        if (rc != 0) {
            throw new Exception("compilation failed: rc=" + rc);
        }
        String expected = "[wrote " + Paths.get(".").resolve(className + ".class") + "]";
        if (!log.contains(expected)) {
            throw new Exception("expected output not found: " + expected);
        }
    }
}
