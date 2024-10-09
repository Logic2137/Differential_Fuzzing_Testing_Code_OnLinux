import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;

public class SignatureLength {

    public static void main(String[] args) throws Exception {
        for (Provider p0 : Security.getProviders()) {
            for (Provider p1 : Security.getProviders()) {
                for (Provider p2 : Security.getProviders()) {
                    if (!p0.getName().equals("SunMSCAPI") && p1.getName().equals("SunMSCAPI"))
                        continue;
                    if (p0.getName().equals("SunMSCAPI") && !p1.getName().equals("SunMSCAPI"))
                        continue;
                    boolean mayNotThrow = p2.getName().equals("SunMSCAPI") || p2.getName().startsWith("SunPKCS11");
                    main0("EC", 256, "SHA256withECDSA", p0, p1, p2, mayNotThrow);
                    main0("RSA", 2048, "SHA256withRSA", p0, p1, p2, mayNotThrow);
                    main0("DSA", 2048, "SHA256withDSA", p0, p1, p2, mayNotThrow);
                }
            }
        }
    }

    private static void main0(String keyAlgorithm, int keysize, String signatureAlgorithm, Provider generatorProvider, Provider signerProvider, Provider verifierProvider, boolean mayNotThrow) throws Exception {
        KeyPairGenerator generator;
        Signature signer;
        Signature verifier;
        try {
            generator = KeyPairGenerator.getInstance(keyAlgorithm, generatorProvider);
            signer = Signature.getInstance(signatureAlgorithm, signerProvider);
            verifier = Signature.getInstance(signatureAlgorithm, verifierProvider);
        } catch (NoSuchAlgorithmException nsae) {
            return;
        }
        byte[] plaintext = "aaa".getBytes("UTF-8");
        generator.initialize(keysize);
        System.out.println("Generating " + keyAlgorithm + " keypair using " + generator.getProvider().getName() + " JCE provider");
        KeyPair keypair = generator.generateKeyPair();
        signer.initSign(keypair.getPrivate());
        signer.update(plaintext);
        System.out.println("Signing using " + signer.getProvider().getName() + " JCE provider");
        byte[] signature = signer.sign();
        System.out.println("Invalidating signature ...");
        byte[] badSignature = new byte[signature.length + 5];
        System.arraycopy(signature, 0, badSignature, 0, signature.length);
        badSignature[signature.length] = 0x01;
        badSignature[signature.length + 1] = 0x01;
        badSignature[signature.length + 2] = 0x01;
        badSignature[signature.length + 3] = 0x01;
        badSignature[signature.length + 4] = 0x01;
        verifier.initVerify(keypair.getPublic());
        verifier.update(plaintext);
        System.out.println("Verifying using " + verifier.getProvider().getName() + " JCE provider");
        try {
            boolean valid = verifier.verify(badSignature);
            System.out.println("Valid? " + valid);
            if (mayNotThrow) {
                if (valid) {
                    throw new Exception("ERROR: expected a SignatureException but none was thrown" + " and invalid signature was verified");
                } else {
                    System.out.println("OK: verification failed as expected");
                }
            } else {
                throw new Exception("ERROR: expected a SignatureException but none was thrown");
            }
        } catch (SignatureException e) {
            System.out.println("OK: caught expected exception: " + e);
        }
        System.out.println();
    }
}
