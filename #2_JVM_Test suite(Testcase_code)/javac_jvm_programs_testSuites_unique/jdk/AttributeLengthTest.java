



import java.io.*;


import java.util.*;
import java.util.Properties;

public class AttributeLengthTest {
    public static void main(String... args) throws Exception {
        String testClasses = System.getProperty("test.classes");
        String fileSep = System.getProperty("file.separator");

        String[] opts = { "-v", testClasses + fileSep + "JavapBug.class" };
        StringWriter sw = new StringWriter();
        PrintWriter pout = new PrintWriter(sw);

        com.sun.tools.javap.Main.run(opts, pout);
        pout.flush();
        if (sw.getBuffer().indexOf("Error: Fatal error: attribute Code too big to handle") == -1) {
            throw new Exception("unexpected javap output");
        }
    }
}
