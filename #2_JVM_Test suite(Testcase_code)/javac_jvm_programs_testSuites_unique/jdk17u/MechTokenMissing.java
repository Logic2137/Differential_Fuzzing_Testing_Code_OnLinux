import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;

public class MechTokenMissing {

    public static void main(String[] args) throws Exception {
        GSSCredential cred = null;
        GSSContext ctx = GSSManager.getInstance().createContext(cred);
        String var = "60 1C 06 06 2B 06 01 05 05 02 A0 12 30 10 A0 0E " + "30 0C 06 0A 2B 06 01 04 01 82 37 02 02 0A ";
        byte[] token = new byte[var.length() / 3];
        for (int i = 0; i < token.length; i++) {
            token[i] = Integer.valueOf(var.substring(3 * i, 3 * i + 2), 16).byteValue();
        }
        try {
            ctx.acceptSecContext(token, 0, token.length);
        } catch (GSSException gsse) {
            System.out.println("Expected exception: " + gsse);
        }
    }
}
