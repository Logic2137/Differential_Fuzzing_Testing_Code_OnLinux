import static javax.crypto.Cipher.getMaxAllowedKeyLength;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class TestAESWrapOids {

    private static final String PROVIDER_NAME = "SunJCE";

    private static final List<DataTuple> DATA = Arrays.asList(new DataTuple("2.16.840.1.101.3.4.1.5", "AESWrap_128", 128), new DataTuple("2.16.840.1.101.3.4.1.25", "AESWrap_192", 192), new DataTuple("2.16.840.1.101.3.4.1.45", "AESWrap_256", 256));

    public static void main(String[] args) throws Exception {
        for (DataTuple dataTuple : DATA) {
            int maxAllowedKeyLength = getMaxAllowedKeyLength(dataTuple.algorithm);
            boolean supportedKeyLength = maxAllowedKeyLength >= dataTuple.keyLength;
            try {
                runTest(dataTuple, supportedKeyLength);
                System.out.println("passed");
            } catch (InvalidKeyException ike) {
                if (supportedKeyLength) {
                    throw new RuntimeException(String.format("The key length %d is supported, but test failed.", dataTuple.keyLength), ike);
                } else {
                    System.out.printf("Catch expected InvalidKeyException " + "due to the key length %d is greater " + "than max supported key length %d%n", dataTuple.keyLength, maxAllowedKeyLength);
                }
            }
        }
    }

    private static void runTest(DataTuple dataTuple, boolean supportedKeyLength) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
        Cipher algorithmCipher = Cipher.getInstance(dataTuple.algorithm, PROVIDER_NAME);
        Cipher oidCipher = Cipher.getInstance(dataTuple.oid, PROVIDER_NAME);
        if (algorithmCipher == null) {
            throw new RuntimeException(String.format("Test failed: algorithm string %s getInstance failed.%n", dataTuple.algorithm));
        }
        if (oidCipher == null) {
            throw new RuntimeException(String.format("Test failed: OID %s getInstance failed.%n", dataTuple.oid));
        }
        if (!algorithmCipher.getAlgorithm().equals(dataTuple.algorithm)) {
            throw new RuntimeException(String.format("Test failed: algorithm string %s getInstance " + "doesn't generate expected algorithm.%n", dataTuple.oid));
        }
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(dataTuple.keyLength);
        SecretKey key = kg.generateKey();
        algorithmCipher.init(Cipher.WRAP_MODE, key);
        if (!supportedKeyLength) {
            throw new RuntimeException(String.format("The key length %d is not supported, so the initialization" + " of algorithmCipher should fail.%n", dataTuple.keyLength));
        }
        oidCipher.init(Cipher.UNWRAP_MODE, key);
        if (!supportedKeyLength) {
            throw new RuntimeException(String.format("The key length %d is not supported, so the initialization" + " of oidCipher should fail.%n", dataTuple.keyLength));
        }
        byte[] keyWrapper = algorithmCipher.wrap(key);
        Key unwrappedKey = oidCipher.unwrap(keyWrapper, "AES", Cipher.SECRET_KEY);
        if (!Arrays.equals(key.getEncoded(), unwrappedKey.getEncoded())) {
            throw new RuntimeException("Key comparison failed");
        }
    }

    private static class DataTuple {

        private final String oid;

        private final String algorithm;

        private final int keyLength;

        private DataTuple(String oid, String algorithm, int keyLength) {
            this.oid = oid;
            this.algorithm = algorithm;
            this.keyLength = keyLength;
        }
    }
}
