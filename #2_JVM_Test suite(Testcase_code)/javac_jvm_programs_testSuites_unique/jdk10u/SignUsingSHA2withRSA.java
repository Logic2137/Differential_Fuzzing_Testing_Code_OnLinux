



import java.security.*;
import java.util.*;

public class SignUsingSHA2withRSA {

    private static final byte[] toBeSigned = new byte[] {
        0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10
    };

    private static List<byte[]> generatedSignatures = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        Provider[] providers = Security.getProviders("Signature.SHA256withRSA");
        if (providers == null) {
            System.out.println("No JCE providers support the " +
                "'Signature.SHA256withRSA' algorithm");
            System.out.println("Skipping this test...");
            return;

        } else {
            System.out.println("The following JCE providers support the " +
                "'Signature.SHA256withRSA' algorithm: ");
            for (Provider provider : providers) {
                System.out.println("    " + provider.getName());
            }
        }
        System.out.println("-------------------------------------------------");

        KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
        ks.load(null, null);
        System.out.println("Loaded keystore: Windows-MY");

        Enumeration<String> e = ks.aliases();
        PrivateKey privateKey = null;
        PublicKey publicKey = null;

        while (e.hasMoreElements()) {
            String alias = e.nextElement();
            if (alias.equals("6753664")) {
                System.out.println("Loaded entry: " + alias);
                privateKey = (PrivateKey) ks.getKey(alias, null);
                publicKey = (PublicKey) ks.getCertificate(alias).getPublicKey();
            }
        }
        if (privateKey == null || publicKey == null) {
            throw new Exception("Cannot load the keys need to run this test");
        }
        System.out.println("-------------------------------------------------");

        generatedSignatures.add(signUsing("SHA256withRSA", privateKey));
        generatedSignatures.add(signUsing("SHA384withRSA", privateKey));
        generatedSignatures.add(signUsing("SHA512withRSA", privateKey));

        System.out.println("-------------------------------------------------");

        verifyUsing("SHA256withRSA", publicKey, generatedSignatures.get(0));
        verifyUsing("SHA384withRSA", publicKey, generatedSignatures.get(1));
        verifyUsing("SHA512withRSA", publicKey, generatedSignatures.get(2));

        System.out.println("-------------------------------------------------");
    }

    private static byte[] signUsing(String signAlgorithm,
        PrivateKey privateKey) throws Exception {

        
        
        Signature sig1 = Signature.getInstance(signAlgorithm, "SunMSCAPI");
        if (sig1 == null) {
            throw new Exception("'" + signAlgorithm + "' is not supported");
        }
        System.out.println("Using " + signAlgorithm + " signer from the " +
            sig1.getProvider().getName() + " JCE provider");

        System.out.println("Using key: " + privateKey);
        sig1.initSign(privateKey);
        sig1.update(toBeSigned);
        byte [] sigBytes = null;

        try {
            sigBytes = sig1.sign();
            System.out.println("Generated RSA signature over a " +
                toBeSigned.length + "-byte data (signature length: " +
                sigBytes.length * 8 + " bits)");
            System.out.println(String.format("0x%0" +
                (sigBytes.length * 2) + "x",
                new java.math.BigInteger(1, sigBytes)));

        } catch (SignatureException se) {
                System.out.println("Error generating RSA signature: " + se);
        }

        return sigBytes;
    }

    private static void verifyUsing(String signAlgorithm, PublicKey publicKey,
        byte[] signature) throws Exception {

        
        
        Signature sig1 = Signature.getInstance(signAlgorithm, "SunMSCAPI");
        if (sig1 == null) {
            throw new Exception("'" + signAlgorithm + "' is not supported");
        }
        System.out.println("Using " + signAlgorithm + " verifier from the "
            + sig1.getProvider().getName() + " JCE provider");

        System.out.println("Using key: " + publicKey);

        System.out.println("\nVerifying RSA Signature over a " +
            toBeSigned.length + "-byte data (signature length: " +
            signature.length * 8 + " bits)");
        System.out.println(String.format("0x%0" + (signature.length * 2) +
            "x", new java.math.BigInteger(1, signature)));

        sig1.initVerify(publicKey);
        sig1.update(toBeSigned);

        if (sig1.verify(signature)) {
            System.out.println("Verify PASSED\n");
        } else {
            throw new Exception("Verify FAILED");
        }
    }
}
