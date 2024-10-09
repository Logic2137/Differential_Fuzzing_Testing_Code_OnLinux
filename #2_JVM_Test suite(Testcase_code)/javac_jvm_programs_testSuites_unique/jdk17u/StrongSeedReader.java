import java.io.*;
import java.net.*;
import java.security.SecureRandom;

public class StrongSeedReader {

    public static void main(String[] args) throws Exception {
        if (System.getProperty("os.name", "unknown").startsWith("Windows")) {
            return;
        }
        File file = null;
        try {
            file = new File(System.getProperty("java.io.tmpdir"), "StrongSeedReader.tmpdata");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(new byte[2048]);
            System.setProperty("java.security.egd", file.toURI().toString());
            testSeed("NativePRNG");
            testSeed("SHA1PRNG");
            testSeed("DRBG");
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    private static void testSeed(String alg) throws Exception {
        System.out.println("Testing: " + alg);
        SecureRandom sr = SecureRandom.getInstance(alg);
        byte[] ba = sr.generateSeed(20);
        for (byte b : ba) {
            if (b != 0) {
                throw new Exception("Byte != 0");
            }
        }
    }
}
