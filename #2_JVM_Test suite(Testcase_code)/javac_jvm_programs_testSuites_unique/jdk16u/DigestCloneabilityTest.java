


import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.*;
import javax.crypto.spec.*;

public class DigestCloneabilityTest {

    private static String ALGO = "HmacSHA512";

    public static void main(String[] args) throws Exception {
        Provider p = new SampleProvider();
        
        int status = Security.insertProviderAt(p, 1);
        try {
            Mac mac = Mac.getInstance(ALGO, "SunJCE");
            
            
            mac.init(new SecretKeySpec(new byte[512>>3], ALGO));
            mac.update((byte)0x12);
            byte[] macBytes = mac.doFinal();
            if (!SampleProvider.CloneableDigest.isUsed) {
                throw new RuntimeException("Expected Digest impl not used");
            }
        } finally {
            if (status != -1) {
                Security.removeProvider(p.getName());
            }
        }
        System.out.println("Test Passed");
    }

    public static class SampleProvider extends Provider {

        public SampleProvider() {
            super("Sample", "1.0", "test provider");
            putService(new Provider.Service(this, "MessageDigest", "SHA-512",
                    "DigestCloneabilityTest$SampleProvider$CloneableDigest",
                    null, null));
        }
        public static class CloneableDigest extends MessageDigestSpi
                implements Cloneable {
            private MessageDigest md;
            static boolean isUsed = false;

            public CloneableDigest() throws NoSuchAlgorithmException {
                try {
                    md = MessageDigest.getInstance("SHA-512", "SUN");
                } catch (NoSuchProviderException nspe) {
                    
                }
            }

            public byte[] engineDigest() {
                isUsed = true;
                return md.digest();
            }

            public void engineReset() {
                isUsed = true;
                md.reset();
            }

            public void engineUpdate(byte input) {
                isUsed = true;
                md.update(input);
            }

            public void engineUpdate(byte[] b, int ofs, int len) {
                isUsed = true;
                md.update(b, ofs, len);
            }

            public Object clone() throws CloneNotSupportedException {
                return this;
            }
        }
    }
}
