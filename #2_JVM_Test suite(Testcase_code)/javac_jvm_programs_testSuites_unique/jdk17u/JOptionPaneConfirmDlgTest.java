import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class JOptionPaneConfirmDlgTest {

    JInternalFrame textFrame;

    JFrame f = null;

    public static void main(String[] args) throws Exception {
        new JOptionPaneConfirmDlgTest();
    }

    public JOptionPaneConfirmDlgTest() throws Exception {
        try {
            SwingUtilities.invokeAndWait(() -> createGUI());
            Thread.sleep(10000);
        } finally {
            SwingUtilities.invokeAndWait(() -> f.dispose());
        }
    }

    public void createGUI() {
        JOptionPane.showMessageDialog((Component) null, "An internalFrame with 2 buttons will be displayed. \n" + " Press \"Hit me 1\" button. The bug causes a RuntimeException to be thrown here\n" + " But If a confirmation dialog comes, test has passed\n" + " Similarly, press \"Hit me 2\" button. The bug will cause a RuntimeException\n" + " to be thrown here but if a confirmation dialog comes, test has passed.\n" + " Close the dialog and frame.", "information", JOptionPane.INFORMATION_MESSAGE);
        f = new JFrame();
        textFrame = new JInternalFrame("Main-Frame", true);
        f.setContentPane(textFrame);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        f.setSize(dim.width / 6, dim.height / 5);
        textFrame.setBounds(10, 10, dim.width / 8, dim.height / 8);
        textFrame.setVisible(true);
        JButton b1 = new JButton("Hit me 1");
        b1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showInternalConfirmDialog(null, "Test?");
            }
        });
        JButton b2 = new JButton("Hit me 2");
        b2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showInternalConfirmDialog(new JInternalFrame(), "Test?");
            }
        });
        textFrame.setLayout(new FlowLayout());
        textFrame.add(b1);
        textFrame.add(b2);
        f.setVisible(true);
    }
}
