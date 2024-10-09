



import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;

public class RegisterDuringSelect {

    static Callable<Void> selectLoop(Selector sel, Phaser barrier) {
        return new Callable<Void>() {
            @Override
            public Void call() throws IOException {
                for (;;) {
                    try {
                        sel.select();
                    } catch (ClosedSelectorException ignore) {
                        return null;
                    }
                    if (sel.isOpen()) {
                        barrier.arriveAndAwaitAdvance();
                        System.out.println("selectLoop advanced ...");
                    } else {
                        
                        return null;
                    }
                }
            }
        };
    }
    
    public static void main(String args[]) throws Exception {
        Future<Void> result;

        ExecutorService pool = Executors.newFixedThreadPool(1);
        try (Selector sel = Selector.open()) {
            Phaser barrier = new Phaser(2);

            
            result = pool.submit(selectLoop(sel, barrier));

            Pipe p = Pipe.open();
            try {
                Pipe.SourceChannel sc = p.source();
                sc.configureBlocking(false);

                System.out.println("register ...");
                SelectionKey key = sc.register(sel, SelectionKey.OP_READ);
                if (!sel.keys().contains(key))
                    throw new RuntimeException("key not in key set");
                sel.wakeup();
                barrier.arriveAndAwaitAdvance();

                System.out.println("interestOps ...");
                key.interestOps(0);
                sel.wakeup();
                barrier.arriveAndAwaitAdvance();

                System.out.println("cancel ...");
                key.cancel();
                sel.wakeup();
                barrier.arriveAndAwaitAdvance();
                if (sel.keys().contains(key))
                    throw new RuntimeException("key not removed from key set");

            } finally {
                p.source().close();
                p.sink().close();
            }

        } finally {
            pool.shutdown();
        }

        
        result.get();

    }
}

