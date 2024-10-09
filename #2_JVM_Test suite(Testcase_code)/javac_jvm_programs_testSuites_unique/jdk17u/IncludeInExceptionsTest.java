import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import static java.nio.charset.StandardCharsets.UTF_8;

public class IncludeInExceptionsTest {

    static final String FILENAME = "Unique-Filename-Expected-In_Msg.jar";

    static final byte[] INVALID_MANIFEST = ("Manifest-Version: 1.0\r\n" + "\r\n" + "Illegal\r\n" + "\r\n").getBytes(UTF_8);

    static String createJarInvalidManifest(String jar) throws IOException {
        try (OutputStream out = Files.newOutputStream(Paths.get(jar));
            JarOutputStream jos = new JarOutputStream(out)) {
            JarEntry je = new JarEntry(JarFile.MANIFEST_NAME);
            jos.putNextEntry(je);
            jos.write(INVALID_MANIFEST);
            jos.closeEntry();
        }
        return jar;
    }

    static void test(Callable<?> attempt, boolean includeInExceptions) throws Exception {
        try {
            attempt.call();
            throw new AssertionError("Excpected Exception not thrown");
        } catch (IOException e) {
            boolean foundFileName = e.getMessage().contains(FILENAME);
            if (includeInExceptions && !foundFileName) {
                throw new AssertionError("JAR file name expected but not found in error message");
            } else if (foundFileName && !includeInExceptions) {
                throw new AssertionError("JAR file name found but should not be in error message");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        boolean includeInExceptions;
        if (args.length > 0) {
            includeInExceptions = true;
            System.out.println("**** Running test WITH -Djdk.includeInExceptions=jar");
        } else {
            includeInExceptions = false;
            System.out.println("**** Running test WITHOUT -Djdk.includeInExceptions=jar");
        }
        test(() -> new JarFile(createJarInvalidManifest(FILENAME)).getManifest(), includeInExceptions);
        test(() -> new JarFile(createJarInvalidManifest("Verifying-" + FILENAME), true).getManifest(), includeInExceptions);
    }
}
