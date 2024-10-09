



import java.security.SecureRandom;

public class PrngSlow {

    public static void main(String[] args) throws Exception {
        double t = 0.0;
        SecureRandom sr = null;
        sr = SecureRandom.getInstance("Windows-PRNG", "SunMSCAPI");
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            if (i % 100 == 0) System.err.print(".");
            sr.nextBoolean();
        };
        t = (System.nanoTime() - start) / 1000000000.0;
        System.err.println("\nSpend " + t + " seconds");
        if (t > 5)
            throw new RuntimeException("Still too slow");
    }
}
