



import java.awt.AWTPermission;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.security.Permission;

public class Permissions {

    static class MySecurityManager extends SecurityManager {
        private List<Permission> permissionsChecked = new ArrayList<>();

        static MySecurityManager install() {
            MySecurityManager sm = new MySecurityManager();
            System.setSecurityManager(sm);
            return sm;
        }

        @Override
        public void checkPermission(Permission perm) {
            permissionsChecked.add(perm);
        }

        void prepare(String msg) {
            System.out.println(msg);
            permissionsChecked.clear();
        }

        
        void assertChecked(Class<? extends Permission> type, String name) {
            for (Permission perm: permissionsChecked) {
                if (type.isInstance(perm) && perm.getName().equals(name))
                    return;
            }
            throw new RuntimeException(type.getName() + "(\"" + name + "\") not checked");
        }
    }

    public static void main(String[] args) {
        MySecurityManager sm = MySecurityManager.install();

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        sm.prepare("Toolkit.getSystemClipboard()");
        toolkit.getSystemClipboard();
        sm.assertChecked(AWTPermission.class, "accessClipboard");

        sm.prepare("Toolkit.getSystemEventQueue()");
        toolkit.getSystemEventQueue();
        sm.assertChecked(AWTPermission.class, "accessEventQueue");

        sm.prepare("Toolkit.getSystemSelection()");
        toolkit.getSystemSelection();
        

        sm.prepare("Window(Frame)");
        new Window((Frame)null);
        sm.assertChecked(AWTPermission.class, "showWindowWithoutWarningBanner");

        sm.prepare("Window(Window)");
        new Window((Window)null);
        sm.assertChecked(AWTPermission.class, "showWindowWithoutWarningBanner");

        sm.prepare("Window(Window,GraphicsConfiguration)");
        new Window((Window)null, (GraphicsConfiguration)null);
        sm.assertChecked(AWTPermission.class, "showWindowWithoutWarningBanner");
    }
}
