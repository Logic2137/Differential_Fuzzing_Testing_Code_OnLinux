



import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class ReleaseOptionThroughAPI {
    public static void main(String... args) throws IOException {
        new ReleaseOptionThroughAPI().run();
    }

    void run() throws IOException {
        String lineSep = System.getProperty("line.separator");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null);
             StringWriter out = new StringWriter();
             PrintWriter outWriter = new PrintWriter(out)) {
            Iterable<? extends JavaFileObject> input =
                    fm.getJavaFileObjects(System.getProperty("test.src") + "/ReleaseOption.java");
            List<String> options = Arrays.asList("--release", "7", "-XDrawDiagnostics", "-Xlint:-options");

            compiler.getTask(outWriter, fm, null, options, null, input).call();
            String expected =
                    "ReleaseOption.java:9:49: compiler.err.doesnt.exist: java.util.stream" + lineSep +
                    "1 error" + lineSep;
            if (!expected.equals(out.toString())) {
                throw new AssertionError("Unexpected output: " + out.toString());
            }
        }
    }
}
