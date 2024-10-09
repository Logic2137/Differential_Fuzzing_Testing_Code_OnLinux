



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TextAreaMixing {

    private static volatile boolean menuClicked = false;
    private static JMenuItem menuItem;

    public static void main(String[] args) throws Exception {
        
        

        final JFrame frame = new JFrame("JFrame");
        frame.setLayout(new GridLayout(0, 1));
        frame.setSize(200, 200);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Test Menu");

        for (int i = 0; i < 6; i++) {
            JMenuItem mi = new JMenuItem(Integer.toString(i));
            mi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    menuClicked = true;
                }
            });
            menu.add(mi);

            
            if (i == 3) {
                menuItem = mi;
            }
        }
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(new TextArea());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Thread.sleep(2000);

        Robot robot = new Robot();

        
        Point loc = menu.getLocationOnScreen();
        robot.mouseMove(loc.x + menu.getWidth() / 2, loc.y + menu.getHeight() / 2);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(500);

        
        loc = menuItem.getLocationOnScreen();
        robot.mouseMove(loc.x + menuItem.getWidth() / 2, loc.y + menuItem.getHeight() / 2);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(500);

        frame.dispose();

        if (!menuClicked) {
            throw new RuntimeException("A menu item has never been clicked.");
        }
    }
}
