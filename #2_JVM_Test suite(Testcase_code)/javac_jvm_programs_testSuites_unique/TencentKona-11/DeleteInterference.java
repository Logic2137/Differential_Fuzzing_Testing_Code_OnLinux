



import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.System.out;
import static java.nio.file.StandardWatchEventKinds.*;

public class DeleteInterference {

    private static final int ITERATIONS_COUNT = 1024;

    
    public static void main(String[] args) throws Exception {
        Path testDir = Paths.get(System.getProperty("test.dir", "."));
        Path dir = Files.createTempDirectory(testDir, "DeleteInterference");
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            Future<?> task1 = pool.submit(() -> openAndCloseWatcher(dir));
            Future<?> task2 = pool.submit(() -> deleteAndRecreateDirectory(dir));
            task1.get();
            task2.get();
        } finally {
            pool.shutdown();
        }
    }

    private static void openAndCloseWatcher(Path dir) {
        FileSystem fs = FileSystems.getDefault();
        for (int i = 0; i < ITERATIONS_COUNT; i++) {
            out.printf("open %d begin%n", i);
            try (WatchService watcher = fs.newWatchService()) {
                dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            } catch (IOException ioe) {
                
            } finally {
                out.printf("open %d end%n", i);
            }
        }
    }

    private static void deleteAndRecreateDirectory(Path dir) {
        for (int i = 0; i < ITERATIONS_COUNT; i++) {
            out.printf("del %d begin%n", i);
            try {
                deleteFileTree(dir);
                Path subdir = Files.createDirectories(dir.resolve("subdir"));
                Files.createFile(subdir.resolve("test"));
            } catch (IOException ioe) {
                
            } finally {
                out.printf("del %d end%n", i);
            }
        }
    }

    private static void deleteFileTree(Path file) {
        try {
            if (Files.isDirectory(file)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(file)) {
                    for (Path pa : stream) {
                        deleteFileTree(pa);
                    }
                }
            }
            Files.delete(file);
        } catch (IOException ioe) {
            
        }
    }
}
