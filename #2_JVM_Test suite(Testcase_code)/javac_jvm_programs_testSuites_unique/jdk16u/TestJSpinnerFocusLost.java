



import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TestJSpinnerFocusLost extends JFrame implements ChangeListener, FocusListener {

    JSpinner spinner;

    boolean spinnerGainedFocus = false;
    boolean spinnerLostFocus = false;

    static TestJSpinnerFocusLost b;
    Point p;
    Rectangle rect;
    static Robot robot;

    public static void blockTillDisplayed(Component comp) {
        Point p = null;
        while (p == null) {
            try {
                p = comp.getLocationOnScreen();
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public TestJSpinnerFocusLost() {
        spinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        spinner.addChangeListener(this);
        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().addFocusListener(this);
        getContentPane().add(spinner);
    }

    public void doTest() throws Exception {
        blockTillDisplayed(spinner);
        SwingUtilities.invokeAndWait(() -> {
            ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().requestFocus();
        });

        try {
            synchronized (TestJSpinnerFocusLost.this) {
                if (!spinnerGainedFocus) {
                    TestJSpinnerFocusLost.this.wait(2000);
                }
            }


            SwingUtilities.invokeAndWait(() -> {
                p = spinner.getLocationOnScreen();
                rect = spinner.getBounds();
            });
            robot.delay(1000);
            robot.mouseMove(p.x+rect.width-5, p.y+3);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            synchronized (TestJSpinnerFocusLost.this) {
                while (!spinnerLostFocus) {
                    TestJSpinnerFocusLost.this.wait(2000);
                }
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        if ( ((Integer) spinner.getValue()).intValue() != 11 ) {
            System.out.println("spinner value " + ((Integer) spinner.getValue()).intValue());
            throw new RuntimeException("Spinner value shouldn't be other than 11");
        }
    }


    private boolean changing = false;

    public void stateChanged(ChangeEvent e) {
        if (changing) {
            return;
        }
        JSpinner spinner = (JSpinner)e.getSource();
        int value = ((Integer) spinner.getValue()).intValue();
        if (value > 10) {
            changing = true;
            JOptionPane.showMessageDialog(spinner, "10 exceeded");
        }
    }

    public void focusGained(FocusEvent e) {
        synchronized (TestJSpinnerFocusLost.this) {
            spinnerGainedFocus = true;
            TestJSpinnerFocusLost.this.notifyAll();
        }
    }

    public void focusLost(FocusEvent e) {
        synchronized (TestJSpinnerFocusLost.this) {
            spinnerLostFocus = true;
            TestJSpinnerFocusLost.this.notifyAll();
        }
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported L&F: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] argv) throws Exception {
        robot = new Robot();
        robot.setAutoWaitForIdle(true);
        robot.setAutoDelay(250);
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            System.out.println("Testing L&F: " + laf.getClassName());
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            try {
                SwingUtilities.invokeAndWait(() -> {
                    b = new TestJSpinnerFocusLost();
                    b.pack();
                    b.setLocationRelativeTo(null);
                    b.setVisible(true);
                });
                robot.waitForIdle();
                b.doTest();
                robot.delay(500);
            } finally {
                SwingUtilities.invokeAndWait(() -> {
                    if (b != null) b.dispose();
                });
            }
            robot.delay(1000);
        }
    }
}
