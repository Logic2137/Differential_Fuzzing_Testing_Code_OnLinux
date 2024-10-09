import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.spi.ToolProvider;

public class VerboseClassPathTest {

    public static void main(String... args) throws Exception {
        new VerboseClassPathTest().run();
    }

    void run() throws Exception {
        String className = getClass().getName();
        Path testSrc = Paths.get(System.getProperty("test.src"));
        Path file = testSrc.resolve(className + ".java");
        ToolProvider javac = ToolProvider.findFirst("javac").orElseThrow();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        int rc = javac.run(pw, pw, "-d", ",", "-source", "8", "-target", "8", "-verbose", file.toString());
        String log = sw.toString();
        System.out.println(log);
        if (rc != 0) {
            throw new Exception("compilation failed: rc=" + rc);
        }
        String expect = "[search path for class files:";
        long count = new BufferedReader(new StringReader(log)).lines().filter(line -> line.startsWith(expect)).count();
        if (count != 1) {
            throw new Exception("expected '" + expect + "' to appear once, actual: " + count);
        }
    }
}
