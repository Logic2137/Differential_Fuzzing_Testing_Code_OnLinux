import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Policy;

public class NullCodeSource {

    public static void main(String[] args) throws Exception {
        Policy policy = Policy.getPolicy();
        PermissionCollection perms = policy.getPermissions((CodeSource) null);
        if (perms.elements().hasMoreElements()) {
            System.err.println(perms);
            throw new Exception("PermissionCollection is not empty");
        }
    }
}
