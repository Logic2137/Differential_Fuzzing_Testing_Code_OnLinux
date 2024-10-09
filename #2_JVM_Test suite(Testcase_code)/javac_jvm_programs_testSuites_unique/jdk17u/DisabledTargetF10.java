import java.awt.*;
import java.awt.event.*;

public class DisabledTargetF10 implements ActionListener {

    Button b1 = new Button("Disable");

    Button b3 = new Button("Dummy");

    TextField b2 = new TextField("Click Me");

    Button b4 = new Button("Dummy, too");

    Panel p = new Panel(new GridLayout(1, 3));

    MenuItem exit = new MenuItem("exit");

    Frame frame;

    Robot robot = null;

    boolean success = false;

    public static final int MAX_COUNT = 100;

    public void init() {
        frame = new Frame();
        frame.setSize(300, 300);
        Menu file = new Menu("file");
        exit.setShortcut(new MenuShortcut(KeyEvent.VK_SPACE, false));
        exit.addActionListener(this);
        MenuBar mb = new MenuBar();
        mb.add(file);
        file.add(exit);
        frame.setMenuBar(mb);
        p.add(b1);
        p.add(b2);
        p.add(b3);
        frame.add("North", b4);
        frame.add("South", p);
        b1.addActionListener(this);
    }

    public void start() {
        boolean isX = Toolkit.getDefaultToolkit().getClass().getName().equals("sun.awt.X11.XToolkit");
        frame.setLocation(300, 300);
        frame.setVisible(true);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("unable to create robot", e);
        }
        robot.delay(1000);
        Point pt;
        frame.toFront();
        pt = getLocation(b1);
        robot.delay(200);
        int x, y;
        x = pt.x + b1.getWidth() / 2;
        y = pt.y + b1.getHeight() / 2;
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(200);
        robot.keyPress(KeyEvent.VK_F10);
        robot.delay(40);
        robot.keyRelease(KeyEvent.VK_F10);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(40);
        robot.keyRelease(KeyEvent.VK_ENTER);
        if (!isX) {
            robot.delay(200);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(40);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }
        robot.delay(200);
        if (!success) {
            throw new RuntimeException("Menu was not brought down by F10, apparently.");
        }
    }

    public static Point getLocation(Component co) throws RuntimeException {
        Point pt = null;
        boolean bFound = false;
        int count = 0;
        while (!bFound) {
            try {
                pt = co.getLocationOnScreen();
                bFound = true;
            } catch (Exception ex) {
                bFound = false;
                count++;
            }
            if (!bFound && count > MAX_COUNT) {
                throw new RuntimeException("don't see a component to get location");
            }
        }
        return pt;
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == b1) {
            b1.setEnabled(false);
            b2.setEnabled(false);
            b3.setEnabled(false);
        } else if (evt.getSource() == exit) {
            success = true;
        }
    }

    public static void main(String[] args) {
        DisabledTargetF10 imt = new DisabledTargetF10();
        imt.init();
        imt.start();
    }
}
