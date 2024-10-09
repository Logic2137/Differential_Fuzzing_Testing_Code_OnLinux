




import java.security.*;
import java.security.spec.*;
import java.util.Arrays;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class TestCipherMode {

    private static final String[] TRANSFORMATIONS = {
        "DES/ECB/PKCS5Padding", 
        "AES/GCM/NoPadding", 
        "AES/KW/NoPadding", 
        "AES/KW/PKCS5Padding", 
        "AES/KWP/NoPadding", 
        "RSA/ECB/NoPadding", 
        "DESedeWrap/CBC/NoPadding", 
        "ChaCha20-Poly1305", 
    };

    private static final byte[] BYTES32 =
            Arrays.copyOf(TRANSFORMATIONS[0].getBytes(), 32);
    private static final SecretKey DES_KEY =
            new SecretKeySpec(BYTES32, 0, 8, "DES");
    private static final SecretKey AES_KEY =
            new SecretKeySpec(BYTES32, 0, 16, "AES");

    private  static enum CipherMode {
        ENCRYPT(Cipher.ENCRYPT_MODE),
        DECRYPT(Cipher.DECRYPT_MODE),
        WRAP(Cipher.WRAP_MODE),
        UNWRAP(Cipher.UNWRAP_MODE),
        NONEXISTENT(100);

        int value;

        CipherMode(int value) {
            this.value = value;
        }
    }

    private static Key getKey(String t, CipherMode m)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        Key key;
        String algo = t.split("/")[0];
        switch (algo) {
        case "AES":
            key = AES_KEY;
        break;
        case "RSA":
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(algo);
            KeyPair kp = kpg.generateKeyPair();
            key = ((m == CipherMode.ENCRYPT || m == CipherMode.UNWRAP)?
                    kp.getPrivate() : kp.getPublic());
        break;
        case "ChaCha20-Poly1305":
            key = new SecretKeySpec(BYTES32, 0, 32, "ChaCha20");
        break;
        case "DES":
            key = new SecretKeySpec(BYTES32, 0, 8, algo);
        break;
        case "DESedeWrap":
            key = new SecretKeySpec(BYTES32, 0, 24, "DESede");
        break;
        default:
            throw new RuntimeException("Unknown transformation: " + t);
        }
        return key;
    }

    public static void main(String[] argv) throws Exception {

        TestCipherMode test = new TestCipherMode("SunJCE", TRANSFORMATIONS);
        System.out.println("All Tests Passed");
   }

    private Cipher c = null;
    private SecretKey key = null;

    private TestCipherMode(String provName, String... transformations)
            throws Exception {

        System.out.println("Testing " + provName);

        for (String t : transformations) {
            for (CipherMode m : CipherMode.values()) {
                checkMode(t, m, provName);
            }
        }
    }

    private void checkMode(String t, CipherMode mode, String provName)
            throws Exception {
        Cipher c = Cipher.getInstance(t, provName);
        Key key = getKey(t, mode);

        System.out.println(c.getAlgorithm() + " with " + mode.name());
        try {
            c.init(mode.value, key, c.getParameters());
            if (mode == CipherMode.NONEXISTENT) {
                throw new Exception("ERROR: should throw IPE for init()");
            }
        } catch (UnsupportedOperationException uoe)  {
            
            if (mode != CipherMode.NONEXISTENT) {
                System.out.println("Expected UOE thrown with init()");
                return;
            }
            throw uoe;
        } catch (InvalidParameterException ipe) {
            if (mode == CipherMode.NONEXISTENT) {
                System.out.println("=> expected IPE thrown for init()");
                return;
            }
            throw ipe;
        }

        switch (mode) {
        case ENCRYPT:
        case DECRYPT:
            
            try {
                c.wrap(key);
                throw new Exception("ERROR: should throw ISE for wrap()");
            } catch (IllegalStateException ise) {
                System.out.println("=> expected ISE thrown for wrap()");
            }
            try {
                c.unwrap(new byte[16], key.getAlgorithm(), Cipher.SECRET_KEY);
                throw new Exception("ERROR: should throw ISE for unwrap()");
            } catch (IllegalStateException ise) {
                System.out.println("=> expected ISE thrown for unwrap()");
            }
            break;
        case WRAP:
        case UNWRAP:
            try {
                c.update(new byte[16]);
                throw new Exception("ERROR: should throw ISE for update()");
            } catch (IllegalStateException ise) {
                System.out.println("=> expected ISE thrown for update()");
            }
            try {
                c.doFinal();
                throw new Exception("ERROR: should throw ISE for doFinal()");
            } catch (IllegalStateException ise) {
                System.out.println("=> expected ISE thrown for doFinal()");
            }
            break;
        }
    }
}
