import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Random;

public class ThreadSafetyTest {

    static volatile boolean runrun = true;

    static volatile boolean error = false;

    private static final String[] ALGORITHM_ARRAY = { "MD2", "MD5", "SHA1", "SHA-224", "SHA-256", "SHA-384", "SHA-512", "SHA-512/224", "SHA-512/256", "SHA3-224", "SHA3-256", "SHA3-384", "SHA3-512" };

    public static void main(String[] args) throws Exception {
        int threadsFactor = 5;
        if (args.length > 0) {
            threadsFactor = Integer.parseInt(args[0]);
        }
        int duration = 4;
        if (args.length > 1) {
            duration = Integer.parseInt(args[1]);
        }
        int nProcessors = Runtime.getRuntime().availableProcessors();
        int nTasks = nProcessors * threadsFactor;
        System.out.println("Testing with " + nTasks + " threads on " + nProcessors + " processors for " + duration + " seconds.");
        byte[] input = new byte[1024];
        (new Random()).nextBytes(input);
        for (Provider p : Security.getProviders()) {
            for (String alg : ALGORITHM_ARRAY) {
                try {
                    MessageDigest md = MessageDigest.getInstance(alg, p);
                    testThreadSafety(md, input, nTasks, duration, false);
                    if (isClonable(md)) {
                        md.reset();
                        testThreadSafety(md, input, nTasks, duration, true);
                    }
                } catch (NoSuchAlgorithmException nae) {
                }
            }
        }
    }

    static private void testThreadSafety(final MessageDigest originalMD, final byte[] input, final int nTasks, final int duration, final boolean useClone) {
        Thread[] tasks = new Thread[nTasks];
        byte[] expectedOut = getHash(originalMD, input, useClone);
        originalMD.reset();
        runrun = true;
        for (int i = 0; i < nTasks; i++) {
            tasks[i] = new Thread(new Runnable() {

                public void run() {
                    MessageDigest md = getMessageDigest(originalMD, useClone);
                    while (runrun) {
                        byte[] newOut = getHash(md, input, useClone);
                        if (!Arrays.equals(expectedOut, newOut)) {
                            runrun = false;
                            error = true;
                        }
                    }
                }
            });
        }
        for (int i = 0; i < nTasks; i++) {
            tasks[i].start();
        }
        try {
            for (int i = 0; runrun && i < duration; i++) {
                Thread.sleep(1000);
            }
            runrun = false;
            for (int i = 0; i < nTasks; i++) {
                tasks[i].join();
            }
        } catch (InterruptedException e) {
        }
        if (error) {
            throw new RuntimeException("MessageDigest " + originalMD.getAlgorithm() + " in the provider " + originalMD.getProvider().getName() + " is not thread-safe" + (useClone ? " after clone." : "."));
        }
    }

    static private byte[] getHash(final MessageDigest messageDigest, final byte[] input, final boolean useClone) {
        for (int i = 0; i < 100; i++) messageDigest.update(input);
        return messageDigest.digest();
    }

    static private MessageDigest getMessageDigest(final MessageDigest prototype, final boolean useClone) {
        try {
            if (useClone) {
                return (MessageDigest) prototype.clone();
            }
            return MessageDigest.getInstance(prototype.getAlgorithm(), prototype.getProvider());
        } catch (final CloneNotSupportedException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static private boolean isClonable(final MessageDigest messageDigest) {
        try {
            messageDigest.clone();
            return true;
        } catch (final CloneNotSupportedException e) {
            return false;
        }
    }
}
