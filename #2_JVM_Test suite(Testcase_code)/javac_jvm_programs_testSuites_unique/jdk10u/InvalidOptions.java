



import java.io.*;
import java.util.zip.*;

public class InvalidOptions {
    int errorCount;
    String log;

    public static void main(String[] args) throws Exception {
        new InvalidOptions().run();
    }

    void run() throws Exception {
        test(2, "-b", "Error: unknown option: -b",
                      "Usage: javap <options> <classes>",
                      "use -help for a list of possible options");
        if (errorCount > 0)
            throw new Exception(errorCount + " errors received");
    }

    void test(int expect, String option, String ... expectedOutput) {
        String output = runJavap(expect, option);
        verify(output, expectedOutput);
    }

    String runJavap(int expect, String... option) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        int rc = com.sun.tools.javap.Main.run(option, pw);
        pw.close();
        System.out.println("javap prints:");
        System.out.println(sw);
        if (rc != expect)
           throw new Error("Expect to return " + expect + ", but return " + rc);
        return sw.toString();
    }

    void verify(String output, String... expects) {
        for (String expect: expects) {
            if (!output.contains(expect))
                error(expect + " not found");
        }
    }

    void error(String msg) {
        System.err.println(msg);
        errorCount++;
    }
}
