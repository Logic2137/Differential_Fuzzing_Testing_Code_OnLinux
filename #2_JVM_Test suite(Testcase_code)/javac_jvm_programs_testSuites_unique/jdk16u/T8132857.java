



import java.io.*;
import java.nio.charset.Charset;

public class T8132857 {
    public static void main(String... args) throws Exception{
        new T8132857().run();
    }

    void run() throws IOException {
        if (!Charset.defaultCharset().equals(Charset.forName("UTF-8"))) {
            System.err.println("skipping test, default charset is not UTF-8");
            return;
        }

        File src = new File("src");
        src.mkdirs();
        try (OutputStream out = new FileOutputStream(new File(src, "Test.java"))) {
            out.write('/');
            out.write('/');
            out.write(0b1100_0000);
            out.write('a');
        }

        try (StringWriter out = new StringWriter(); PrintWriter pw = new PrintWriter(out)) {
            int rc = com.sun.tools.javac.Main.compile(new String[] {"-XDrawDiagnostics", "src/Test.java"}, pw);

            pw.flush();

            String lineSeparator = System.getProperty("line.separator");
            String expected =
                    "Test.java:1:3: compiler.err.illegal.char.for.encoding: C0, UTF-8" + lineSeparator +
                    "1 error" + lineSeparator;
            String actual = out.toString();

            System.err.println(actual);

            if (rc == 0) {
                throw new Error("compilation unexpectedly passed: " + rc);
            }

            if (!expected.equals(actual)) {
                throw new Error("unexpected output: " + actual);
            }
        }
    }

}

