import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;

public class GssMemoryIssues {

    public static void main(String[] argv) throws Exception {
        GSSManager man = GSSManager.getInstance();
        String s = "me@REALM";
        GSSName name = man.createName(s, GSSName.NT_USER_NAME);
        byte[] exported = name.export();
        int lenOffset = exported.length - s.length() - 4;
        exported[lenOffset] = 0x7f;
        try {
            man.createName(exported, GSSName.NT_EXPORT_NAME);
        } catch (GSSException gsse) {
            System.out.println(gsse);
        }
    }
}
