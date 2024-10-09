import javax.swing.*;
import java.awt.*;

public class bug6274267 {

    private JFrame frame;

    private Component left;

    public static void main(String[] args) throws Exception {
        final bug6274267 test = new bug6274267();
        Robot robot = new Robot();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    test.setupUI1();
                }
            });
            robot.waitForIdle();
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    test.setupUI2();
                }
            });
            test.test();
        } finally {
            if (test.frame != null) {
                test.frame.dispose();
            }
        }
    }

    private void setupUI1() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        left = new JPanel();
        left.setPreferredSize(new Dimension(100, 100));
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(100, 50));
        Component right = new JScrollPane(rightPanel);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        frame = new JFrame();
        frame.add(split);
        frame.pack();
    }

    private void setupUI2() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void test() throws Exception {
        if (left.getSize().width == 100) {
            System.out.println("Test passed");
        } else {
            throw new RuntimeException("ScrollPaneLayout sometimes improperly " + "calculates the preferred layout size. ");
        }
    }
}
