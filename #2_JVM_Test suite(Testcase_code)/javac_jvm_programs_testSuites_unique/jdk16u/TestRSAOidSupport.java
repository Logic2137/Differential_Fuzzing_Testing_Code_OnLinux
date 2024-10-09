



import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;

public class TestRSAOidSupport {

    
    
    
    private static String DER_BYTES =
             "3058300906052b0e03020f0500034b003048024100d7157c65e8f22557d8" +
             "a857122cfe85bddfaba3064c21b345e2a7cdd8a6751e519ab861c5109fb8" +
             "8cce45d161b9817bc0eccdc30fda69e62cc577775f2c1d66bd0203010001";

    
    static byte[] toByteArray(String s) {
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            bytes[i] = (byte) v;
        }
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        X509EncodedKeySpec x509Spec = new X509EncodedKeySpec
                (toByteArray(DER_BYTES));
        String keyAlgo = "RSA";
        KeyFactory kf = KeyFactory.getInstance(keyAlgo, "SunRsaSign");
        RSAPublicKey rsaKey = (RSAPublicKey) kf.generatePublic(x509Spec);

        if (rsaKey.getAlgorithm() != keyAlgo) {
            throw new RuntimeException("Key algo should be " + keyAlgo +
                    ", but got " + rsaKey.getAlgorithm());
        }
        kf = KeyFactory.getInstance("RSASSA-PSS", "SunRsaSign");
        try {
            kf.generatePublic(x509Spec);
            throw new RuntimeException("Should throw IKSE");
        } catch (InvalidKeySpecException ikse) {
            System.out.println("Expected IKSE exception thrown");
        }
    }
}

