



import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapWithSecurityManager {
    public static void main(String[] args) throws IOException {
        Path tempFile = Files.createTempFile("test", "test");
        try (FileChannel ch = FileChannel.open(tempFile)) {
             System.setSecurityManager(new SecurityManager());
             ch.map(FileChannel.MapMode.READ_ONLY, 0, 0);
        }
    }
}
