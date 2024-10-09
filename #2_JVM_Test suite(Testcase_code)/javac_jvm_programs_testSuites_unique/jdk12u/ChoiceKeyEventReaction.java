



import java.awt.Robot;
import java.awt.Choice;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.TextField;
import java.awt.FlowLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ItemListener;
import java.awt.Frame;

public class ChoiceKeyEventReaction
{
    private static Robot robot;
    private static Choice choice1 = new Choice();
    private static Point pt;
    private static TextField tf = new TextField("Hi");
    private static boolean keyTypedOnTextField = false;
    private static boolean itemChanged = false;
    private static Frame frame;
    private static String toolkit;

    public static void main(String[] args) {
        createAndShowGUI();

        try {
            robot = new Robot();
            robot.setAutoDelay(100);

            moveFocusToTextField();
            testKeyOnChoice(InputEvent.BUTTON1_MASK, KeyEvent.VK_UP);
        } catch (Exception e) {
            throw new RuntimeException("Test failed. Exception thrown: "+e);
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    private static void createAndShowGUI() {
        frame = new Frame();
        toolkit = Toolkit.getDefaultToolkit().getClass().getName();
        System.out.println("Current toolkit is :" +toolkit);
        for (int i = 1; i<20; i++){
            choice1.add("item-0"+i);
        }

        tf.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke) {
                keyTypedOnTextField = true;
                System.out.println(ke);
            }
        });

        choice1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                itemChanged = true;
                System.out.println(e);
            }
        });
        choice1.setFocusable(false);

        frame.add(tf);
        frame.add(choice1);
        frame.setLayout (new FlowLayout());
        frame.setSize (200,200);
        frame.setVisible(true);
    }

    private static void testKeyOnChoice(int button, int key) {
        pt = choice1.getLocationOnScreen();
        robot.mouseMove(pt.x + choice1.getWidth()/2, pt.y + choice1.getHeight()/2);

        robot.mousePress(button);
        robot.mouseRelease(button);

        robot.keyPress(key);
        robot.keyRelease(key);

        System.out.println("keyTypedOnTextField = "+keyTypedOnTextField +": itemChanged = " + itemChanged);
        if (itemChanged) {
            throw new RuntimeException("Test failed. ItemChanged event occur on Choice.");
        }

       
       
       
       if (toolkit.equals("sun.awt.windows.WToolkit") &&
           !keyTypedOnTextField) {
           throw new RuntimeException("Test failed. (Win32) KeyEvent wasn't addressed to TextField. ");
       }

       if (!toolkit.equals("sun.awt.windows.WToolkit") &&
          keyTypedOnTextField) {
           throw new RuntimeException("Test failed. (XToolkit/MToolkit). KeyEvent was addressed to TextField.");
        }

        System.out.println("Test passed. Unfocusable Choice doesn't react on keys.");

        
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
    }

    public static void moveFocusToTextField() {
        pt = tf.getLocationOnScreen();
        robot.mouseMove(pt.x + tf.getWidth()/2, pt.y + tf.getHeight()/2);

        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
