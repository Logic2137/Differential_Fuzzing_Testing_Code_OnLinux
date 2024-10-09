import java.security.SecureRandom;

public class MacNativePRNGSetSeed {

    public static void main(String[] args) throws Exception {
        SecureRandom sr = SecureRandom.getInstance("NativePRNG");
        sr.setSeed(1);
    }
}
