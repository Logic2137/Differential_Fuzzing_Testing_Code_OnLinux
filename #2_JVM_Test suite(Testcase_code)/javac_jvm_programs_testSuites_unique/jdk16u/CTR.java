

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;




public class CTR {

    private static final String ALGORITHM = "AES";

    private static final String PROVIDER = "SunJCE";

    private static final String[] MODES = {"CTR","CFB24","OFB32","GCM"};

    private static final String PADDING = "NoPadding";


    private static final int KEY_LENGTH = 128;

    public static void main(String argv[]) throws Exception {
        CTR test = new CTR();
        for (String mode : MODES) {
            test.runTest(ALGORITHM, mode, PADDING);
        }
    }


    public void runTest(String algo, String mo, String pad) throws Exception {
        Cipher ci = null;
        byte[] iv = null;
        AlgorithmParameterSpec aps = null;
        SecretKey key = null;

        try {
            Random rdm = new Random();
            byte[] plainText;

            ci = Cipher.getInstance(algo + "/" + mo + "/" + pad, PROVIDER);
            KeyGenerator kg = KeyGenerator.getInstance(algo, PROVIDER);
            kg.init(KEY_LENGTH);
            key = kg.generateKey();

            for (int i = 0; i < 15; i++) {
                plainText = new byte[1600 + i + 1];
                rdm.nextBytes(plainText);

                if (!mo.equalsIgnoreCase("GCM")) {
                    ci.init(Cipher.ENCRYPT_MODE, key, aps);
                } else {
                    ci.init(Cipher.ENCRYPT_MODE, key);
                }

                byte[] cipherText = new byte[ci.getOutputSize(plainText.length)];
                int offset = ci.update(plainText, 0, plainText.length,
                        cipherText, 0);

                ci.doFinal(cipherText, offset);

                if (!mo.equalsIgnoreCase("ECB")) {
                    iv = ci.getIV();
                    aps = new IvParameterSpec(iv);
                } else {
                    aps = null;
                }

                if (!mo.equalsIgnoreCase("GCM")) {
                    ci.init(Cipher.DECRYPT_MODE, key, aps);
                } else {
                    ci.init(Cipher.DECRYPT_MODE, key, ci.getParameters());
                }

                byte[] recoveredText = new byte[ci.getOutputSize(cipherText.length)];
                int len = ci.doFinal(cipherText, 0, cipherText.length,
                        recoveredText);
                byte[] tmp = new byte[len];

                for (int j = 0; j < len; j++) {
                    tmp[j] = recoveredText[j];
                }
                Arrays.toString(plainText);
                if (!java.util.Arrays.equals(plainText, tmp)) {
                    System.out.println("Original: ");
                    dumpBytes(plainText);
                    System.out.println("Recovered: ");
                    dumpBytes(tmp);
                    throw new RuntimeException("Original text is not equal with recovered text, with mode:" + mo);
                }
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException
                | ShortBufferException | IllegalBlockSizeException
                | BadPaddingException e) {
            System.out.println("Test failed!");
            throw e;
        }
    }

    private void dumpBytes(byte[] bytes){
        for (byte b : bytes){
            System.out.print(Integer.toHexString(b));
        }
        System.out.println();
    }
}
