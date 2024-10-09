

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;


public final class MouseModifiersInKeyEvent {

    private static int modifiersEX = 0;
    private static int modifiers = 0;
    private static JFrame f;
    private static Rectangle bounds;

    public static void main(final String[] args) throws Exception {
        for (int i = 1; i <= MouseInfo.getNumberOfButtons(); ++i) {
            test(InputEvent.getMaskForButton(i));
        }
    }

    private static void test(final int mask) throws Exception {
        final Robot r = new Robot();
        r.setAutoDelay(100);
        r.setAutoWaitForIdle(true);

        EventQueue.invokeAndWait(MouseModifiersInKeyEvent::createAndShowGUI);
        r.waitForIdle();
        EventQueue.invokeAndWait(() -> bounds = f.getBounds());

        r.mouseMove(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
        r.mousePress(mask);
        r.keyPress(KeyEvent.VK_A);
        r.keyRelease(KeyEvent.VK_A);

        EventQueue.invokeAndWait(() -> f.dispose());

        r.mouseRelease(mask);

        
        if (modifiersEX != mask) {
            System.err.println("Expected modifiersEX: " + mask);
            System.err.println("Actual modifiersEX: " + modifiersEX);
            throw new RuntimeException();
        }
        
        if (modifiersEX == InputEvent.BUTTON1_DOWN_MASK) {
            if (modifiers != InputEvent.BUTTON1_MASK) {
                System.err.println("Expected modifiers: " + InputEvent.BUTTON1_MASK);
                System.err.println("Actual modifiers: " + modifiers);
                throw new RuntimeException();
            }
        }
        modifiersEX = 0;
        modifiers = 0;
    }

    private static void createAndShowGUI() {
        f = new JFrame();

        final Component component = new JTextField();
        component.addKeyListener(new MyKeyListener());

        f.add(component);
        f.setSize(300, 300);
        f.setLocationRelativeTo(null);
        f.setAlwaysOnTop(true);
        f.setVisible(true);
    }

    static final class MyKeyListener extends KeyAdapter {

        public void keyPressed(final KeyEvent e) {
            modifiersEX = e.getModifiersEx();
            modifiers = e.getModifiers();
        }
    }
}

