import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import java.lang.Exception;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class MSOID {

    public static void main(String[] args) throws Exception {
        byte[] header = Files.readAllBytes(Paths.get(System.getProperty("test.src"), "msoid.txt"));
        byte[] token = Base64.getMimeDecoder().decode(Arrays.copyOfRange(header, 10, header.length));
        GSSCredential cred = null;
        GSSContext ctx = GSSManager.getInstance().createContext(cred);
        try {
            ctx.acceptSecContext(token, 0, token.length);
            throw new Exception("Should fail");
        } catch (GSSException gsse) {
            gsse.printStackTrace();
            if (gsse.getMajor() != GSSException.NO_CRED) {
                throw gsse;
            }
            for (StackTraceElement st : gsse.getStackTrace()) {
                if (st.getClassName().startsWith("sun.security.jgss.krb5.")) {
                    return;
                }
            }
            throw gsse;
        }
    }
}
