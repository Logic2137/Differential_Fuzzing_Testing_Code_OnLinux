



import javax.swing.*;
import java.awt.*;
import java.lang.reflect.*;


public class bug4816114 {

    JFrame fr;
    JSplitPane splitPane;

    boolean[] resized = new boolean[] { false, false, false,
                                        false, false, false };
    static int step = 0;
    boolean h_passed = false;
    boolean v_passed = false;

    static bug4816114 test = new bug4816114();

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, AWTException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                test.createAndShowGUI();
            }
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        Thread.sleep(1000);
        Thread.sleep(2000);

        step++;
        test.doTest(150, 300);

        step++;
        test.doTest(650, 300);

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                test.splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            }
        });

        step++;
        test.doTest(300, 650);

        step++;
        test.doTest(300, 150);

        step++;
        test.doTest(300, 650);

        if ( !test.isPassed() ) {
            throw new Error("The divider location is wrong.");
        }
    }
    public void createAndShowGUI() {
        fr = new JFrame("Test");

        splitPane = new TestSplitPane();
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0);
        splitPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        JButton leftButton = new JButton("LEFT");
        leftButton.setPreferredSize(new Dimension(300, 300));
        leftButton.setMinimumSize(new Dimension(150, 150));
        splitPane.setLeftComponent(leftButton);

        JButton rightButton = new JButton("RIGHT");
        rightButton.setPreferredSize(new Dimension(300, 300));
        rightButton.setMinimumSize(new Dimension(150, 150));
        splitPane.setRightComponent(rightButton);

        fr.getContentPane().add(splitPane, BorderLayout.CENTER);

        fr.pack();
        fr.setVisible(true);
    }

    void doTest(final int width, final int height)  throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                splitPane.setPreferredSize(new Dimension(width, height));
                fr.pack();
            }
        });

        synchronized (bug4816114.this) {
            while (!resized[step]) {
                bug4816114.this.wait();
            }
        }
    }

   synchronized void setPassed(int orientation, boolean passed) {
       if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
           this.h_passed = passed;
       }
       else {
           this.v_passed = passed;
       }
   }

    synchronized boolean isPassed() {
        return h_passed && v_passed;
    }


    class TestSplitPane extends JSplitPane {
        public void setDividerLocation(int location) {
            super.setDividerLocation(location);

            if ( splitPane.getDividerLocation() == 151 ) {
                setPassed(getOrientation(), true);
            }

            synchronized (bug4816114.this) {
                resized[step] = true;
                bug4816114.this.notifyAll();
            }
        }
    }
}
