import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Random;

public class InteropWithSunRsaSign {

    private static final SecureRandom NOT_SECURE_RANDOM = new SecureRandom() {

        Random r = new Random();

        @Override
        public void nextBytes(byte[] bytes) {
            r.nextBytes(bytes);
        }
    };

    private static boolean allResult = true;

    private static byte[] msg = "hello".getBytes();

    public static void main(String[] args) throws Exception {
        matrix(new PSSParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, 20, PSSParameterSpec.TRAILER_FIELD_BC));
        matrix(new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, PSSParameterSpec.TRAILER_FIELD_BC));
        matrix(new PSSParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, 48, PSSParameterSpec.TRAILER_FIELD_BC));
        matrix(new PSSParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, 64, PSSParameterSpec.TRAILER_FIELD_BC));
        matrix(new PSSParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, 17, PSSParameterSpec.TRAILER_FIELD_BC));
        if (!allResult) {
            throw new Exception("Failed");
        }
    }

    static void matrix(PSSParameterSpec pss) throws Exception {
        System.out.printf("\n%10s%20s%20s%20s  %s\n", pss.getDigestAlgorithm(), "KeyPairGenerator", "signer", "verifier", "result");
        System.out.printf("%10s%20s%20s%20s  %s\n", "-------", "----------------", "------", "--------", "------");
        String[] provsForKPG = { "SunRsaSign", "SunMSCAPI" };
        String[] provsForSignature = { "SunRsaSign", "SunMSCAPI", "-" };
        int pos = 0;
        for (String pg : provsForKPG) {
            for (String ps : provsForSignature) {
                for (String pv : provsForSignature) {
                    System.out.printf("%10d%20s%20s%20s  ", ++pos, pg, ps, pv);
                    try {
                        boolean result = test(pg, ps, pv, pss);
                        System.out.println(result);
                        if (!result) {
                            allResult = false;
                        }
                    } catch (Exception e) {
                        if (pg.equals("-") || pg.equals(ps)) {
                            allResult = false;
                            System.out.println("X " + e.getMessage());
                        } else {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    static boolean test(String pg, String ps, String pv, PSSParameterSpec pss) throws Exception {
        KeyPairGenerator kpg = pg.length() == 1 ? KeyPairGenerator.getInstance("RSA") : KeyPairGenerator.getInstance("RSA", pg);
        kpg.initialize(pss.getDigestAlgorithm().equals("SHA-512") ? 2048 : 1024, NOT_SECURE_RANDOM);
        KeyPair kp = kpg.generateKeyPair();
        PrivateKey pr = kp.getPrivate();
        PublicKey pu = kp.getPublic();
        Signature s = ps.length() == 1 ? Signature.getInstance("RSASSA-PSS") : Signature.getInstance("RSASSA-PSS", ps);
        s.initSign(pr);
        s.setParameter(pss);
        s.update(msg);
        byte[] sig = s.sign();
        Signature s2 = pv.length() == 1 ? Signature.getInstance("RSASSA-PSS") : Signature.getInstance("RSASSA-PSS", pv);
        s2.initVerify(pu);
        s2.setParameter(pss);
        s2.update(msg);
        return s2.verify(sig);
    }
}
