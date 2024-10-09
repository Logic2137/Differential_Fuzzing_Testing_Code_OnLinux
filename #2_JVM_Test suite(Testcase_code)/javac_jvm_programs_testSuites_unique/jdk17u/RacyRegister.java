import java.nio.channels.*;
import java.util.concurrent.*;
import java.util.Random;
import java.io.IOException;

public class RacyRegister {

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        try (Selector sel = Selector.open()) {
            int count = 100;
            while (count-- > 0) {
                final SocketChannel sc = SocketChannel.open();
                sc.configureBlocking(false);
                Future<Void> result = pool.submit(new Callable<Void>() {

                    public Void call() throws IOException {
                        sc.close();
                        return null;
                    }
                });
                SelectionKey key = null;
                try {
                    key = sc.register(sel, SelectionKey.OP_READ);
                } catch (ClosedChannelException ignore) {
                }
                result.get();
                if (key != null && key.isValid())
                    throw new RuntimeException("Key is valid");
            }
        } finally {
            pool.shutdown();
        }
    }
}
