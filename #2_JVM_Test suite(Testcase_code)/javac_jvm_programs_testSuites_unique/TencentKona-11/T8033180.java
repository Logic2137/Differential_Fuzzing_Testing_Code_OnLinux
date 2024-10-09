



import java.io.*;
import java.util.*;

public class T8033180 {

    public static void main(String... args) throws Exception {
        new T8033180().run();
    }

    void run() throws Exception {
        
        if (lineSep.equals(nl))
            return;

        test("-help");
        test("-version");

        if (errors > 0)
            throw new Exception(errors + " errors occurred");
    }

    static final String lineSep = System.getProperty("line.separator");
    static final String nl = "\n";

    void test(String... opts) throws Exception {
        System.err.println("test " + Arrays.asList(opts));
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(opts));
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        int rc = com.sun.tools.javap.Main.run(args.toArray(new String[args.size()]), pw);
        pw.close();
        String out = sw.toString();
        if (rc != 0)
            throw new Exception("javap failed unexpectedly: rc=" + rc);

        
        String out2 = out.replace(lineSep, "");

        
        int count = 0;
        int i = out2.indexOf(nl, 0);
        while (i != -1) {
            count++;
            i = out2.indexOf(nl, i + nl.length());
        }

        if (count > 0)
            error(count + " newline characters found");
    }

    void error(String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    int errors = 0;
}

