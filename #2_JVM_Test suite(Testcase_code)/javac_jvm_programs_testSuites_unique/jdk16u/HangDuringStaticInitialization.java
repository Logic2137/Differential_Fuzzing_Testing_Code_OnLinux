

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


public final class HangDuringStaticInitialization {

    public static void main(final String[] args) throws Exception {
        FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));
        test(fs, "/modules/java.desktop");
        test(fs, "/modules/java.datatransfer");
    }

    private static void test(FileSystem fs, String s) throws Exception {
        Files.walkFileTree(fs.getPath(s), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) {
                file = file.subpath(2, file.getNameCount());
                String name = file.toString();
                if (name.endsWith(".class")) {
                    name = name.substring(0, name.indexOf(".")).replace('/', '.');
                    try {
                        Class.forName(name, true, null);
                    } catch (Throwable e) {
                        
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
