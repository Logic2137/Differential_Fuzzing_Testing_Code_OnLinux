import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Component;
import javax.swing.JOptionPane;

public class ComponentResizeTest {

    private static JFrame demoFrame;

    public static void testresize() throws Exception {
        Thread.sleep(5000);
        for (int i = 0; i < 20; i++) {
            SwingUtilities.invokeLater(() -> {
                demoFrame.setSize(demoFrame.getWidth() + 5, demoFrame.getHeight() + 5);
            });
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JOptionPane.showMessageDialog((Component) null, "The test creates a transparent JFrame and resizes the JFrame. Please verify JFrame is transparent and components (like JButton, checkbox) move without leaving any trails", "information", JOptionPane.INFORMATION_MESSAGE);
            createAndShowGUI();
        });
        try {
            testresize();
        } finally {
            SwingUtilities.invokeLater(() -> {
                demoFrame.dispose();
            });
        }
        SwingUtilities.invokeAndWait(() -> {
            int confirm = JOptionPane.showConfirmDialog((Component) null, "Did the component resize work without leaving any trails?", "alert", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Test passed");
            } else {
                System.out.println("Test failed");
                throw new RuntimeException("Component resize leaves trail");
            }
        });
    }

    private static void createAndShowGUI() {
        demoFrame = new JFrame();
        demoFrame.setSize(300, 300);
        demoFrame.setLayout(new FlowLayout());
        demoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demoFrame.setUndecorated(true);
        demoFrame.setBackground(new Color(0f, 0, 0, 0.1f));
        JCheckBox b = new JCheckBox("Whatever");
        demoFrame.paintAll(null);
        b.setOpaque(true);
        demoFrame.add(b);
        demoFrame.add(new JButton());
        demoFrame.setVisible(true);
    }
}
