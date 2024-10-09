import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetMacAddress implements Callable<Exception> {

    static final int NUM_THREADS = 5;

    static final int NUM_ITERS = 100;

    static volatile boolean failed;

    final String threadName;

    final NetworkInterface ni;

    final Phaser startingGate;

    public GetMacAddress(NetworkInterface ni, String name, Phaser phaser) {
        this.ni = ni;
        this.threadName = name;
        this.startingGate = phaser;
    }

    @Override
    public Exception call() {
        int count = 0;
        startingGate.arriveAndAwaitAdvance();
        try {
            for (int i = 0; i < NUM_ITERS; i++) {
                ni.getMTU();
                byte[] addr = ni.getHardwareAddress();
                if (addr == null) {
                    System.out.println(threadName + ". mac id is null");
                    failed = true;
                }
                count = count + 1;
                if (count % 100 == 0) {
                    System.out.println(threadName + ". count is " + count);
                }
            }
        } catch (Exception ex) {
            System.out.println(threadName + ". Not expecting exception:" + ex.getMessage());
            failed = true;
            return ex;
        }
        return null;
    }

    static final Predicate<NetworkInterface> hasHardwareAddress = ni -> {
        try {
            if (ni.getHardwareAddress() == null) {
                System.out.println("Not testing null addr: " + ni.getName());
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Not testing: " + ni.getName() + " " + ex.getMessage());
            return false;
        }
        return true;
    };

    public static Stream<NetworkInterface> getNetworkInterfacesAsStream() throws Exception {
        return NetworkInterface.networkInterfaces();
    }

    public static void main(String[] args) throws Exception {
        List<NetworkInterface> toTest = getNetworkInterfacesAsStream().filter(hasHardwareAddress).collect(Collectors.toList());
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        for (NetworkInterface ni : toTest) {
            Phaser startingGate = new Phaser(NUM_THREADS);
            System.out.println("Testing: " + ni.getName());
            List<Callable<Exception>> list = new ArrayList<>();
            for (int i = 0; i < NUM_THREADS; i++) list.add(new GetMacAddress(ni, ni.getName() + "-Thread-" + i, startingGate));
            List<Future<Exception>> futures = executor.invokeAll(list);
            for (Future<Exception> f : futures) {
                if (f.get() != null)
                    f.get().printStackTrace(System.out);
            }
            if (failed)
                break;
        }
        executor.shutdownNow();
        if (!failed) {
            System.out.println("PASSED - Finished all threads");
        } else {
            throw new RuntimeException("Failed");
        }
    }
}
