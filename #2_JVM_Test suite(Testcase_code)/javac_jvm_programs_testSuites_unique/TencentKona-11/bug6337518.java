



import javax.swing.*;
import javax.swing.plaf.basic.*;

public class bug6337518 extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
        return null;
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JComboBox comboBox = new JComboBox();
                comboBox.setUI(new bug6337518());
            }
        });
    }
}
