


import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static java.nio.file.StandardWatchEventKinds.*;

public class UpdateInterference {

    private static volatile boolean stop;

    public static void main(String[] args) throws IOException, InterruptedException {
        final Path root = Files.createTempDirectory("test");
        final Path foo = root.resolve("foo");
        final Path bar = root.resolve("bar");
        final Path baz = root.resolve("baz");

        Files.createDirectory(foo);
        Files.createDirectory(bar);
        Files.createDirectory(baz);

        try (final WatchService watcher = root.getFileSystem().newWatchService()) {
            final WatchKey fooKey = foo.register(watcher, ENTRY_CREATE);
            final WatchKey barKey = bar.register(watcher, ENTRY_CREATE);

            Thread t1 = null;
            Thread t2 = null;
            try {
                t1 = new Thread() {

                    @Override
                    public void run() {
                        while (!stop) {
                            try {
                                final Path temp = Files.createTempFile(foo, "temp", ".tmp");
                                Files.delete(temp);
                                Thread.sleep(10);
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                };

                t2 = new Thread() {

                    @Override
                    public void run() {
                        WatchKey bazKeys[] = new WatchKey[32];
                        while (!stop) {
                            try {
                                for( int i = 0; i < bazKeys.length; i++) {
                                    bazKeys[i] = baz.register(watcher, ENTRY_CREATE);
                                }
                                for( int i = 0; i < bazKeys.length; i++) {
                                    bazKeys[i].cancel();
                                }
                                Thread.sleep(1);
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                };

                t1.start();
                t2.start();

                long time = System.currentTimeMillis();
                while ((System.currentTimeMillis() - time) < 15000) {
                    final WatchKey key = watcher.poll(60, TimeUnit.SECONDS);
                    if (key == null) continue;

                    if (key != fooKey) {
                        List<WatchEvent<?>> pollEvents = key.pollEvents();
                        for (WatchEvent<?> watchEvent : pollEvents) {
                            System.out.println(watchEvent.count() + " " +
                                            watchEvent.kind() + " " +
                                            watchEvent.context());
                        }
                        throw new RuntimeException("Event received for unexpected key");
                    }
                    key.reset();
                }
            } finally {
                
                
                stop = true;

                
                if (t1 != null) {
                    t1.join();
                }

                if (t2 != null) {
                    t2.join();
                }
            }
        } finally {
            
            Files.delete(foo);
            Files.delete(bar);
            Files.delete(baz);
            Files.delete(root);
        }
    }
}

