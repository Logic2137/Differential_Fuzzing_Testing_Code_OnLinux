import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LotsOfCloses {

    private static final Random RAND = new Random();

    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("tmp");
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            long start = System.currentTimeMillis();
            while ((System.currentTimeMillis() - start) < 5000) {
                test(dir, pool);
            }
        } finally {
            pool.shutdown();
        }
    }

    static void test(Path dir, ExecutorService pool) throws Exception {
        WatchService watcher = FileSystems.getDefault().newWatchService();
        dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
        Future<Void> closeResult;
        Future<Boolean> registerResult;
        if (RAND.nextBoolean()) {
            closeResult = pool.submit(newCloserTask(watcher));
            registerResult = pool.submit(newRegisterTask(watcher, dir));
        } else {
            registerResult = pool.submit(newRegisterTask(watcher, dir));
            closeResult = pool.submit(newCloserTask(watcher));
        }
        closeResult.get();
        registerResult.get();
    }

    static Callable<Void> newCloserTask(WatchService watcher) {
        return () -> {
            try {
                watcher.close();
                return null;
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        };
    }

    static Callable<Boolean> newRegisterTask(WatchService watcher, Path dir) {
        return () -> {
            try {
                dir.register(watcher, StandardWatchEventKinds.ENTRY_DELETE);
                return true;
            } catch (ClosedWatchServiceException e) {
                return false;
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        };
    }
}
