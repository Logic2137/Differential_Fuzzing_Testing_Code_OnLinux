import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;

public class T8147801 {

    public static void main(String... args) throws Exception {
        new T8147801().run();
    }

    void run() throws Exception {
        initJar();
        test(false);
        test(true);
        if (errors > 0) {
            throw new Exception(errors + " errors occurred");
        }
    }

    void test(boolean withOption) {
        System.err.println("Testing " + (withOption ? "with" : "without") + " option");
        try {
            String dump = "";
            RootDoc root = getRootDoc(withOption);
            for (ClassDoc cd : root.specifiedClasses()) {
                dump += dump(cd);
            }
            if (dump.contains("lib.Lib2.i")) {
                if (!withOption) {
                    error("control case failed: Lib2 class file was read, unexpectedly, without using option");
                }
            } else {
                if (withOption) {
                    error("test case failed: could not read Lib2 class file, using option");
                }
            }
        } catch (ClosedFileSystemException e) {
            error("Unexpected exception: " + e);
        }
        System.err.println();
    }

    RootDoc getRootDoc(boolean withOption) {
        List<String> opts = new ArrayList<>();
        if (withOption)
            opts.add("-XDfileManager.deferClose=10");
        opts.add("-doclet");
        opts.add(getClass().getName());
        opts.add("-classpath");
        opts.add(jarPath.toString());
        opts.add(Paths.get(System.getProperty("test.src"), "p", "Test.java").toString());
        System.err.println("javadoc opts: " + opts);
        int rc = com.sun.tools.javadoc.Main.execute("javadoc", getClass().getClassLoader(), opts.toArray(new String[opts.size()]));
        if (rc != 0) {
            error("unexpected exit from javadoc or doclet: " + rc);
        }
        return cachedRoot;
    }

    String dump(ClassDoc cd) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        dump(pw, "", cd);
        String out = sw.toString();
        System.err.println(out);
        return out;
    }

    void dump(PrintWriter out, String prefix, ClassDoc cd) {
        out.println(prefix + "class: " + cd);
        for (FieldDoc fd : cd.fields()) {
            out.println(prefix + "  " + fd);
            if (fd.type().asClassDoc() != null) {
                dump(out, prefix + "    ", fd.type().asClassDoc());
            }
        }
    }

    void initJar() throws IOException {
        String testClassPath = System.getProperty("test.class.path", "");
        Path jarsrc = Stream.of(testClassPath.split(File.pathSeparator)).map(Paths::get).filter(e -> e.endsWith("jarsrc")).findAny().orElseThrow(() -> new InternalError("jarsrc not found"));
        jarPath = Paths.get("lib.jar");
        try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(jarPath))) {
            String[] classNames = { "Lib1.class", "Lib2.class" };
            for (String cn : classNames) {
                out.putNextEntry(new JarEntry("lib/" + cn));
                Path libClass = jarsrc.resolve("lib").resolve(cn);
                out.write(Files.readAllBytes(libClass));
            }
        }
    }

    void error(String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    Path jarPath;

    int errors;

    static RootDoc cachedRoot;

    public static boolean start(RootDoc root) {
        cachedRoot = root;
        return true;
    }
}
