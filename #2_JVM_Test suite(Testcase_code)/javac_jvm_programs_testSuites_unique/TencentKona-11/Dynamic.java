

import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Dynamic {

    static final String ALGORITHM = "AES";
    static final String[] MODE = {
        "ECb", "CbC", "CTR", "PCBC", "OFB", "OFB150", "cFB", "CFB7",
        "cFB8", "cFB16", "cFB24", "cFB32", "Cfb40", "cfB48", "cfB56",
        "cfB64", "cfB72", "cfB80", "cfB88", "cfB96", "cfb104", "cfB112",
        "cfB120", "cfB128", "OFB8", "OFB16", "OFB24", "OFB32", "OFB40",
        "OFB48", "OFB56", "OFB64", "OFB72", "OFB80", "OFB88", "OFB96",
        "OFB104", "OFB112", "OFB120", "OFB128", "GCM"
    };
    static final String[] PADDING = {
        "NoPadding", "PKCS5Padding", "ISO10126Padding"
    };
    static final String SUNJCE = "SunJCE";

    Cipher ci = null;
    byte[] iv = null;
    AlgorithmParameterSpec aps = null;
    SecretKey key = null;
    int keyStrength;
    static int DefaultSize = 128;

    public void run(String[] argv) throws Exception {
        if (!runAllTest(argv, System.out)) {
            throw new Exception("Test Failed");
        }
    }

    protected boolean runAllTest(String argv[], PrintStream out) {
        boolean result = true;
        StringBuilder failedList = new StringBuilder();
        int failedCnt = 0;
        int testCount = 0;
        int padKinds; 
        

        try {
            for (int i = 0; i < 3; i++) {
                keyStrength = DefaultSize + i * 64; 
                

                for (int j = 0; j < MODE.length; j++) {
                    if (MODE[j].equalsIgnoreCase("ECB")
                            || MODE[j].equalsIgnoreCase("PCBC")
                            || MODE[j].equalsIgnoreCase("CBC")) {
                        padKinds = PADDING.length;
                    } else {
                        padKinds = 1;
                    }

                    for (int k = 0; k < padKinds; k++) {
                        testCount++;
                        try {
                            if (!runTest(ALGORITHM, MODE[j], PADDING[k])) {
                                result = false;
                                failedCnt++;
                                failedList.append(ALGORITHM + "/" + MODE[j]
                                        + "/" + PADDING[k] + " ");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            result = false;
                            failedCnt++;
                            failedList.append(ALGORITHM + "/" + MODE[j] + "/"
                                    + PADDING[k] + " ");
                        }

                    }
                }
            }

            if (result) {
                out.println("STATUS:Passed. Test " + testCount
                        + " cases, All Passed");
                return true;
            }
            out.println("STATUS:Failed. " + failedCnt + " Failed: "
                    + failedList);
            return false;

        } catch (Exception ex) {
            ex.printStackTrace();
            out.println("STATUS:Failed. Unexpected Exception: " + ex);
            return false;
        }
    }

    protected boolean runTest(String algo, String mo, String pad)
            throws Exception {
        boolean result = true;
        try {
            byte[] plainText = new byte[160000];
            new Random().nextBytes(plainText);

            String transformation = algo + "/" + mo + "/" + pad;
            ci = Cipher.getInstance(transformation, SUNJCE);
            KeyGenerator kg = KeyGenerator.getInstance(algo, SUNJCE);
            if (keyStrength > Cipher.getMaxAllowedKeyLength(transformation)) {
                
                
                System.out.println(keyStrength
                        + " is larger than what's configured "
                        + "in the jce jurisdiction policy files");
                return result;
            }
            kg.init(keyStrength);
            key = kg.generateKey();

            if (!mo.equalsIgnoreCase("GCM")) {
                ci.init(Cipher.ENCRYPT_MODE, key, aps);
            } else {
                ci.init(Cipher.ENCRYPT_MODE, key);
            }
            byte[] cipherText = new byte[ci.getOutputSize(plainText.length)];
            int offset = ci.update(plainText, 0, plainText.length, cipherText,
                    0);
            ci.doFinal(cipherText, offset);
            ci.init(Cipher.DECRYPT_MODE, key, ci.getParameters());

            byte[] recoveredText = new byte[ci.getOutputSize(cipherText.length)];
            int len = ci.doFinal(cipherText, 0, cipherText.length,
                    recoveredText);

            byte[] tmp = new byte[len];
            for (int i = 0; i < len; i++) {
                tmp[i] = recoveredText[i];
            }

            result = Arrays.equals(plainText, tmp);
        } catch (NoSuchAlgorithmException nsaEx) {
            
            
            result = mo.equalsIgnoreCase("CFB7")
                    || mo.equalsIgnoreCase("OFB150");
            if (!result) {
                
                nsaEx.printStackTrace();
            }
        }
        return result;
    }
}
