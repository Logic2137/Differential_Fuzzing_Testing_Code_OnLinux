



import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import sun.security.provider.DSAPrivateKey;

public class SolarisShortDSA {
    static byte[] data = new byte[0];
    public static void main(String args[]) throws Exception {
        for (int i=0; i<10000; i++) {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            KeyPair kp = kpg.generateKeyPair();
            DSAPrivateKey dpk = (DSAPrivateKey)kp.getPrivate();
            int len = dpk.getX().bitLength();
            if (len <= 152) {
                if (!use(kp)) {
                    String os = System.getProperty("os.name");
                    
                    if (os.equals("SunOS")) {
                        throw new IllegalStateException(
                                "Don't panic. This is a Solaris bug");
                    } else {
                        throw new RuntimeException("Real test failure");
                    }
                }
                break;
            }
        }
    }

    static boolean use(KeyPair kp) throws Exception {
        Signature sig = Signature.getInstance("SHA1withDSA");
        sig.initSign(kp.getPrivate());
        sig.update(data);
        byte[] signed = sig.sign();
        Signature sig2 = Signature.getInstance("SHA1withDSA");
        sig2.initVerify(kp.getPublic());
        sig2.update(data);
        return sig2.verify(signed);
   }
}
