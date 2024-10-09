import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;

public class SHA1PRNGReseed {

    public static void main(String[] args) throws Exception {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(0);
        sr.nextInt();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        new ObjectOutputStream(bout).writeObject(sr);
        SecureRandom sr2 = (SecureRandom) new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray())).readObject();
        int i1 = sr.nextInt();
        sr2.setSeed(1);
        int i2 = sr2.nextInt();
        if (i1 == i2) {
            throw new Exception("output should not be the same");
        }
    }
}
