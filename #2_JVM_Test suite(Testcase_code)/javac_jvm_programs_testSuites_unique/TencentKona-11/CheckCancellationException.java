

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingWorker;


public final class CheckCancellationException {

    private static final CountDownLatch go = new CountDownLatch(1);

    public static void main(final String[] args) throws Exception {
        SwingWorker<?, ?> worker = new SwingWorker() {
            protected Void doInBackground() {
                go.countDown();
                while (!Thread.interrupted()) ;
                return null;
            }
        };
        worker.execute();
        go.await();
        worker.cancel(true);
        try {
            worker.get();
        } catch (final CancellationException expected) {
            
            return;
        }
        throw new RuntimeException("CancellationException was not thrown");
    }
}
