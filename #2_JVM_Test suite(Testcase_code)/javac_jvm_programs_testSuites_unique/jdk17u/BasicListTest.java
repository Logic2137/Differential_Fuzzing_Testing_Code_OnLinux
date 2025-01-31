import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class MyList extends JList {

    @Override
    public int locationToIndex(final Point location) {
        final int n = super.locationToIndex(location);
        final Rectangle q = getCellBounds(n, n);
        return q != null && q.contains(location) ? n : -1;
    }
}

public class BasicListTest {

    private static void initComponents() {
        f = new JFrame();
        jScrollPane1 = new JScrollPane();
        list1 = new MyList();
        f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        list1.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            @Override
            public int getSize() {
                return strings.length;
            }

            @Override
            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane1.setViewportView(list1);
        f.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
        p = list1.getLocationOnScreen();
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            try {
                SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
                System.out.println("Test for LookAndFeel " + laf.getClassName());
                SwingUtilities.invokeAndWait(() -> {
                    initComponents();
                });
                System.out.println("Test passed for LookAndFeel " + laf.getClassName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Robot robot = new Robot();
            robot.setAutoDelay(200);
            robot.mouseMove(p.x, p.y);
            robot.waitForIdle();
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
            robot.keyPress(KeyEvent.VK_PAGE_DOWN);
            robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            SwingUtilities.invokeAndWait(() -> {
                f.dispose();
            });
        }
    }

    private static JScrollPane jScrollPane1;

    private static MyList list1;

    private static Point p;

    private static JFrame f;
}
