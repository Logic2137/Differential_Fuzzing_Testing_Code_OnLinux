



import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;

public class T7040592 {

    private static final String assertionErrorMsg =
            "null should be assignable to array type without a checkcast";

    public static void main(String[] args) {
        new T7040592().run();
    }

    void run() {
        check("-c", Paths.get(System.getProperty("test.classes"),
                "T7040592_01.class").toString());
    }

    void check(String... params) {
        StringWriter s;
        String out;
        try (PrintWriter pw = new PrintWriter(s = new StringWriter())) {
            com.sun.tools.javap.Main.run(params, pw);
            out = s.toString();
        }
        if (out.contains("checkcast")) {
            throw new AssertionError(assertionErrorMsg);
        }
    }

}

class T7040592_01 {
    static void handleArrays(Object [] a, Object [][] b, Object [][][] c) {
    }
    public static void main(String[] args) {
        Object a[];
        Object o = (a = null)[0];
        Object b[][];
        o = (b = null)[0][0];
        Object c[][][];
        o = (c = null)[0][0][0];
        handleArrays(null, null, null);
    }
}
