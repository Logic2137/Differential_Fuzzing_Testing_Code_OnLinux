import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;

public class bug7142955 {

    private static final Color TEST_COLOR = Color.RED;

    public static void main(String[] args) throws Exception {
        UIManager.put("Tree.rendererFillBackground", Boolean.FALSE);
        UIManager.put("Tree.textBackground", TEST_COLOR);
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                int w = 200;
                int h = 100;
                BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics g = image.getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, image.getWidth(), image.getHeight());
                DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
                renderer.setSize(w, h);
                renderer.paint(g);
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        if (image.getRGB(x, y) == TEST_COLOR.getRGB()) {
                            throw new RuntimeException("Test bug7142955 failed");
                        }
                    }
                }
                System.out.println("Test bug7142955 passed.");
            }
        });
    }
}
