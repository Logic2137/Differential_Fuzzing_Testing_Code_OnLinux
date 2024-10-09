



import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignatureException;

public class SignatureLength {

    public static void main(String[] args) throws Exception {
        main0("EC", 256, "SHA256withECDSA", "SunEC");
        main0("RSA", 2048, "SHA256withRSA", "SunRsaSign");
        main0("DSA", 2048, "SHA256withDSA", "SUN");

        if (System.getProperty("os.name").equals("SunOS")) {
            main0("EC", 256, "SHA256withECDSA", null);
            main0("RSA", 2048, "SHA256withRSA", null);
        }
    }

    private static void main0(String keyAlgorithm, int keysize,
            String signatureAlgorithm, String provider) throws Exception {
        byte[] plaintext = "aaa".getBytes("UTF-8");

        
        KeyPairGenerator generator =
            provider == null ?
                (KeyPairGenerator) KeyPairGenerator.getInstance(keyAlgorithm) :
                (KeyPairGenerator) KeyPairGenerator.getInstance(
                                       keyAlgorithm, provider);
        generator.initialize(keysize);
        System.out.println("Generating " + keyAlgorithm + " keypair using " +
            generator.getProvider().getName() + " JCE provider");
        KeyPair keypair = generator.generateKeyPair();

        
        Signature signer =
            provider == null ?
                Signature.getInstance(signatureAlgorithm) :
                Signature.getInstance(signatureAlgorithm, provider);
        signer.initSign(keypair.getPrivate());
        signer.update(plaintext);
        System.out.println("Signing using " + signer.getProvider().getName() +
            " JCE provider");
        byte[] signature = signer.sign();

        
        System.out.println("Invalidating signature ...");
        byte[] badSignature = new byte[signature.length + 5];
        System.arraycopy(signature, 0, badSignature, 0, signature.length);
        badSignature[signature.length] = 0x01;
        badSignature[signature.length + 1] = 0x01;
        badSignature[signature.length + 2] = 0x01;
        badSignature[signature.length + 3] = 0x01;
        badSignature[signature.length + 4] = 0x01;

        
        Signature verifier =
            provider == null ?
                Signature.getInstance(signatureAlgorithm) :
                Signature.getInstance(signatureAlgorithm, provider);
        verifier.initVerify(keypair.getPublic());
        verifier.update(plaintext);
        System.out.println("Verifying using " +
            verifier.getProvider().getName() + " JCE provider");

        try {
            System.out.println("Valid? " + verifier.verify(badSignature));
            throw new Exception(
                "ERROR: expected a SignatureException but none was thrown");
        } catch (SignatureException e) {
            System.out.println("OK: caught expected exception: " + e);
        }
        System.out.println();
    }
}
