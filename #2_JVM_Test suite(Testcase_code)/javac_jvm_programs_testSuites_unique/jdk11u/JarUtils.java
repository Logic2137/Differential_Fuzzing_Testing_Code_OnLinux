
package jaxp.library;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public final class JarUtils {
    private JarUtils() { }

    
    public static void createJarFile(Path jarfile, Manifest man, Path dir, Path... file)
        throws IOException
    {
        
        Path parent = jarfile.getParent();
        if (parent != null)
            Files.createDirectories(parent);

        List<Path> entries = new ArrayList<>();
        for (Path entry : file) {
            Files.find(dir.resolve(entry), Integer.MAX_VALUE,
                        (p, attrs) -> attrs.isRegularFile())
                    .map(e -> dir.relativize(e))
                    .forEach(entries::add);
        }

        try (OutputStream out = Files.newOutputStream(jarfile);
             JarOutputStream jos = new JarOutputStream(out))
        {
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

    
    public static void createJarFile(Path jarfile, Path dir, Path... file)
        throws IOException
    {
        createJarFile(jarfile, null, dir, file);
    }

    
    public static void createJarFile(Path jarfile, Path dir, String... input)
        throws IOException
    {
        Path[] paths = Stream.of(input).map(Paths::get).toArray(Path[]::new);
        createJarFile(jarfile, dir, paths);
    }

    
    public static void createJarFile(Path jarfile, Path dir) throws IOException {
        createJarFile(jarfile, dir, Paths.get("."));
    }

    
    private static String toJarEntryName(Path file) {
        Path normalized = file.normalize();
        return normalized.subpath(0, normalized.getNameCount())  
                .toString()
                .replace(File.separatorChar, '/');
    }
}
