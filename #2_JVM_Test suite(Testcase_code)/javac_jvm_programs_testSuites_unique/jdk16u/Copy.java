

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignedObject;


public class Copy {

    private static final String DSA = "DSA";
    private static final int KEY_SIZE = 512;
    private static final int MAGIC = 123;

    public static void main(String args[]) throws Exception {
        KeyPairGenerator kg = KeyPairGenerator.getInstance(DSA);
        kg.initialize(KEY_SIZE);
        KeyPair kp = kg.genKeyPair();

        Signature signature = Signature.getInstance(DSA);
        Test original = new Test();
        SignedObject so = new SignedObject(original, kp.getPrivate(),
                signature);
        System.out.println("Signature algorithm: " + so.getAlgorithm());

        signature = Signature.getInstance(DSA, "SUN");
        if (!so.verify(kp.getPublic(), signature)) {
            throw new RuntimeException("Verification failed");
        }

        kg = KeyPairGenerator.getInstance(DSA);
        kg.initialize(KEY_SIZE);
        kp = kg.genKeyPair();

        if (so.verify(kp.getPublic(), signature)) {
            throw new RuntimeException("Unexpected success");
        }

        Object copy = so.getObject();
        if (!original.equals(copy)) {
            throw new RuntimeException("Signed object is not equal "
                    + "to original one: " + copy);
        }

        
        original.set(MAGIC - 1);
        copy = so.getObject();
        if (original.equals(copy)) {
            throw new RuntimeException("Signed object is not a copy "
                    + "of original one: " + copy);
        }

        System.out.println("Test passed");
    }

    private static class Test implements Serializable {
        private int number = MAGIC;

        public int get() {
            return number;
        }

        public void set(int magic) {
            this.number = magic;
        }

        @Override
        public int hashCode() {
            return number;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (!(obj instanceof Test)) {
                return false;
            }

            Test other = (Test) obj;
            return number == other.number;
        }

        @Override
        public String toString() {
            return "" + number;
        }
    }
}
