import java.security.SecureRandom;
import java.security.Security;

public class AutoReseed {

    public static void main(String[] args) throws Exception {
        SecureRandom sr;
        boolean pass = true;
        for (String mech : new String[] { "Hash_DRBG", "HMAC_DRBG", "CTR_DRBG" }) {
            try {
                System.out.println("Testing " + mech + "...");
                Security.setProperty("securerandom.drbg.config", mech);
                sr = SecureRandom.getInstance("DRBG");
                sr.nextInt();
                sr = SecureRandom.getInstance("DRBG");
                sr.reseed();
                sr = SecureRandom.getInstance("DRBG");
                sr.generateSeed(10);
            } catch (Exception e) {
                pass = false;
                e.printStackTrace(System.out);
            }
        }
        if (!pass) {
            throw new RuntimeException("At least one test case failed");
        }
    }
}
