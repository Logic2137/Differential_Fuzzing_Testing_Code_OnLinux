



import javax.swing.*;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class bug6889007 {

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(20);

        final JFrame frame = new JFrame();
        frame.setUndecorated(true);

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JTableHeader th = new JTableHeader();
                th.setColumnModel(new JTable(20, 5).getColumnModel());

                th.setUI(new MyTableHeaderUI());

                frame.add(th);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        robot.waitForIdle();
        Point point = frame.getLocationOnScreen();
        int shift = 10;
        int x = point.x;
        int y = point.y + frame.getHeight()/2;
        for(int i = -shift; i < frame.getWidth() + 2*shift; i++) {
            robot.mouseMove(x++, y);
        }
        robot.waitForIdle();
        
        if (MyTableHeaderUI.getTestValue() != 9) {
            throw new RuntimeException("Unexpected test number "
                    + MyTableHeaderUI.getTestValue());
        }
        System.out.println("ok");
    }

    static class MyTableHeaderUI extends BasicTableHeaderUI {
        private static int testValue;

        protected void rolloverColumnUpdated(int oldColumn, int newColumn) {
            increaseTestValue(newColumn);
            Cursor cursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
            if (oldColumn != -1 && newColumn != -1 &&
                    header.getCursor() != cursor) {
                throw new RuntimeException("Wrong type of cursor!");
            }
        }

        private static synchronized void increaseTestValue(int increment) {
            testValue += increment;
        }

        public static synchronized int getTestValue() {
            return testValue;
        }
    }
}
