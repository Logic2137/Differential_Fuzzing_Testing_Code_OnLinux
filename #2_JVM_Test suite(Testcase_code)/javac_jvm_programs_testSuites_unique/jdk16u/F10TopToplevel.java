



import java.awt.*;
import java.awt.event.*;

public class F10TopToplevel {

    static Frame frame;
    static Dialog dialog;
    static volatile boolean menuToggled = false;

    public static void main(final String[] args) {
        MenuBar mb;
        Menu menu;
        MenuItem item;
        frame = new Frame("am below");
        frame.setMenuBar( (mb=new MenuBar()) );
        menu = new Menu("nu");
        menu.add((item = new MenuItem("item")));
        item.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent ae ) {
                menuToggled = true;
            }
        });
        mb.add(menu);

        frame.setSize(200,200);
        frame.setLocation( 400,100 );
        frame.setVisible( true );

        dialog = new Dialog(frame);
        dialog.setSize( 100,100 );
        dialog.setVisible(true);

        Robot robot;
        try {
            robot = new Robot();
            robot.setAutoDelay(5);
        } catch(AWTException e){
            throw new RuntimeException("cannot create robot.", e);
        }
        robot.waitForIdle();
        robot.mouseMove(dialog.getLocationOnScreen().x + dialog.getWidth()/2,
                        dialog.getLocationOnScreen().y + dialog.getHeight()/2 );
        robot.waitForIdle();
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_F10);
        robot.keyRelease(KeyEvent.VK_F10);

        robot.delay(10);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.waitForIdle();
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.waitForIdle();

        if(menuToggled) {
            throw new RuntimeException("Oops! Menu should not open.");
        }

    }
}
