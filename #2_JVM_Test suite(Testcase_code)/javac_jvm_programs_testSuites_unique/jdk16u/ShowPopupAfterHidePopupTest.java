



import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.metal.*;

public class ShowPopupAfterHidePopupTest {
    private static JFrame frame = null;
    private static JComboBox comboBox = null;
    private static boolean popupIsVisible = false;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new MetalLookAndFeel());
        Robot robot = new Robot();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame("Popup Menu of JComboBox");
                comboBox = new JComboBox(new String[]{"Item1", "Item2", "Item3"});
                frame.getContentPane().add(comboBox);
                frame.pack();
                frame.setVisible(true);
            }
        });
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                comboBox.showPopup();
                comboBox.hidePopup();
                comboBox.showPopup();
            }
        });
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                popupIsVisible = comboBox.isPopupVisible();
                frame.dispose();
            }
        });
        if (!popupIsVisible) {
            throw new RuntimeException("Calling hidePopup() affected the next call to showPopup().");
        }
    }
}
