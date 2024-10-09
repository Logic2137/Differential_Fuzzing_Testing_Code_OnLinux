import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import java.awt.*;

public class bug6777378 {

    private static JFrame frame;

    private static JTableHeader header;

    public static void main(String[] args) throws Exception {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(20);
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        UIManager.setLookAndFeel(new MetalLookAndFeel());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JTable table = new JTable(new AbstractTableModel() {

                        public int getRowCount() {
                            return 10;
                        }

                        public int getColumnCount() {
                            return 10;
                        }

                        public Object getValueAt(int rowIndex, int columnIndex) {
                            return "" + rowIndex + " " + columnIndex;
                        }
                    });
                    header = new JTableHeader(table.getColumnModel());
                    header.setToolTipText("hello");
                    frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.add(header);
                    frame.setSize(300, 300);
                    frame.setVisible(true);
                }
            });
            robot.waitForIdle();
            Point point = header.getLocationOnScreen();
            robot.mouseMove(point.x + 20, point.y + 50);
            robot.mouseMove(point.x + 30, point.y + 50);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        } finally {
            if (frame != null)
                SwingUtilities.invokeAndWait(() -> frame.dispose());
        }
    }
}
