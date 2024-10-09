

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;


public class AccessibleIndexInParentTest {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(AccessibleIndexInParentTest::test);
    }

    private static void test() {

        int N = 5;
        JTabbedPane tabbedPane = new JTabbedPane();

        for (int i = 0; i < N; i++) {
            tabbedPane.addTab("Title: " + i, new JLabel("Component: " + i));
        }

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component child = tabbedPane.getComponentAt(i);

            AccessibleContext ac = child.getAccessibleContext();
            if (ac == null) {
                throw new RuntimeException("Accessible Context is null!");
            }

            int index = ac.getAccessibleIndexInParent();
            Accessible parent = ac.getAccessibleParent();

            if (parent.getAccessibleContext().getAccessibleChild(index) != child) {
                throw new RuntimeException("Wrong getAccessibleIndexInParent!");
            }
        }
    }
}
