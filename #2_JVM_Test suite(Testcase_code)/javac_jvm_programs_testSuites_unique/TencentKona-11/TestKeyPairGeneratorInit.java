



import java.security.*;
import java.security.interfaces.*;

public class TestKeyPairGeneratorInit {

    private static class MySecureRandom extends SecureRandom {
        boolean isUsed = false;
        public MySecureRandom() {
            super();
        }

        public void nextBytes(byte[] bytes) {
            isUsed = true;
            super.nextBytes(bytes);
        }
    }

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg =
            KeyPairGenerator.getInstance("RSA", "SunRsaSign");
        MySecureRandom rnd = new MySecureRandom();
        kpg.initialize(2048, rnd);
        System.out.println("Generate keypair then check");
        KeyPair kp = kpg.generateKeyPair();
        if (!rnd.isUsed) {
            throw new RuntimeException("ERROR: Supplied random not used");
        } else {
            System.out.println("=> Test passed");
        }
    }
}
