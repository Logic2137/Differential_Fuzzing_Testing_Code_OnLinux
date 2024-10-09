
package jdk.test.lib.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class JarUtils {

    private JarUtils() {
    }

    public static void createJarFile(Path jarfile, Manifest man, Path dir, Path... files) throws IOException {
        Path parent = jarfile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        List<Path> entries = findAllRegularFiles(dir, files);
        try (OutputStream out = Files.newOutputStream(jarfile);
            JarOutputStream jos = new JarOutputStream(out)) {
            if (man != null) {
                JarEntry je = new JarEntry(JarFile.MANIFEST_NAME);
                jos.putNextEntry(je);
                man.write(jos);
                jos.closeEntry();
            }
            for (Path entry : entries) {
                String name = toJarEntryName(entry);
                jos.putNextEntry(new JarEntry(name));
                Files.copy(dir.resolve(entry), jos);
                jos.closeEntry();
            }
        }
    }

    public static void createJarFile(Path jarfile, Path dir, Path... files) throws IOException {
        createJarFile(jarfile, null, dir, files);
    }

    public static void createJarFile(Path jarfile, Path dir) throws IOException {
        createJarFile(jarfile, dir, Paths.get("."));
    }

    public static void createJarFile(Path jarfile, Path dir, String... input) throws IOException {
        Path[] paths = Stream.of(input).map(Paths::get).toArray(Path[]::new);
        createJarFile(jarfile, dir, paths);
    }

    public static void updateJarFile(Path jarfile, Path dir, Path... files) throws IOException {
        List<Path> entries = findAllRegularFiles(dir, files);
        Set<String> names = entries.stream().map(JarUtils::toJarEntryName).collect(Collectors.toSet());
        Path tmpfile = Files.createTempFile("jar", "jar");
        try (OutputStream out = Files.newOutputStream(tmpfile);
            JarOutputStream jos = new JarOutputStream(out)) {
            try (JarFile jf = new JarFile(jarfile.toString())) {
                Enumeration<JarEntry> jentries = jf.entries();
                while (jentries.hasMoreElements()) {
                    JarEntry jentry = jentries.nextElement();
                    if (!names.contains(jentry.getName())) {
                        jos.putNextEntry(copyEntry(jentry));
                        jf.getInputStream(jentry).transferTo(jos);
                    }
                }
            }
            for (Path entry : entries) {
                String name = toJarEntryName(entry);
                jos.putNextEntry(new JarEntry(name));
                Files.copy(dir.resolve(entry), jos);
            }
        }
        Files.move(tmpfile, jarfile, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void updateJarFile(Path jarfile, Path dir) throws IOException {
        updateJarFile(jarfile, dir, Paths.get("."));
    }

    @Deprecated
    public static void createJar(String dest, String... files) throws IOException {
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(dest), new Manifest())) {
            for (String file : files) {
                System.out.println(String.format("Adding %s to %s", file, dest));
                jos.putNextEntry(new JarEntry(file));
                try (FileInputStream fis = new FileInputStream(file)) {
                    fis.transferTo(jos);
                } catch (FileNotFoundException e) {
                    jos.write(file.getBytes());
                }
            }
        }
        System.out.println();
    }

    @Deprecated
    public static void updateJar(String src, String dest, String... files) throws IOException {
        Map<String, Object> changes = new HashMap<>();
        boolean update = true;
        for (String file : files) {
            if (file.equals("-")) {
                update = false;
            } else if (update) {
                try {
                    Path p = Paths.get(file);
                    if (Files.exists(p)) {
                        changes.put(file, p);
                    } else {
                        changes.put(file, file);
                    }
                } catch (InvalidPathException e) {
                    changes.put(file, file);
                }
            } else {
                changes.put(file, Boolean.FALSE);
            }
        }
        updateJar(src, dest, changes);
    }

    @Deprecated
    public static void updateJar(String src, String dest, Map<String, Object> changes) throws IOException {
        changes = new HashMap<>(changes);
        System.out.printf("Creating %s from %s...\n", dest, src);
        if (dest.equals(src)) {
            throw new IOException("src and dest cannot be the same");
        }
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(dest))) {
            try (JarFile srcJarFile = new JarFile(src)) {
                Enumeration<JarEntry> entries = srcJarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (changes.containsKey(name)) {
                        System.out.println(String.format("- Update %s", name));
                        updateEntry(jos, name, changes.get(name));
                        changes.remove(name);
                    } else {
                        System.out.println(String.format("- Copy %s", name));
                        jos.putNextEntry(copyEntry(entry));
                        srcJarFile.getInputStream(entry).transferTo(jos);
                    }
                }
            }
            for (Map.Entry<String, Object> e : changes.entrySet()) {
                System.out.println(String.format("- Add %s", e.getKey()));
                updateEntry(jos, e.getKey(), e.getValue());
            }
        }
        System.out.println();
    }

    public static void updateManifest(String src, String dest, Manifest man) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        man.write(bout);
        updateJar(src, dest, Map.of(JarFile.MANIFEST_NAME, bout.toByteArray()));
    }

    private static void updateEntry(JarOutputStream jos, String name, Object content) throws IOException {
        if (content instanceof Boolean) {
            if (((Boolean) content).booleanValue()) {
                throw new RuntimeException("Boolean value must be FALSE");
            }
        } else {
            jos.putNextEntry(new JarEntry(name));
            if (content instanceof Path) {
                Files.newInputStream((Path) content).transferTo(jos);
            } else if (content instanceof byte[]) {
                jos.write((byte[]) content);
            } else if (content instanceof String) {
                jos.write(((String) content).getBytes());
            } else {
                throw new RuntimeException("Unknown type " + content.getClass());
            }
        }
    }

    private static String toJarEntryName(Path file) {
        Path normalized = file.normalize();
        return normalized.subpath(0, normalized.getNameCount()).toString().replace(File.separatorChar, '/');
    }

    private static List<Path> findAllRegularFiles(Path dir, Path[] files) throws IOException {
        List<Path> entries = new ArrayList<>();
        for (Path file : files) {
            try (Stream<Path> stream = Files.find(dir.resolve(file), Integer.MAX_VALUE, (p, attrs) -> attrs.isRegularFile())) {
                stream.map(dir::relativize).forEach(entries::add);
            }
        }
        return entries;
    }

    private static JarEntry copyEntry(JarEntry e1) {
        JarEntry e2 = new JarEntry(e1.getName());
        e2.setMethod(e1.getMethod());
        e2.setTime(e1.getTime());
        e2.setComment(e1.getComment());
        e2.setExtra(e1.getExtra());
        if (e1.getMethod() == JarEntry.STORED) {
            e2.setSize(e1.getSize());
            e2.setCrc(e1.getCrc());
        }
        return e2;
    }
}
