import java.security.*;
import java.util.*;

public class StrongSecureRandom {

    private static final String os = System.getProperty("os.name", "unknown");

    private static void testDefaultEgd() throws Exception {
        String s = Security.getProperty("securerandom.source");
        System.out.println("Testing:  default EGD: " + s);
        if (!s.equals("file:/dev/random")) {
            throw new Exception("Default is not 'file:/dev/random'");
        }
    }

    private static void testNativePRNGImpls() throws Exception {
        SecureRandom sr;
        byte[] ba;
        System.out.println("Testing new NativePRNGImpls");
        if (os.startsWith("Windows")) {
            System.out.println("Skip windows testing.");
            return;
        }
        System.out.println("Testing regular");
        sr = SecureRandom.getInstance("NativePRNG");
        if (!sr.getAlgorithm().equals("NativePRNG")) {
            throw new Exception("sr.getAlgorithm(): " + sr.getAlgorithm());
        }
        ba = sr.generateSeed(1);
        sr.nextBytes(ba);
        sr.setSeed(ba);
        System.out.println("Testing NonBlocking");
        sr = SecureRandom.getInstance("NativePRNGNonBlocking");
        if (!sr.getAlgorithm().equals("NativePRNGNonBlocking")) {
            throw new Exception("sr.getAlgorithm(): " + sr.getAlgorithm());
        }
        ba = sr.generateSeed(1);
        sr.nextBytes(ba);
        sr.setSeed(ba);
        if (os.equals("Linux")) {
            System.out.println("Skip Linux blocking test.");
            return;
        }
        System.out.println("Testing Blocking");
        sr = SecureRandom.getInstance("NativePRNGBlocking");
        if (!sr.getAlgorithm().equals("NativePRNGBlocking")) {
            throw new Exception("sr.getAlgorithm(): " + sr.getAlgorithm());
        }
        ba = sr.generateSeed(1);
        sr.nextBytes(ba);
        sr.setSeed(ba);
    }

    private static void testStrongInstance(boolean expected) throws Exception {
        boolean result;
        try {
            SecureRandom.getInstanceStrong();
            result = true;
        } catch (NoSuchAlgorithmException e) {
            result = false;
        }
        if (expected != result) {
            throw new Exception("Received: " + result);
        }
    }

    private static void testProperty(String property, boolean expected) throws Exception {
        System.out.println("Testing: '" + property + "' " + expected);
        final String origStrongAlgoProp = Security.getProperty("securerandom.strongAlgorithms");
        try {
            Security.setProperty("securerandom.strongAlgorithms", property);
            testStrongInstance(expected);
        } finally {
            Security.setProperty("securerandom.strongAlgorithms", origStrongAlgoProp);
        }
    }

    private static void testProperties() throws Exception {
        testProperty("", false);
        testProperty("SHA1PRNG", true);
        testProperty(" SHA1PRNG", true);
        testProperty("SHA1PRNG ", true);
        testProperty(" SHA1PRNG ", true);
        testProperty("SHA1PRNG:SUN", true);
        testProperty("Sha1PRNG:SUN", true);
        testProperty("SHA1PRNG:Sun", false);
        testProperty(" SHA1PRNG:SUN", true);
        testProperty("SHA1PRNG:SUN ", true);
        testProperty(" SHA1PRNG:SUN ", true);
        testProperty(" SHA1PRNG:SUn", false);
        testProperty("SHA1PRNG:SUn ", false);
        testProperty(" SHA1PRNG:SUn ", false);
        testProperty(",,,SHA1PRNG", true);
        testProperty(",,, SHA1PRNG", true);
        testProperty(" , , ,SHA1PRNG ", true);
        testProperty(",,,, SHA1PRNG ,,,", true);
        testProperty(",,,, SHA1PRNG:SUN ,,,", true);
        testProperty(",,,, SHA1PRNG:SUn ,,,", false);
        testProperty(",,,SHA1PRNG:Sun,, SHA1PRNG:SUN", true);
        testProperty(",,,Sha1PRNG:Sun, SHA1PRNG:SUN", true);
        testProperty(" SHA1PRNG:Sun, Sha1PRNG:Sun,,,,Sha1PRNG:SUN", true);
        testProperty(",,,SHA1PRNG:Sun,, SHA1PRNG:SUn", false);
        testProperty(",,,Sha1PRNG:Sun, SHA1PRNG:SUn", false);
        testProperty(" SHA1PRNG:Sun, Sha1PRNG:Sun,,,,Sha1PRNG:SUn", false);
        testProperty(" @#%,%$#:!%^, NativePRNG:Sun, Sha1PRNG:Sun,,Sha1PRNG:SUN", true);
        testProperty(" @#%,%$#!%^, NativePRNG:Sun, Sha1PRNG:Sun,,Sha1PRNG:SUn", false);
    }

    private static void handleLinuxRead(SecureRandom sr) throws Exception {
        if (os.equals("Linux")) {
            if (!sr.getAlgorithm().equalsIgnoreCase("NativePRNGBlocking")) {
                sr.nextBytes(new byte[34]);
            }
        } else {
            sr.nextBytes(new byte[34]);
            sr.generateSeed(34);
            sr.setSeed(new byte[34]);
        }
    }

    private static void testAllImpls() throws Exception {
        System.out.print("Testing:  AllImpls:  ");
        Iterator<String> i = Security.getAlgorithms("SecureRandom").iterator();
        while (i.hasNext()) {
            String s = i.next();
            System.out.print("/" + s);
            SecureRandom sr = SecureRandom.getInstance(s);
            handleLinuxRead(sr);
            handleLinuxRead(sr);
        }
        System.out.println("/");
    }

    public static void main(String[] args) throws Exception {
        testDefaultEgd();
        testNativePRNGImpls();
        testAllImpls();
        testStrongInstance(true);
        testProperties();
    }
}
