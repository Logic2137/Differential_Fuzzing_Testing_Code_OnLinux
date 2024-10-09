import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import com.sun.tools.doclint.DocLint;

public class ResourceTest {

    public static void main(String... args) throws Exception {
        Locale prev = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
        try {
            new ResourceTest().run();
        } finally {
            Locale.setDefault(prev);
        }
    }

    public void run() throws Exception {
        test(Arrays.asList("-help"), Arrays.asList("Usage:", "Options"));
        test(Arrays.asList("-foo"), Arrays.asList("bad option: -foo"));
    }

    void test(List<String> opts, List<String> expects) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            new DocLint().run(pw, opts.toArray(new String[opts.size()]));
        } catch (DocLint.BadArgs e) {
            pw.println("BadArgs: " + e.getMessage());
        } catch (IOException e) {
            pw.println("IOException: " + e.getMessage());
        } finally {
            pw.close();
        }
        String out = sw.toString();
        if (!out.isEmpty()) {
            System.err.println(out);
        }
        for (String e : expects) {
            if (!out.contains(e))
                throw new Exception("expected string not found: " + e);
        }
    }
}
