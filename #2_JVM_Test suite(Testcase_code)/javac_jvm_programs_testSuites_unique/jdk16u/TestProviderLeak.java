





import javax.crypto.*;
import javax.crypto.spec.*;

import java.util.*;
import java.util.concurrent.*;

public class TestProviderLeak {
    private static final int MB = 1024 * 1024;
    
    
    
    private static final int RESERVATION = 3;
    
    private static final int TIME_OUT;
    static {
        int timeout = 5;
        try {
            double timeoutFactor = Double.parseDouble(
                    System.getProperty("test.timeout.factor", "1.0"));
            timeout = (int) (timeout * timeoutFactor);
        } catch (Exception e) {
            System.out.println("Warning: " + e);
        }
        TIME_OUT = timeout;
        System.out.println("Timeout for each iteration is "
                + TIME_OUT + " seconds");
    }

    private static Deque<byte []> eatupMemory() throws Exception {
        dumpMemoryStats("Before memory allocation");

        Deque<byte []> data = new ArrayDeque<byte []>();
        boolean hasException = false;
        while (!hasException) {
            byte [] megaByte;
            try {
                megaByte = new byte [MB];
                data.add(megaByte);
            } catch (OutOfMemoryError e) {
                megaByte = null;    

                int size = data.size();

                for (int j = 0; j < RESERVATION && !data.isEmpty(); j++) {
                    data.removeLast();
                }
                System.gc();
                hasException = true;
                System.out.println("OOME is thrown when allocating "
                        + size + "MB memory.");
            }
        }
        dumpMemoryStats("After memory allocation");

        return data;
    }

    private static void dumpMemoryStats(String s) throws Exception {
        Runtime rt = Runtime.getRuntime();
        System.out.println(s + ":\t"
            + rt.freeMemory() + " bytes free");
    }

    public static void main(String [] args) throws Exception {
        
        final SecretKeyFactory skf =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1", "SunJCE");
        final PBEKeySpec pbeKS = new PBEKeySpec(
            "passPhrase".toCharArray(), new byte [] { 0 }, 5, 512);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<SecretKey> task = new Callable<SecretKey>() {
            @Override
            public SecretKey call() throws Exception {
                return skf.generateSecret(pbeKS);
            }
        };

        
        Deque<byte []> dummyData = eatupMemory();
        assert (dummyData != null);

        
        try {
            for (int i = 0; i <= 1000; i++) {
                if ((i % 20) == 0) {
                    
                    
                    System.gc();
                    dumpMemoryStats("Iteration " + i);
                }

                Future<SecretKey> future = executor.submit(task);

                try {
                    future.get(TIME_OUT, TimeUnit.SECONDS);
                } catch (Exception e) {
                    dumpMemoryStats("\nException seen at iteration " + i);
                    throw e;
                }
            }
        } finally {
            
            
            dummyData = null;
            System.gc();
            dumpMemoryStats("Memory dereference");
            executor.shutdownNow();
        }
    }
}
