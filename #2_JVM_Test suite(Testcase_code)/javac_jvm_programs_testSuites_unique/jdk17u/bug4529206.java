import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class bug4529206 extends JFrame {

    static JFrame frame;

    static JToolBar jToolBar1;

    public bug4529206() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel jPanFrame = (JPanel) this.getContentPane();
        jPanFrame.setLayout(new BorderLayout());
        this.setSize(new Dimension(200, 100));
        this.setLocation(125, 75);
        this.setTitle("Test Floating Toolbar");
        jToolBar1 = new JToolBar();
        JButton jButton1 = new JButton("Float");
        jPanFrame.add(jToolBar1, BorderLayout.NORTH);
        JTextField tf = new JTextField("click here");
        jPanFrame.add(tf);
        jToolBar1.add(jButton1, null);
        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                buttonPressed(e);
            }
        });
        makeToolbarFloat();
        setVisible(true);
    }

    private void makeToolbarFloat() {
        javax.swing.plaf.basic.BasicToolBarUI ui = (javax.swing.plaf.basic.BasicToolBarUI) jToolBar1.getUI();
        if (!ui.isFloating()) {
            ui.setFloatingLocation(100, 100);
            ui.setFloating(true, jToolBar1.getLocation());
        }
    }

    private void buttonPressed(ActionEvent e) {
        makeToolbarFloat();
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                frame = new bug4529206();
            }
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                if (frame.isFocused()) {
                    throw (new RuntimeException("setFloating does not work correctly"));
                }
            }
        });
    }
}
