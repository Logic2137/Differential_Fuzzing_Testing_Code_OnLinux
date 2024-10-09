



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EnterAsGrabbedEvent
{
    
    private static Frame frame;
    private static Button button;
    private static volatile boolean enterTriggered = false;
    private static volatile boolean actionTriggered = false;

    private static void init()
    {
        frame = new Frame();
        frame.setLayout(new FlowLayout());
        button = new Button("button");
        button.addActionListener(actionEvent -> {
            actionTriggered = true;
        });
        frame.add(button);
        frame.setBounds(100, 100, 200, 200);
        frame.setVisible(true);
        frame.validate();
      }

    public static void main(String[] args) throws Exception {
        try {
            Robot r = new Robot();
            r.setAutoDelay(200);
            r.setAutoWaitForIdle(true);
            SwingUtilities.invokeAndWait(EnterAsGrabbedEvent::init);
            r.waitForIdle();

            Point loc = button.getLocationOnScreen();
            r.mouseMove(loc.x+button.getWidth()/2, loc.y+button.getHeight()/2);
            r.mousePress(InputEvent.BUTTON1_MASK);

            
            
            
            frame.addMouseListener(
                    new MouseAdapter() {
                        public void mouseEntered(MouseEvent me) {
                            System.out.println(me);
                            enterTriggered = true;
                        }

                        
                        public void mouseExited(MouseEvent me) {
                            System.out.println(me);
                        }
                    });

            
            button.addMouseListener(
                    new MouseAdapter(){
                        public void mouseEntered(MouseEvent me){
                            System.out.println(me);
                        }
                        public void mouseExited(MouseEvent me){
                            System.out.println(me);
                        }
                    });

            r.mouseMove(loc.x+button.getWidth() + 1, loc.y+button.getHeight()/2);

            r.mouseRelease(InputEvent.BUTTON1_MASK);

            if (!enterTriggered) {
                throw new RuntimeException("Test failed. MouseEntered was not triggered");
            }

            if (actionTriggered) {
                throw new RuntimeException("Test failed. ActionEvent triggered");
            }
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }
}
