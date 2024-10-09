import java.io.PrintStream;
import java.security.*;
import java.security.spec.*;
import java.util.Random;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.Provider;

public class Test4512524 {

    private static final String ALGO = "AES";

    private static final String PADDING = "NoPadding";

    private static final int KEYSIZE = 16;

    public void execute(String mode) throws Exception {
        String transformation = ALGO + "/" + mode + "/" + PADDING;
        Cipher ci = Cipher.getInstance(transformation, "SunJCE");
        KeyGenerator kg = KeyGenerator.getInstance(ALGO, "SunJCE");
        kg.init(KEYSIZE * 8);
        SecretKey key = kg.generateKey();
        try {
            AlgorithmParameterSpec aps = null;
            ci.init(Cipher.ENCRYPT_MODE, key, aps);
        } catch (NullPointerException ex) {
            throw new Exception("null parameter is not handled correctly!");
        }
        System.out.println(transformation + ": Passed");
    }

    public static void main(String[] args) throws Exception {
        Test4512524 test = new Test4512524();
        test.execute("CBC");
        test.execute("GCM");
    }
}
