



import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;
import java.awt.event.KeyEvent;

public class bug6236162 {
    private static JFrame frame;
    private static JComboBox combo;
    private static MyComboUI comboUI;
    private static Robot robot;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
            robot.waitForIdle();
            robot.delay(1000);
            test();
            System.out.println("Test passed");
        } finally {
            if (frame != null) SwingUtilities.invokeAndWait(() -> frame.dispose());
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame("bug6236162");

        combo = new JComboBox(new String[]{"one", "two", "three", "four", "five"});
        combo.setEditable(true);
        comboUI = new MyComboUI();
        combo.setUI(comboUI);
        combo.setSelectedIndex(3);
        frame.getContentPane().add(combo);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.toFront();
    }

    private static void test() throws AWTException {

        
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);

        
        robot.waitForIdle();
        Point p = combo.getLocationOnScreen();
        Dimension size = combo.getSize();
        p.x += size.width / 2;
        p.y += size.height;
        float dy = 1;
        robot.mouseMove(p.x, p.y - 5);
        for (int i=1; i <= 10; i++) {
            robot.mouseMove((int)(p.x), (int)(p.y - 5 + dy*i));
        }

        
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);

        robot.waitForIdle();
        JList list = comboUI.getComboPopup().getList();
        if (list.getSelectedIndex() != 1) {
            throw new RuntimeException("There is an inconsistence in combo box " +
                    "behavior when user points an item in combo popup " +
                    "by mouse and then uses UP/DOWN keys.");
        }
    }


    
    private static class MyComboUI extends MetalComboBoxUI {
        public ComboPopup getComboPopup() {
            return popup;
        }
    }
}
