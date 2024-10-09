



import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;

public class bug6406264 extends JComboBox {

    static JFrame frame;
    static bug6406264 comboBox;

    public static void main(String[] args) throws Exception {

        Robot robot = new Robot();
        
        SwingUtilities.invokeAndWait(
                new Runnable() {
                    public void run() {
                        frame = new JFrame("JComboBox6406264 test");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        comboBox = new bug6406264("One", "Two", "Three");
                        frame.getContentPane().add(comboBox);

                        frame.setLocationRelativeTo(null);
                        frame.pack();
                        frame.setVisible(true);
                    }
                }
        );

        robot.waitForIdle();

        
        SwingUtilities.invokeAndWait(
                new Runnable() {
                    public void run() {
                        comboBox.showPopup();
                    }
                });
        robot.waitForIdle();

        
        SwingUtilities.invokeAndWait(
                new Runnable() {
                    public void run() {
                        if (comboBox.getUI().isPopupVisible(comboBox) == false)
                        {
                            throw new RuntimeException("A focusable popup is not visible " +
                                    "in JComboBox!");
                        }
                    }
                }
        );

        frame.dispose();
    }

    public bug6406264(Object ... items) {
        super(items);
    }

    public void updateUI() {
        setUI(new CustomComboBoxUI());
    }

    
    private class CustomComboBoxUI extends BasicComboBoxUI {
        protected ComboPopup createPopup() {
            return new FocusablePopup(bug6406264.this);
        }
    }

    
    private class FocusablePopup extends BasicComboPopup {
        public FocusablePopup(JComboBox combo) {
            super(combo);
        }

        public boolean isFocusable() {
            return true;
        }
    }
}
