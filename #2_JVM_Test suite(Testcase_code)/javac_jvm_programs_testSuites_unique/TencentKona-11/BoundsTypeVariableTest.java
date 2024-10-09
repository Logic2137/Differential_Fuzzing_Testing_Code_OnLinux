



import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import static java.nio.file.StandardOpenOption.*;

public class BoundsTypeVariableTest {
    public static void main(String... args) throws Exception {
        new BoundsTypeVariableTest().run();
    }

    void run() throws Exception {
        File srcDir = new File("src");
        srcDir.mkdirs();
        File classesDir = new File("classes");
        classesDir.mkdirs();
        final String expect = "public abstract <U extends java.lang.Object> U doit();";
        List<String> contents = new ArrayList<>();
        contents.add("abstract class X {");
        contents.add(expect);
        contents.add("}");

        File f = writeFile(new File(srcDir, "X.java"), contents);
        javac("-d", classesDir.getPath(), f.getPath());
        String out = javap("-p", "-v", new File(classesDir, "X.class").getPath());
        if (!out.contains(expect)) {
            throw new Exception("expected pattern not found: " + expect);
        }
    }

    File writeFile(File f, List<String> body) throws IOException {
        Files.write(f.toPath(), body, Charset.defaultCharset(),
                CREATE, TRUNCATE_EXISTING);
        return f;
    }

    void javac(String... args) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        int rc = com.sun.tools.javac.Main.compile(args, pw);
        pw.flush();
        String out = sw.toString();
        if (!out.isEmpty())
            System.err.println(out);
        if (rc != 0)
            throw new Exception("compilation failed");
    }

    String javap(String... args) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        int rc = com.sun.tools.javap.Main.run(args, pw);
        pw.flush();
        String out = sw.toString();
        if (!out.isEmpty())
            System.err.println(out);
        if (rc != 0)
            throw new Exception("javap failed");
        return out;
    }
}
