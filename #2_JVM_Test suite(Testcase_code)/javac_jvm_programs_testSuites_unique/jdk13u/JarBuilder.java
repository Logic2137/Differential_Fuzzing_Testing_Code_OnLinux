import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarBuilder {

    final private String name;

    final private Attributes attributes = new Attributes();

    final private List<Entry> entries = new ArrayList<>();

    public JarBuilder(String name) {
        this.name = name;
        attributes.putValue("Manifest-Version", "1.0");
        attributes.putValue("Created-By", "1.9.0-internal (Oracle Corporation)");
    }

    public JarBuilder addAttribute(String name, String value) {
        attributes.putValue(name, value);
        return this;
    }

    public JarBuilder addEntry(String name, byte[] bytes) {
        entries.add(new Entry(name, bytes));
        return this;
    }

    public void build() throws IOException {
        try (OutputStream os = Files.newOutputStream(Paths.get(name));
            JarOutputStream jos = new JarOutputStream(os)) {
            JarEntry me = new JarEntry("META-INF/MANIFEST.MF");
            jos.putNextEntry(me);
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().putAll(attributes);
            manifest.write(jos);
            jos.closeEntry();
            entries.forEach(e -> {
                JarEntry je = new JarEntry(e.name);
                try {
                    jos.putNextEntry(je);
                    jos.write(e.bytes);
                    jos.closeEntry();
                } catch (IOException iox) {
                    throw new RuntimeException(iox);
                }
            });
        } catch (RuntimeException x) {
            Throwable t = x.getCause();
            if (t instanceof IOException) {
                IOException iox = (IOException) t;
                throw iox;
            }
            throw x;
        }
    }

    private static class Entry {

        String name;

        byte[] bytes;

        Entry(String name, byte[] bytes) {
            this.name = name;
            this.bytes = bytes;
        }
    }

    public static void main(String[] args) throws IOException {
        JarBuilder jb = new JarBuilder("version.jar");
        jb.addAttribute("Multi-Release", "true");
        String s = "something to say";
        byte[] bytes = s.getBytes();
        jb.addEntry("version/Version.class", bytes);
        jb.addEntry("README", bytes);
        jb.addEntry("version/Version.java", bytes);
        jb.build();
    }
}
