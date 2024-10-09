





import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class ClickDuringKeypress implements MouseListener
 {
   
   final static int CLICKCOUNT = 10;
   final static int DOUBLE_CLICK_AUTO_DELAY = 10;
   volatile int lastClickCount = 0;
   volatile boolean clicked = false;
   volatile boolean ready = false;

   Frame frame;
   Robot robot;

   public void init()
    {
      
      
      

      frame = new Frame("ClickDuringKeypress");
      frame.addMouseListener(this);
      frame.addWindowListener(new WindowAdapter() {
          public void windowActivated(WindowEvent e) {
              synchronized(ClickDuringKeypress.this) {
                  ready = true;
                  ClickDuringKeypress.this.notifyAll();
              }
          }
      });
      frame.setBounds(0, 0, 400, 400);

      start();

    }

   public void start ()
    {
      try {
        robot = new Robot();
      } catch (AWTException e) {
        System.out.println("Could not create Robot.");
        throw new RuntimeException("Couldn't create Robot.  Test fails");
      }

      robot.mouseMove(200, 200);
      frame.show();

      synchronized(this) {
          try {
              if (!ready) {
                  wait(10000);
              }
          } catch (InterruptedException ex) {
          }
          if (!ready) {
              System.out.println("Not Activated. Test fails");
              throw new RuntimeException("Not Activated. Test fails");
          }
      }

      doTest();

      
      
      
      

    }

    
    private void doTest() {
      robot.setAutoDelay(2000);
      robot.waitForIdle();
      robot.keyPress(KeyEvent.VK_B);
      robot.mousePress(InputEvent.BUTTON1_MASK);
      robot.delay(10);
      robot.mouseRelease(InputEvent.BUTTON1_MASK);
      
      robot.keyRelease(KeyEvent.VK_B);
      robot.delay(1000);

      robot.setAutoDelay(DOUBLE_CLICK_AUTO_DELAY);
      for (int i = 0; i < CLICKCOUNT / 2; i++) {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.keyPress(KeyEvent.VK_B);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_B);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
      }
      robot.waitForIdle();
      
      robot.delay(200);
      if (!clicked) {
          System.out.println("No MOUSE_CLICKED events received.  Test fails.");
          throw new RuntimeException("No MOUSE_CLICKED events received.  Test fails.");
      }
      if (lastClickCount != CLICKCOUNT) {
          System.out.println("Actual click count: " + lastClickCount + " does not match expected click count: " + CLICKCOUNT + ".  Test fails");
          throw new RuntimeException("Actual click count: " + lastClickCount + " does not match expected click count: " + CLICKCOUNT + ".  Test fails");

      }
      
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.toString());
        clicked = true;
        lastClickCount = e.getClickCount();
    }

     public static void main(String[] args) {
         new ClickDuringKeypress().init();
     }

 }
