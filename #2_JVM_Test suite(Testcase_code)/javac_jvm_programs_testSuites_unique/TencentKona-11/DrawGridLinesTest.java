
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;


public class DrawGridLinesTest {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 150;
    private static final Color GRID_COLOR = Color.BLACK;
    private static final Color TABLE_BACKGROUND_COLOR = Color.BLUE;
    private static final Color CELL_RENDERER_BACKGROUND_COLOR = Color.YELLOW;
    private static final int SCALE = 2;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(DrawGridLinesTest::checkTableGridLines);
    }

    private static void checkTableGridLines() {

        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() {
                return 10;
            }

            public int getRowCount() {
                return 10;
            }

            public Object getValueAt(int row, int col) {
                return " ";
            }
        };

        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setOpaque(true);
        r.setBackground(CELL_RENDERER_BACKGROUND_COLOR);

        JTable table = new JTable(dataModel);
        table.setSize(WIDTH, HEIGHT);
        table.setDefaultRenderer(Object.class, r);
        table.setGridColor(GRID_COLOR);
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setBackground(TABLE_BACKGROUND_COLOR);

        checkTableGridLines(table);
    }

    private static void checkTableGridLines(JTable table) {

        int w = SCALE * WIDTH;
        int h = SCALE * HEIGHT;

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.scale(SCALE, SCALE);
        table.paint(g);
        g.dispose();

        int size = Math.min(w, h);
        int rgb = TABLE_BACKGROUND_COLOR.getRGB();

        for (int i = 0; i < size; i++) {
            if (img.getRGB(i, i) == rgb || img.getRGB(i, size - i - 1) == rgb) {
                throw new RuntimeException("Artifacts in the table background color!");
            }
        }
    }
}
