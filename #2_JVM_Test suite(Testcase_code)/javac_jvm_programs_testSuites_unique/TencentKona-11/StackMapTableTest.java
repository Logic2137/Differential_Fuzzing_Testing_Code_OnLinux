import java.io.*;
import java.util.*;

public class StackMapTableTest {

    public static void main(String... args) throws Exception {
        new StackMapTableTest().run();
    }

    void run() throws Exception {
        String testClasses = System.getProperty("test.classes");
        String out = javap("-v", "-classpath", testClasses, A.class.getName());
        String nl = System.getProperty("line.separator");
        out = out.replaceAll(nl, "\n");
        if (out.contains("\n\n\n"))
            error("double blank line found");
        String expect = "      StackMapTable: number_of_entries = 2\n" + "        frame_type = 252 \n" + "          offset_delta = 2\n" + "          locals = [ int ]\n" + "        frame_type = 250 \n" + "          offset_delta = 18\n";
        if (!out.contains(expect))
            error("expected text not found");
        if (errors > 0)
            throw new Exception(errors + " errors found");
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

    public class A {

        public void a() {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
            }
        }

        public void b() {
        }

        public void c() {
        }
    }
}
