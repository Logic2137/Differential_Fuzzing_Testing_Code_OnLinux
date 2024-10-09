import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import com.sun.tools.javac.code.Source;

public class ReleaseOptionCurrent {

    public static void main(String... args) throws IOException {
        new ReleaseOptionCurrent().run();
    }

    void run() throws IOException {
        String lineSep = System.getProperty("line.separator");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> input = fm.getJavaFileObjects(System.getProperty("test.src") + "/ReleaseOption.java");
            List<String> options = Arrays.asList("-d", ".", "--release", Source.DEFAULT.name);
            boolean result = compiler.getTask(null, fm, null, options, null, input).call();
            if (!result) {
                throw new AssertionError("Compilation failed unexpectedly.");
            }
        }
    }
}
