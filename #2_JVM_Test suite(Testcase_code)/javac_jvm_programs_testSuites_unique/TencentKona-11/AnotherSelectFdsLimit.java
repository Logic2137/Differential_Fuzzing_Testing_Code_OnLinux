



import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class AnotherSelectFdsLimit {
    static final int DEFAULT_FDS_TO_USE = 1600;

    public static void main(String [] args) throws Exception {
        if (!System.getProperty("os.name").contains("OS X")) {
            System.out.println("Test only run on MAC. Exiting.");
            return;
        }

        int fdsToUse = DEFAULT_FDS_TO_USE;
        if (args.length == 1)
            fdsToUse = Integer.parseInt(args[0]);

        System.out.println("Using " + fdsToUse + " fds.");

        List<Thread> threads = new ArrayList<>();
        for (int i=0; i<fdsToUse; i++)
            threads.add(new WorkerThread());

        for (Thread t : threads)
            t.start();

        for (Thread t : threads)
            t.join();
    }

    static class WorkerThread extends Thread {
        public void run() {
            try (ServerSocket ss = new ServerSocket(0)) {
                ss.setSoTimeout(2000);
                ss.accept();
            } catch (SocketTimeoutException x) {
                
            } catch (IOException x) {
                throw new java.io.UncheckedIOException(x);
            }
        }
    }
}
