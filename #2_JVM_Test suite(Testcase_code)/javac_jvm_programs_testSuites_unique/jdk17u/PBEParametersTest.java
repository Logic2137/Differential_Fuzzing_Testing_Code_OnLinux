import java.security.AlgorithmParameters;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class PBEParametersTest {

    private static final char[] PASSWORD = { 'p', 'a', 's', 's' };

    private static final String[] PBE_ALGOS = { "PBEWithMD5AndDES", "PBEWithSHA1AndDESede", "PBEWithSHA1AndRC2_40", "PBEWithSHA1AndRC2_128", "PBEWithSHA1AndRC4_40", "PBEWithSHA1AndRC4_128", "PBEWithHmacSHA1AndAES_128", "PBEWithHmacSHA224AndAES_128", "PBEWithHmacSHA256AndAES_128", "PBEWithHmacSHA384AndAES_128", "PBEWithHmacSHA512AndAES_128" };

    public static void main(String[] args) throws Exception {
        PBEKeySpec ks = new PBEKeySpec(PASSWORD);
        for (int i = 0; i < PBE_ALGOS.length; i++) {
            String algo = PBE_ALGOS[i];
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algo);
            SecretKey key = skf.generateSecret(ks);
            Cipher c = Cipher.getInstance(algo, "SunJCE");
            c.init(Cipher.ENCRYPT_MODE, key);
            c.doFinal(new byte[10]);
            AlgorithmParameters params = c.getParameters();
            if (!params.getAlgorithm().equalsIgnoreCase(algo)) {
                throw new Exception("expect: " + algo + ", but got: " + params.getAlgorithm());
            }
            System.out.println(algo + "...done...");
        }
        System.out.println("Test Passed");
    }
}
