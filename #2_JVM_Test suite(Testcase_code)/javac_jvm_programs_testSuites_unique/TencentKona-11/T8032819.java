



import java.io.*;
import java.util.*;

public class T8032819 {
    static class Fields {
        int f1;
        int f2;
    }

    public static void main(String... args) throws Exception {
        new T8032819().run();
    }

    void run() throws Exception {
        Class<?> clazz = Fields.class;
        test(clazz);
        test(clazz, "-c");
        test(clazz, "-l");
        test(clazz, "-l", "-c");
        test(clazz, "-v");
        test(clazz, "-v", "-c");
        test(clazz, "-v", "-l");
        test(clazz, "-v", "-l", "-c");

        if (errors > 0)
            throw new Exception(errors + " errors occurred");
    }

    static final String sep = System.getProperty("line.separator");
    static final String doubleBlankLine = sep + sep + sep;

    void test(Class<?> clazz, String... opts) throws Exception {
        System.err.println("test " + Arrays.asList(opts));
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(opts));
        args.addAll(Arrays.asList("-classpath", System.getProperty("test.classes")));
        args.add(clazz.getName());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        int rc = com.sun.tools.javap.Main.run(args.toArray(new String[args.size()]), pw);
        pw.close();
        String out = sw.toString();
        if (rc != 0)
            throw new Exception("javap failed unexpectedly: rc=" + rc);

        int count = 0;
        int i = out.indexOf(doubleBlankLine, 0);
        while (i != -1) {
            count++;
            i = out.indexOf(doubleBlankLine, i + doubleBlankLine.length());
        }

        if (count > 0)
            error(count + " double blank lines found");
    }

    void error(String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    int errors = 0;
}

