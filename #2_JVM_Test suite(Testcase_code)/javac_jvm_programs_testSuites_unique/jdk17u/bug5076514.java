import java.awt.GraphicsEnvironment;
import java.security.Permission;
import javax.swing.JEditorPane;

public class bug5076514 {

    private final static String ACCESS_CLIPBOARD = "accessClipboard";

    private static boolean isCheckPermissionCalled = false;

    public static void main(String[] args) {
        System.setSecurityManager(new MySecurityManager());
        boolean expected = !GraphicsEnvironment.isHeadless();
        JEditorPane editor = new JEditorPane();
        editor.copy();
        if (isCheckPermissionCalled != expected) {
            throw new RuntimeException("JEditorPane's clipboard operations " + "didn't call SecurityManager.checkPermission() with " + "permission 'accessClipboard' when there is a security" + " manager installed");
        }
    }

    private static class MySecurityManager extends SecurityManager {

        @Override
        public void checkPermission(Permission perm) {
            if (ACCESS_CLIPBOARD.equals(perm.getName())) {
                isCheckPermissionCalled = true;
            }
        }
    }
}
