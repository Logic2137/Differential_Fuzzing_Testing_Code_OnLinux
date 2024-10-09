import java.io.PrintStream;
import java.security.*;
import java.security.spec.*;
import java.util.Random;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.Provider;

public class Test4512704 {

    private static final String ALGO = "AES";

    private static final String PADDING = "NoPadding";

    private static final int KEYSIZE = 16;

    public void execute(String mode) throws Exception {
        AlgorithmParameterSpec aps = null;
        String transformation = ALGO + "/" + mode + "/" + PADDING;
        Cipher ci = Cipher.getInstance(transformation, "SunJCE");
        KeyGenerator kg = KeyGenerator.getInstance(ALGO, "SunJCE");
        kg.init(KEYSIZE * 8);
        SecretKey key = kg.generateKey();
        try {
            ci.init(Cipher.ENCRYPT_MODE, key, aps);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new Exception("parameter should be generated when null is specified!");
        }
        System.out.println(transformation + ": Passed");
    }

    public static void main(String[] args) throws Exception {
        Test4512704 test = new Test4512704();
        test.execute("CBC");
        test.execute("GCM");
    }
}
