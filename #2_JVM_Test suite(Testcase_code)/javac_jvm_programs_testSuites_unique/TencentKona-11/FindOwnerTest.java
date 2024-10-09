



import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FindOwnerTest extends Applet
{

    private boolean gained;

    public void init() {
        super.init();
    }

    @Override
    public void start() {
        Window owner = SwingUtilities.windowForComponent(this);

        Window window1 = new Window(owner);
        window1.setVisible(true);

        Window window2 = new Window(window1);
        window2.setFocusable(true);
        JTextField field = new JTextField("JTextField");
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                gained = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        window2.setBounds(100, 100, 200, 200);
        window2.add(field);
        window2.setVisible(true);

        try {
            gained = false;
            Robot robot = new Robot();
            robot.setAutoDelay(50);
            robot.waitForIdle();
            robot.delay(200);

            Point p = field.getLocationOnScreen();
            System.out.println(p);
            robot.mouseMove(p.x + 1, p.y + 1);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.waitForIdle();
            robot.delay(200);

            if (!gained) {
                throw new Exception("Focus is not gained upon mouse click");
            }
            System.out.println("ok");
        } catch (SecurityException e) {

            JOptionPane optionPane = new JOptionPane(
                    "You are in the browser so test is manual. Try to " +
                    "click \"JTextField\" in the opened window then press OK " +
                    "below",
                    JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            JDialog dialog =
                    optionPane.createDialog(null,"FindOwnerTest instruction");
            dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
            dialog.setVisible(true);
            if (!gained) {
                throw new RuntimeException(
                        "Focus is not gained upon mouse click");
            }
            System.out.println("ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            window1.dispose();
            stop();
        }
    }
}
