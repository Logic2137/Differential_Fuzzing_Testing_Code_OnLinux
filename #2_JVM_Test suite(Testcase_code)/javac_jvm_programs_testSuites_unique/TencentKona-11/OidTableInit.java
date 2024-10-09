import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import sun.security.x509.AlgorithmId;

public class OidTableInit {

    public static void main(String[] args) throws Throwable {
        final String[] algorithmNames = { "PBKDF2WITHHMACSHA1", "PBEWITHMD5ANDDES", "DSA", "SHA384WITHRSA", "RSA", "SHA1WITHDSA", "SHA512WITHRSA", "MD2WITHRSA", "PBEWITHSHA1ANDDESEDE", "SHA1WITHRSA", "DIFFIEHELLMAN", "MD5WITHRSA", "PBEWITHSHA1ANDRC2_40", "SHA256WITHRSA" };
        final int THREADS = 2;
        final ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        final CountDownLatch startingGate = new CountDownLatch(THREADS);
        final Runnable r = new Runnable() {

            public void run() {
                startingGate.countDown();
                do {
                } while (startingGate.getCount() > 0);
                try {
                    for (String algorithmName : algorithmNames) AlgorithmId.get(algorithmName);
                } catch (Throwable fail) {
                    throw new AssertionError(fail);
                }
            }
        };
        final ArrayList<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) futures.add(pool.submit(r));
        pool.shutdown();
        for (Future<?> future : futures) future.get();
    }
}
