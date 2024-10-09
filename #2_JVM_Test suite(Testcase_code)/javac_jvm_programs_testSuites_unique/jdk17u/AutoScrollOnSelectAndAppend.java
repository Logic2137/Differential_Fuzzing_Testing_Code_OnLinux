import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.TextArea;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AutoScrollOnSelectAndAppend {

    Frame frame;

    TextArea textArea;

    Button buttonHoldFocus;

    Robot robot;

    int test;

    int selectScrollPos1;

    int selectScrollPos2;

    String selectionText;

    public void composeTextArea() {
        String filler = "";
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                filler = filler + i + i + "\n";
            }
        }
        selectScrollPos1 = filler.length();
        String text = filler + "FirstScroll\n";
        filler = "";
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                filler = filler + i + i + "\n";
            }
        }
        text = text + filler;
        selectScrollPos2 = text.length();
        text = text + "SecondScroll\n";
        filler = "";
        for (int i = 4; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                filler = filler + i + i + "\n";
            }
        }
        text = text + filler;
        textArea.setText(text);
        textArea.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() % 2 == 0) {
                    if (!(textArea.getSelectedText().contains(selectionText))) {
                        dispose();
                        throw new RuntimeException("Test No: " + test + ": TextArea is not auto scrolled to show the" + " select/append text.");
                    }
                }
            }
        });
    }

    public AutoScrollOnSelectAndAppend() {
        try {
            robot = new Robot();
        } catch (Exception ex) {
            throw new RuntimeException("Robot Creation Failed.");
        }
        frame = new Frame();
        frame.setSize(200, 200);
        frame.setLayout(new FlowLayout());
        textArea = new TextArea(5, 20);
        composeTextArea();
        frame.add(textArea);
        buttonHoldFocus = new Button("HoldFocus");
        frame.add(buttonHoldFocus);
        frame.setVisible(true);
        robot.waitForIdle();
        Point loc = textArea.getLocationOnScreen();
        robot.mouseMove(loc.x + 8, loc.y + 8);
        robot.waitForIdle();
    }

    public void doubleClick() {
        robot.waitForIdle();
        robot.delay(500);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(100);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
    }

    public void setFocusOnButton() {
        buttonHoldFocus.requestFocusInWindow();
        robot.waitForIdle();
    }

    public void selectAutoScrollTest1() {
        test = 1;
        setFocusOnButton();
        textArea.select(selectScrollPos1, selectScrollPos1);
        selectionText = "11";
        doubleClick();
    }

    public void selectAutoScrollTest2() {
        test = 2;
        setFocusOnButton();
        textArea.select(selectScrollPos2, selectScrollPos2);
        selectionText = "33";
        doubleClick();
    }

    public void appendAutoScrollTest() {
        test = 3;
        setFocusOnButton();
        selectionText = "55";
        textArea.append("appendScroll");
        doubleClick();
    }

    public void dispose() {
        frame.dispose();
    }

    public static void main(String[] args) {
        AutoScrollOnSelectAndAppend test = new AutoScrollOnSelectAndAppend();
        test.selectAutoScrollTest1();
        test.selectAutoScrollTest2();
        test.appendAutoScrollTest();
        test.dispose();
    }
}
