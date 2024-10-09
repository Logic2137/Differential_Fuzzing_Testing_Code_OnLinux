



import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;

public class CtorTests2 {

    
    public static void main(String[] argv) throws Exception {
        try {
            GSSManager manager = GSSManager.getInstance();
            GSSName name = manager.createName("anonymous", GSSName.NT_ANONYMOUS);
            boolean anonymous = name.isAnonymous();
            if (anonymous == false) {
                throw new RuntimeException("GSSName.isAnonymous() returns false for GSSName.NT_ANONYMOUS");
            }
        } catch (GSSException e) {
            System.out.println("Not supported, ignored!");
        }
    }

}
