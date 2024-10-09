



import javax.swing.*;
import javax.swing.plaf.LayerUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

public class bug7068740 extends JFrame {

    private static Robot robot = null;
    private static JTable table = null;

    bug7068740() {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public int getRowCount() {
                return 20;
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int row, int column) {
                return "(" + row + "," + column + ")";
            }
        };

        table = new JTable(model);
        table.setRowSelectionInterval(0, 0);
        LayerUI<JComponent> layerUI = new LayerUI<>();
        JLayer<JComponent> layer = new JLayer<>(table, layerUI);
        JScrollPane scrollPane = new JScrollPane(layer);
        add(scrollPane);
        pack();
        setLocationRelativeTo(null);
    }

    private static void setUp() {
        try {
            if (robot == null) {
                robot = new Robot();
                robot.setAutoDelay(50);
            }

            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    bug7068740 test = new bug7068740();
                    test.setVisible(true);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed");
        } catch (AWTException e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed");
        }
    }

    private static int getSelectedRow() throws Exception {
        final AtomicInteger row = new AtomicInteger(-1);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                row.set(table.getSelectedRow());
            }
        });
        return row.intValue();
    }

    private static void doTest() throws Exception {
        robot.waitForIdle();

        robot.keyPress(KeyEvent.VK_PAGE_DOWN);
        robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
        robot.waitForIdle();

        if (getSelectedRow() != 19) {
            throw new RuntimeException("Test failed");
        }

        robot.keyPress(KeyEvent.VK_PAGE_UP);
        robot.keyRelease(KeyEvent.VK_PAGE_UP);
        robot.waitForIdle();
        if (getSelectedRow() != 0) {
            throw new RuntimeException("Test failed");
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
            setUp();
            doTest();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed");
        }
    }
}
