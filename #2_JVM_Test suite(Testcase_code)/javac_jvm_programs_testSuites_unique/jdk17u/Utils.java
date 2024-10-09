import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

class Utils {

    private Utils() {
    }

    static Path createJarFile(String name, String... entries) throws IOException {
        Path jarFile = Paths.get("basic.jar");
        Random rand = new Random();
        try (OutputStream out = Files.newOutputStream(jarFile);
            JarOutputStream jout = new JarOutputStream(out)) {
            int len = 100;
            for (String entry : entries) {
                JarEntry je = new JarEntry(entry);
                jout.putNextEntry(je);
                byte[] bytes = new byte[len];
                rand.nextBytes(bytes);
                jout.write(bytes);
                jout.closeEntry();
                len += 1024;
            }
        }
        return jarFile;
    }
}
