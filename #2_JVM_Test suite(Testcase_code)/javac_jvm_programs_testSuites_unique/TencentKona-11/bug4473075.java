



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.InputEvent;

public class bug4473075 {
    public static final int USER_HEADER_HEIGHT = 40;
    private static JTable table;
    private static JScrollPane scpScroll;
    private static Point point;
    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(20);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                frame = new JFrame();
                frame.setUndecorated(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                table = new JTable();
                String t = "a cell text";
                table.setModel(new DefaultTableModel(
                        new Object[][]{new Object[]{t, t, t, t, t}},
                        new Object[]{t, t, t, t, t}));
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                scpScroll = new JScrollPane(table);

                
                Dimension preferredSize = new Dimension(table.getSize().width,
                        USER_HEADER_HEIGHT);
                table.getTableHeader().setPreferredSize(preferredSize);

                frame.setContentPane(scpScroll);
                frame.setSize(250, 480);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                point = scpScroll.getHorizontalScrollBar()
                        .getLocationOnScreen();
            }
        });
        robot.waitForIdle();

        robot.mouseMove(point.x + 100, point.y + 5);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseMove(point.x + 150, point.y + 5);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);

        int headerH = table.getTableHeader().getHeight();
        if (headerH != USER_HEADER_HEIGHT) {
            throw new RuntimeException("TableHeader height was not set: "
                    + headerH + " !=" + USER_HEADER_HEIGHT);
        }

        double tableX = table.getX();
        int headerX = table.getTableHeader().getX();
        if (tableX != headerX) {
            throw new RuntimeException("TableHeader X position is wrong: "
                    + tableX + " !=" + headerX);
        }

        double tableW = table.getWidth();
        int headerW = table.getTableHeader().getWidth();
        if (tableW != headerW) {
            throw new RuntimeException("TableHeader width is wrong: "
                    + tableW + " !=" + headerW);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.dispose();
            }
        });
        System.out.println("ok");
    }
}
