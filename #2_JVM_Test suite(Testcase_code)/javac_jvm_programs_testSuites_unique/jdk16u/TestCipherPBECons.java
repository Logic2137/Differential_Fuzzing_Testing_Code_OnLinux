

import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;


public class TestCipherPBECons {

    private static final String[] PBEAlgorithms = {"pbeWithMD5ANDdes",
        "PBEWithMD5AndTripleDES"};
    private static final String[] cipherModes = {"ECb", "cbC", "cFB", "Cfb32",
        "OfB", "oFb64", "pCbC"};
    private static final String[] cipherPaddings = {"Pkcs5Padding", "NoPaDDing"};

    public static void main(String[] args) {
        TestCipherPBECons test = new TestCipherPBECons();
        Provider sunjce = Security.getProvider("SunJCE");

        if (!test.runAll(sunjce, System.out)) {
            throw new RuntimeException("One or more tests have failed....");
        }
    }

    public boolean runAll(Provider p, PrintStream out) {
        boolean finalResult = true;

        for (String algorithm : PBEAlgorithms) {
            for (String mode : cipherModes) {
                for (String padding : cipherPaddings) {
                    out.println("Running test with " + algorithm
                            + "/" + mode + "/" + padding);
                    try {
                        if (!runTest(p, algorithm, mode, padding, out)) {
                            finalResult = false;
                            out.println("STATUS: Failed");
                        } else {
                            out.println("STATUS: Passed");
                        }
                    } catch (Exception ex) {
                        finalResult = false;
                        ex.printStackTrace(out);
                        out.println("STATUS:Failed");
                    }
                }
            }
        }

        return finalResult;
    }

    public boolean runTest(Provider p, String algo, String mo, String pad,
            PrintStream out) throws Exception {
        try {
            
            Cipher ci = Cipher.getInstance(algo + "/" + mo + "/" + pad, p);

            
            
            return (mo.equalsIgnoreCase("CBC"))
                    && (pad.equalsIgnoreCase("PKCS5Padding"));
        } catch (NoSuchAlgorithmException ex) {
            if (p.getName().compareTo("SunJCE") == 0) {
                if (!(mo.equalsIgnoreCase("CBC")
                        && pad.equalsIgnoreCase("PKCS5Padding"))) {
                    out.println("NoSuchAlgorithmException is as expected");
                    return true;
                }
            }

            out.println("Caught exception: " + ex.getMessage());
            throw ex;
        } catch (NoSuchPaddingException ex) {
            if (mo.equalsIgnoreCase("CBC")
                    && pad.equalsIgnoreCase("NoPadding")) {
                out.println("NoSuchPaddingException is as expected");
                return true;
            } else {
                out.println("Caught unexpected exception: " + ex.getMessage());
                return false;
            }
        }
    }
}
