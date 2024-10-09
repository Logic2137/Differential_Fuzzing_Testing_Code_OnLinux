import java.awt.Component;
import java.awt.Container;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Insets;
import java.awt.Robot;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TabProb extends JFrame {

    static TabProb tb1;

    static TabProb tb2;

    class FixLayout implements LayoutManager {

        @Override
        public void layoutContainer(Container C) {
            Insets in = C.getInsets();
            int w = 200 - in.left - in.right;
            int h = 100 - in.top - in.bottom;
            C.getComponents()[0].setBounds(in.top, in.left, w, h);
        }

        @Override
        public Dimension minimumLayoutSize(Container C) {
            return new Dimension(200, 100);
        }

        @Override
        public Dimension preferredLayoutSize(Container C) {
            return new Dimension(200, 100);
        }

        @Override
        public void removeLayoutComponent(Component c) {
        }

        @Override
        public void addLayoutComponent(String s, Component c) {
        }
    }

    public TabProb(int layoutPolicy) {
        JTabbedPane tabpanel = new JTabbedPane();
        tabpanel.setTabPlacement(JTabbedPane.TOP);
        tabpanel.setTabLayoutPolicy(layoutPolicy);
        JPanel panel = new JPanel(new FixLayout());
        JLabel label = new JLabel("Label");
        label.setBorder(BorderFactory.createLineBorder(Color.green, 3));
        panel.add(label);
        tabpanel.add("TEST", panel);
        add(tabpanel, BorderLayout.CENTER);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            System.out.println("Test for LookAndFeel " + laf.getClassName());
            UIManager.setLookAndFeel(laf.getClassName());
            test();
            System.out.println("Test passed for LookAndFeel " + laf.getClassName());
        }
    }

    public static void test() throws Exception {
        Robot robot = new Robot();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                tb1 = new TabProb(JTabbedPane.SCROLL_TAB_LAYOUT);
                tb1.pack();
                tb1.setVisible(true);
                tb2 = new TabProb(JTabbedPane.WRAP_TAB_LAYOUT);
                tb2.pack();
                tb2.setVisible(true);
            }
        });
        double tb1height = tb1.getPreferredSize().getHeight();
        double tb2height = tb2.getPreferredSize().getHeight();
        System.out.println(tb1height);
        System.out.println(tb2height);
        robot.waitForIdle();
        robot.delay(2000);
        SwingUtilities.invokeAndWait(() -> tb1.dispose());
        SwingUtilities.invokeAndWait(() -> tb2.dispose());
        if (tb1height != tb2height) {
            throw new RuntimeException("JTabbedPane preferred size calculation is wrong for SCROLL_TAB_LAYOUT");
        }
    }
}
