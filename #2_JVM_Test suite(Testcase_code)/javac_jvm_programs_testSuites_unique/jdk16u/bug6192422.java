

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;


public class bug6192422 {

    private static boolean foundJMenuBar = false;

    public static void main(String[] args) throws Throwable {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                if (!testIt()) {
                    throw new RuntimeException("JMenuBar was not found");
                }
            }
        });
    }

    
    private static boolean testIt() {

        JFrame frame = new JFrame("bug6192422");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("foo"));
        menuBar.add(new JMenu("bar"));
        menuBar.add(new JMenu("baz"));
        frame.setJMenuBar(menuBar);

        findJMenuBar(frame.getAccessibleContext());
        return foundJMenuBar;
    }

    
    private static void findJMenuBar(AccessibleContext ac) {
        if (ac != null) {
            System.err.println("findJMenuBar: ac = "+ac.getClass());
            int num = ac.getAccessibleChildrenCount();
            System.err.println("  #children "+num);

            for (int i = 0; i < num; i++) {
                System.err.println("  child #"+i);
                Accessible a = ac.getAccessibleChild(i);
                AccessibleContext child = a.getAccessibleContext();
                AccessibleRole role = child.getAccessibleRole();
                System.err.println("  role "+role);
                if (role == AccessibleRole.MENU_BAR) {
                    foundJMenuBar = true;
                    return;
                }
                if (child.getAccessibleChildrenCount() > 0) {
                    findJMenuBar(child);
                }
            }
        }
    }
}
