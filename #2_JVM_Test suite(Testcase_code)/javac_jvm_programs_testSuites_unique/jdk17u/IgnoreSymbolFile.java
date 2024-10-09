import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;

public class IgnoreSymbolFile {

    public static void main(String... args) throws Exception {
        IgnoreSymbolFile test = new IgnoreSymbolFile();
        test.run();
    }

    void run() throws Exception {
        String body = "package p;\n" + "import sun.reflect.annotation.*;\n" + "class X {\n" + "    ExceptionProxy proxy;" + "}";
        writeFile("src/p/X.java", body);
        new File("classes").mkdirs();
        int rc1 = compile("-d", "classes", "--state-dir=classes", "-Werror", "src");
        if (rc1 == 0)
            error("compilation succeeded unexpectedly");
        int rc2 = compile("-d", "classes", "--state-dir=classes", "-Werror", "-XDignore.symbol.file=true", "src");
        if (rc2 != 0)
            error("compilation failed unexpectedly: rc=" + rc2);
        if (errors > 0)
            throw new Exception(errors + " errors occurred");
    }

    int compile(String... args) throws ReflectiveOperationException {
        System.err.println("compile: " + Arrays.toString(args));
        Class<?> c = Class.forName("com.sun.tools.sjavac.Main");
        Method m = c.getDeclaredMethod("go", String[].class);
        int rc = (Integer) m.invoke(null, (Object) args);
        System.err.println("rc=" + rc);
        return rc;
    }

    void writeFile(String path, String body) throws IOException {
        File f = new File(path);
        if (f.getParentFile() != null)
            f.getParentFile().mkdirs();
        try (FileWriter w = new FileWriter(f)) {
            w.write(body);
        }
    }

    void error(String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    int errors;
}
