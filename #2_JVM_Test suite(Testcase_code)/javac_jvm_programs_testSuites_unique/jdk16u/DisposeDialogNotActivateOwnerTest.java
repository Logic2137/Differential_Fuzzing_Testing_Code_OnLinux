



import java.awt.AWTException;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;

public class DisposeDialogNotActivateOwnerTest {
    Robot robot;
    Frame frame;
    Frame dialogInvisibleOwner;
    Dialog dialog;
    Button frameButton;
    static volatile boolean buttonReceivedFocus = false;

    public static void main(String[] args) {
        DisposeDialogNotActivateOwnerTest test =
                new DisposeDialogNotActivateOwnerTest();
        test.performTest();
        test.dispose();
    }

    public DisposeDialogNotActivateOwnerTest() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Error: unable to create robot");
        }

        robot.setAutoDelay(200);
        dialogInvisibleOwner = new Frame("Dialog Invisible Owner Frame");
        dialog = new Dialog(dialogInvisibleOwner, "Owned Dialog");

        frame = new Frame("A Frame");
        frameButton = new Button("button");
        frameButton.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                buttonReceivedFocus = true;
            }
        });
        frame.setBounds(0, 0, 400, 200);
        frame.add(frameButton);
        dialog.setBounds(100, 50, 200, 100);
    }

    public void performTest() {
        frame.setVisible(true);
        robot.delay(200);
        robot.waitForIdle();
        clickOnTitle(frame);
        robot.delay(200);
        robot.waitForIdle();
        robot.delay(200);
        if (!frame.isFocused()) {
            dispose();
            throw new RuntimeException("Error: frame didn't get initial focus");
        }

        dialog.setVisible(true);
        robot.delay(200);
        robot.waitForIdle();
        robot.delay(200);
        if (!dialog.isFocused()) {
            dispose();
            throw new RuntimeException("Error: dialog didn't get initial focus");
        }

        dialog.dispose();
        robot.waitForIdle();
        robot.delay(200);
        if (!buttonReceivedFocus) {
            dispose();
            throw new RuntimeException(
                "Test failed: Dialog activates invisible owner when disposed!");
        }
    }

    public void dispose() {
        frame.dispose();
        dialog.dispose();
        dialogInvisibleOwner.dispose();
    }

    void clickOnTitle(Component c) {
        Point p = c.getLocationOnScreen();
        Dimension d = c.getSize();
        robot.mouseMove(p.x + (int)(d.getWidth() / 2),
                        p.y + ((Frame)c).getInsets().top / 2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
