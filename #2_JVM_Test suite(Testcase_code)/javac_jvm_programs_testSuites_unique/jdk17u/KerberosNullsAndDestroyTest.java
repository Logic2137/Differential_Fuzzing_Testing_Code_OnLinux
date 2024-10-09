import javax.security.auth.kerberos.*;
import java.util.function.Supplier;

public class KerberosNullsAndDestroyTest {

    public static void main(String[] args) throws Exception {
        KerberosPrincipal c = new KerberosPrincipal("me@HERE");
        KerberosPrincipal s = new KerberosPrincipal("you@THERE");
        checkNPE(() -> new KerberosKey(c, null, 17, 1));
        checkNPE(() -> new EncryptionKey(null, 17));
        checkNPE(() -> new KerberosCredMessage(null, s, new byte[1]));
        checkNPE(() -> new KerberosCredMessage(c, null, new byte[1]));
        checkNPE(() -> new KerberosCredMessage(c, s, null));
        KerberosKey k1 = new KerberosKey(c, new byte[16], 17, 1);
        EncryptionKey k2 = new EncryptionKey(new byte[16], 17);
        KerberosCredMessage m = new KerberosCredMessage(c, s, new byte[1]);
        k1.destroy();
        checkISE(() -> k1.getAlgorithm());
        checkISE(() -> k1.getEncoded());
        checkISE(() -> k1.getFormat());
        checkISE(() -> k1.getKeyType());
        checkISE(() -> k1.getPrincipal());
        checkISE(() -> k1.getVersionNumber());
        k2.destroy();
        checkISE(() -> k2.getAlgorithm());
        checkISE(() -> k2.getEncoded());
        checkISE(() -> k2.getFormat());
        checkISE(() -> k2.getKeyType());
        m.destroy();
        checkISE(() -> m.getSender());
        checkISE(() -> m.getRecipient());
        checkISE(() -> m.getEncoded());
    }

    static void checkNPE(Supplier<?> f) throws Exception {
        check(f, NullPointerException.class);
    }

    static void checkISE(Supplier<?> f) throws Exception {
        check(f, IllegalStateException.class);
    }

    static void check(Supplier<?> f, Class<? extends Exception> type) throws Exception {
        try {
            f.get();
        } catch (Exception e) {
            if (e.getClass() != type) {
                throw e;
            } else {
                return;
            }
        }
        throw new Exception("Should fail");
    }
}
