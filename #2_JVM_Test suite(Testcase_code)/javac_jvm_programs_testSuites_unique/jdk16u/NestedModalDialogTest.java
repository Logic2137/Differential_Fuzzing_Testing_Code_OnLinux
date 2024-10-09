












import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class NestedModalDialogTest {
    private static Frame frame;
    private static IntermediateDialog interDiag;
    private static TextDialog txtDiag;

    
    private static Button[] robot_button = new Button[2];
    private static TextField robot_text = null;
    private static Robot robot = null;

    private static void blockTillDisplayed(Component comp) {
        Point p = null;
        while (p == null) {
            try {
                p = comp.getLocationOnScreen();
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    private static void clickOnComp(Component comp) {
        Rectangle bounds = new Rectangle(comp.getLocationOnScreen(), comp.getSize());
        robot.mouseMove(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
        robot.waitForIdle();
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
    }

    public void testModalDialogs() throws Exception {
        try {
            robot = new Robot();
            robot.setAutoDelay(100);

            
            frame = new StartFrame();
            blockTillDisplayed(frame);
            clickOnComp(robot_button[0]);

            
            blockTillDisplayed(interDiag);
            clickOnComp(robot_button[1]);

            
            blockTillDisplayed(robot_text);
            clickOnComp(robot_text);

            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_H);
            robot.keyRelease(KeyEvent.VK_H);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.waitForIdle();

            robot.keyPress(KeyEvent.VK_E);
            robot.keyRelease(KeyEvent.VK_E);
            robot.waitForIdle();

            robot.keyPress(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_L);
            robot.waitForIdle();

            robot.keyPress(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_L);
            robot.waitForIdle();

            robot.keyPress(KeyEvent.VK_O);
            robot.keyRelease(KeyEvent.VK_O);
            robot.waitForIdle();
        } finally {
            if (frame != null) {
                frame.dispose();
            }
            if (interDiag != null) {
                interDiag.dispose();
            }
            if (txtDiag != null) {
                txtDiag.dispose();
            }
        }
    }

    
    
    class StartFrame extends Frame {

        
        public StartFrame() {
            super("First Frame");
            setLayout(new GridBagLayout());
            setLocation(375, 200);
            setSize(271, 161);
            Button but = new Button("Make Intermediate");
            but.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    interDiag = new IntermediateDialog(StartFrame.this);
                    interDiag.setSize(300, 200);

                    
                    interDiag.setLocation(getLocationOnScreen());
                    interDiag.pack();
                    interDiag.setVisible(true);
                }
            });
            Panel pan = new Panel();
            pan.add(but);
            add(pan);
            setVisible(true);
            robot_button[0] = but;
        }
    }

    
    
    class IntermediateDialog extends Dialog {

        Dialog m_parent;

        public IntermediateDialog(Frame parent) {
            super(parent, "Intermediate Modal", true );
            m_parent = this;
            Button but = new Button("Make Text");
            but.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    txtDiag = new TextDialog(m_parent);
                    txtDiag.setSize(300, 100);
                    txtDiag.setVisible(true);
                }
            });
            Panel pan = new Panel();
            pan.add(but);
            add(pan);
            pack();

            
            robot_button[1] = but;
        }
    }

    
    class TextDialog extends Dialog {

        public TextDialog(Dialog parent) {
            super(parent, "Modal Dialog", true );
            TextField txt = new TextField("", 10);
            Panel pan = new Panel();
            pan.add(txt);
            add(pan);
            pack();

            
            robot_text = txt;
        }
    }

    public static void main(String[] args) throws RuntimeException, Exception {
        try {
            new NestedModalDialogTest().testModalDialogs();
        } catch (Exception e) {
            throw new RuntimeException("NestedModalDialogTest object creation "
                    + "failed");
        }
    }
}
