import java.io.*;
import java.util.*;

public class WhitespaceTest {

    public static void main(String... args) throws Exception {
        new WhitespaceTest().run();
    }

    void run() throws Exception {
        test("-v", "java.lang.String");
        test("-XDtab:1", "-v", "java.lang.String");
        String testClasses = System.getProperty("test.classes");
        for (int i = 10; i < 40; i++) test("-XDtab:" + i, "-v", "-classpath", testClasses, "WhitespaceTest$HelloWorld");
        if (errors > 0)
            throw new Exception(errors + " errors found");
    }

    void test(String... args) throws Exception {
        String slash = "/";
        String doubleSlash = slash + slash;
        System.out.println("test: " + Arrays.asList(args));
        String out = javap(args);
        for (String line : out.split("[\r\n]+")) {
            if (line.endsWith(" "))
                error("line has trailing whitespace: " + line);
            int comment = line.indexOf(doubleSlash);
            if (comment > 0 && line.charAt(comment - 1) != ' ') {
                if (!line.matches(".*\\bfile:/{3}.*"))
                    error("no space before comment: " + line);
            }
            if (line.matches(" +}"))
                error("bad indentation: " + line);
        }
    }

    String javap(String... args) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        int rc = com.sun.tools.javap.Main.run(args, out);
        out.close();
        System.out.println(sw.toString());
        if (rc < 0)
            throw new Exception("javap exited, rc=" + rc);
        return sw.toString();
    }

    void error(String msg) {
        System.out.println("Error: " + msg);
        errors++;
    }

    int errors;

    static class HelloWorld {

        public static void main(String... args) {
            System.out.println("Hello World!");
        }
    }
}
