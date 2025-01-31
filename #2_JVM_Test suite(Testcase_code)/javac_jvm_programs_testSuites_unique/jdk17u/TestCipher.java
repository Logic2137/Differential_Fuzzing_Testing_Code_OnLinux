import static java.lang.System.out;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class TestCipher {

    private final String SUNJCE = "SunJCE";

    private final String ALGORITHM;

    private final String[] MODES;

    private final String[] PADDINGS;

    private final int KEYCUTTER = 8;

    private final int minKeySize;

    private final int maxKeySize;

    private final int TEXT_LEN = 800;

    private final int ENC_OFFSET = 6;

    private final int STORAGE_OFFSET = 3;

    private final int PAD_BYTES = 16;

    private final byte[] IV;

    private final byte[] INPUT_TEXT;

    TestCipher(String algo, String[] modes, String[] paddings, int minKeySize, int maxKeySize) throws NoSuchAlgorithmException {
        ALGORITHM = algo;
        MODES = modes;
        PADDINGS = paddings;
        this.minKeySize = minKeySize;
        int maxAllowedKeySize = Cipher.getMaxAllowedKeyLength(ALGORITHM);
        if (maxKeySize > maxAllowedKeySize) {
            maxKeySize = maxAllowedKeySize;
        }
        this.maxKeySize = maxKeySize;
        IV = generateBytes(8);
        INPUT_TEXT = generateBytes(TEXT_LEN + PAD_BYTES + ENC_OFFSET);
    }

    TestCipher(String algo, String[] modes, String[] paddings) {
        ALGORITHM = algo;
        MODES = modes;
        PADDINGS = paddings;
        this.minKeySize = this.maxKeySize = 0;
        IV = generateBytes(8);
        INPUT_TEXT = generateBytes(TEXT_LEN + PAD_BYTES + ENC_OFFSET);
    }

    private static byte[] generateBytes(int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) (i & 0xff);
        }
        return bytes;
    }

    private boolean isMultipleKeyLengthSupported() {
        return (maxKeySize != minKeySize);
    }

    public void runAll() throws InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        for (String mode : MODES) {
            for (String padding : PADDINGS) {
                if (!isMultipleKeyLengthSupported()) {
                    runTest(mode, padding, minKeySize);
                } else {
                    int keySize = maxKeySize;
                    while (keySize >= minKeySize) {
                        out.println("With Key Strength: " + keySize);
                        runTest(mode, padding, keySize);
                        keySize -= KEYCUTTER;
                    }
                }
            }
        }
    }

    private void runTest(String mo, String pad, int keySize) throws NoSuchPaddingException, BadPaddingException, ShortBufferException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException {
        String TRANSFORMATION = ALGORITHM + "/" + mo + "/" + pad;
        out.println("Testing: " + TRANSFORMATION);
        Cipher ci = Cipher.getInstance(TRANSFORMATION, SUNJCE);
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM, SUNJCE);
        if (keySize != 0) {
            kg.init(keySize);
        }
        SecretKey key = kg.generateKey();
        SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), ALGORITHM);
        AlgorithmParameterSpec aps = new IvParameterSpec(IV);
        if (mo.equalsIgnoreCase("ECB")) {
            ci.init(Cipher.ENCRYPT_MODE, key);
        } else {
            ci.init(Cipher.ENCRYPT_MODE, key, aps);
        }
        byte[] plainText = INPUT_TEXT.clone();
        byte[] cipherText = ci.doFinal(INPUT_TEXT, ENC_OFFSET, TEXT_LEN);
        int enc_bytes = ci.update(INPUT_TEXT, ENC_OFFSET, TEXT_LEN, INPUT_TEXT, STORAGE_OFFSET);
        enc_bytes += ci.doFinal(INPUT_TEXT, enc_bytes + STORAGE_OFFSET);
        if (!equalsBlock(INPUT_TEXT, STORAGE_OFFSET, enc_bytes, cipherText, 0, cipherText.length)) {
            throw new RuntimeException("Different ciphers generated with same buffer");
        }
        if (mo.equalsIgnoreCase("ECB")) {
            ci.init(Cipher.DECRYPT_MODE, skeySpec);
        } else {
            ci.init(Cipher.DECRYPT_MODE, skeySpec, aps);
        }
        byte[] recoveredText = ci.doFinal(cipherText, 0, cipherText.length);
        if (!equalsBlock(plainText, ENC_OFFSET, TEXT_LEN, recoveredText, 0, recoveredText.length)) {
            throw new RuntimeException("Recovered text not same as plain text");
        } else {
            out.println("Recovered and plain text are same");
        }
        int dec_bytes = ci.update(INPUT_TEXT, STORAGE_OFFSET, enc_bytes, INPUT_TEXT, ENC_OFFSET);
        dec_bytes += ci.doFinal(INPUT_TEXT, dec_bytes + ENC_OFFSET);
        if (!equalsBlock(plainText, ENC_OFFSET, TEXT_LEN, INPUT_TEXT, ENC_OFFSET, dec_bytes)) {
            throw new RuntimeException("Recovered text not same as plain text with same buffer");
        } else {
            out.println("Recovered and plain text are same with same buffer");
        }
        out.println("Test Passed.");
    }

    private static boolean equalsBlock(byte[] b1, int off1, int len1, byte[] b2, int off2, int len2) {
        if (len1 != len2) {
            return false;
        }
        for (int i = off1, j = off2, k = 0; k < len1; i++, j++, k++) {
            if (b1[i] != b2[j]) {
                return false;
            }
        }
        return true;
    }
}
