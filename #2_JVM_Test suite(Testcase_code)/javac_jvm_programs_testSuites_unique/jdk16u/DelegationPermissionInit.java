

import javax.security.auth.kerberos.DelegationPermission;
import java.util.List;


public class DelegationPermissionInit {
    public static void main(String[] args) throws Exception {
        var goodOnes = List.of(
                "\"aaa\" \"bbb\"",
                "\"aaa\"  \"bbb\""
        );
        var badOnes = List.of(
                "\"user@REALM\"",
                "\"\"\" \"bbb\"",
                "\"aaa\" \"\"\"",
                "\"\"\" \"\"\"",
                "\"aaa\" \"bbb",
                "\"\"aaa\"\" \"\"bbb\"\"",
                "\"aaa\" \"bbb\"\"\"",
                "\"aaa\"-\"bbb\"",
                "\"aaa\" - \"bbb\"",
                "\"aaa\"- \"bbb\"",
                "\"aaa\" \"bbb\"  ",
                "aaa\" \"bbb\"  "
        );
        boolean failed = false;
        for (var good : goodOnes) {
            System.out.println(">>> " + good);
            try {
                new DelegationPermission(good);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                System.out.println("Failed");
                failed = true;
            }
        }
        for (var bad : badOnes) {
            System.out.println(">>> " + bad);
            try {
                new DelegationPermission(bad);
                System.out.println("Failed");
                failed = true;
            } catch (IllegalArgumentException e) {
                e.printStackTrace(System.out);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                System.out.println("Failed");
                failed = true;
            }
        }
        if (failed) {
            throw new Exception("Failed");
        }
    }
}
