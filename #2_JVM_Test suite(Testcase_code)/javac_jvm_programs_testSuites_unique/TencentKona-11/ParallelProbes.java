



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ParallelProbes {

    private static final int REPEATS = 1000;

    private int numThreads = 0;
    private ArrayList<Thread> threads;

    public ParallelProbes(int numThreads) {
        System.out.println("Using <" + numThreads + "> threads.");
        this.numThreads = numThreads;
        this.threads = new ArrayList<Thread>(numThreads);
    }

    private Path createTmpFile() throws IOException {
        Path dir = Paths.get(System.getProperty("test.dir", "."));
        final Path p = Files.createTempFile(dir, "prefix", ".json");
        Files.write(p, "{\"test\"}".getBytes());
        System.out.println("Write test file <" + p + ">");
        return p;
    }

    private Runnable createRunnable(final Path p) {
        Runnable r = new Runnable() {
            public void run() {
                for (int i = 0; i < REPEATS; i++) {
                    try {
                        System.out.println(Thread.currentThread().getName()
                            + " -> " + Files.probeContentType(p));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        };
        return r;
    }

    public void start() throws IOException {
        for (int i = 0; i < numThreads; i++) {
            final Path p = createTmpFile();
            Runnable r = createRunnable(p);
            Thread thread = new Thread(r, "thread-" + i);
            thread.start();
            threads.add(thread);
        }
    }

    public void join() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ParallelProbes probes =
            new ParallelProbes(args.length < 1 ? 1 : Integer.parseInt(args[0]));
        probes.start();
        probes.join();
    }
}
