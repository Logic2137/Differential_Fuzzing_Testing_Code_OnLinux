import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Robot;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class AltKeyBug {

    private static JFrame f;

    private static boolean rightAltPressed = false;

    private static boolean throwException = false;

    private static String errorString;

    public static void main(String[] args) throws Exception {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(50);
            SwingUtilities.invokeAndWait(() -> {
                JTextField comp = new JTextField();
                comp.addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        System.out.println("ModEx : " + e.getModifiersEx());
                        System.out.println("Mod : " + e.getModifiers());
                        System.out.println("ALT_DOWN : " + e.isAltDown());
                        System.out.println("ALT_GR_DOWN: " + e.isAltGraphDown());
                        System.out.println("-----------");
                        if (rightAltPressed && !e.isAltGraphDown()) {
                            throwException = true;
                            errorString = "Right Alt press was sent but not received back.";
                        } else if (!rightAltPressed && e.isAltGraphDown()) {
                            throwException = true;
                            errorString = "Left Alt press was sent, but received Right Alt";
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                });
                f = new JFrame();
                f.add(comp);
                f.setSize(100, 100);
                f.setVisible(true);
            });
            for (int i = 0; i < 20; i++) {
                rightAltPressed = true;
                robot.keyPress(KeyEvent.VK_ALT_GRAPH);
                robot.keyRelease(KeyEvent.VK_ALT_GRAPH);
                robot.waitForIdle();
                if (throwException) {
                    throw new RuntimeException(errorString);
                }
                rightAltPressed = false;
                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyRelease(KeyEvent.VK_ALT);
                robot.waitForIdle();
                if (throwException) {
                    throw new RuntimeException(errorString);
                }
            }
        } finally {
            SwingUtilities.invokeAndWait(() -> {
                if (f != null)
                    f.dispose();
            });
        }
        System.out.println("Test passed.");
    }
}
