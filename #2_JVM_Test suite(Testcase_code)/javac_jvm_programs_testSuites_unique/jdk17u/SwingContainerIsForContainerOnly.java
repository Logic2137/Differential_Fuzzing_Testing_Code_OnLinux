import java.awt.Container;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import javax.swing.SwingContainer;

public final class SwingContainerIsForContainerOnly {

    public static void main(String[] args) throws IOException {
        FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));
        fs.getFileStores();
        Files.walkFileTree(fs.getPath("/modules/java.desktop"), new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                file = file.subpath(2, file.getNameCount());
                String name = file.toString();
                if (name.endsWith(".class")) {
                    name = name.substring(0, name.indexOf(".")).replace('/', '.');
                    final Class<?> type;
                    try {
                        type = Class.forName(name, false, null);
                    } catch (Throwable e) {
                        return FileVisitResult.CONTINUE;
                    }
                    if (type.isAnnotationPresent(SwingContainer.class)) {
                        if (!Container.class.isAssignableFrom(type)) {
                            System.err.println("Wrong annotation for: " + type);
                            throw new RuntimeException();
                        }
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
