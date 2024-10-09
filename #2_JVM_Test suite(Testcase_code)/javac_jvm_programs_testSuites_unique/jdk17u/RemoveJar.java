import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.zip.ZipException;
import java.util.spi.ToolProvider;

public class RemoveJar {

    private final static String TEST_PKG = "testpkg";

    private final static String JAR_DIR = "testjar/" + TEST_PKG;

    private final static String FILE_NAME = "testjar.jar";

    private final static ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private final static PrintStream out = new PrintStream(baos);

    private final static ToolProvider JAR_TOOL = ToolProvider.findFirst("jar").orElseThrow(() -> new RuntimeException("jar tool not found"));

    private static void buildJar() throws IOException {
        mkdir(JAR_DIR);
        Path path = Paths.get(JAR_DIR);
        String src = "package " + TEST_PKG + ";\n" + "class Test {}\n";
        Files.write(Paths.get(JAR_DIR + "/Test.java"), src.getBytes());
        compile(JAR_DIR + "/Test.java");
        jar("-cf testjar.jar " + JAR_DIR);
    }

    public static void main(String[] args) throws Exception {
        buildJar();
        URLClassLoader loader = null;
        URL url = null;
        Path path = Paths.get(FILE_NAME);
        boolean useCacheFirst = Boolean.parseBoolean(args[0]);
        boolean useCacheSecond = Boolean.parseBoolean(args[1]);
        String firstClass = args[2];
        String secondClass = args[3];
        String subPath = args[4];
        try {
            String path_str = path.toUri().toURL().toString();
            URLConnection.setDefaultUseCaches("jar", useCacheFirst);
            url = new URL("jar", "", path_str + "!/" + subPath);
            loader = new URLClassLoader(new URL[] { url });
            loader.loadClass(firstClass);
        } catch (Exception e) {
            System.err.println("EXCEPTION: " + e);
        }
        try {
            URLConnection.setDefaultUseCaches("jar", useCacheSecond);
            loader.loadClass(secondClass);
        } catch (Exception e) {
            System.err.println("EXCEPTION: " + e);
        } finally {
            loader.close();
            Files.delete(path);
        }
    }

    private static Stream<Path> mkpath(String... args) {
        return Arrays.stream(args).map(d -> Paths.get(".", d.split("/")));
    }

    private static void mkdir(String cmdline) {
        System.out.println("mkdir -p " + cmdline);
        mkpath(cmdline.split(" +")).forEach(p -> {
            try {
                Files.createDirectories(p);
            } catch (IOException x) {
                throw new UncheckedIOException(x);
            }
        });
    }

    private static void jar(String cmdline) throws IOException {
        System.out.println("jar " + cmdline);
        baos.reset();
        ByteArrayOutputStream baes = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(baes);
        PrintStream saveErr = System.err;
        System.setErr(err);
        int rc = JAR_TOOL.run(out, err, cmdline.split(" +"));
        System.setErr(saveErr);
        if (rc != 0) {
            String s = baes.toString();
            if (s.startsWith("java.util.zip.ZipException: duplicate entry: ")) {
                throw new ZipException(s);
            }
            throw new IOException(s);
        }
    }

    private static void compile(String... args) {
        if (com.sun.tools.javac.Main.compile(args) != 0) {
            throw new RuntimeException("javac failed: args=" + Arrays.toString(args));
        }
    }
}
