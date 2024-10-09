import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public class FillTexturePaint {

    private static TexturePaint shape;

    private static final int size = 400;

    static {
        BufferedImage bi = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D gi = bi.createGraphics();
        gi.setBackground(Color.GREEN);
        gi.clearRect(0, 0, 50, 50);
        shape = new TexturePaint(bi, new Rectangle(0, 0, 50, 50));
    }

    public static void main(final String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(size, size);
        while (true) {
            vi.validate(gc);
            Graphics2D g2d = vi.createGraphics();
            g2d.setComposite(AlphaComposite.Src);
            g2d.setPaint(shape);
            g2d.fill(new Rectangle(0, 0, size, size));
            g2d.dispose();
            if (vi.validate(gc) != VolatileImage.IMAGE_OK) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            BufferedImage bi = vi.getSnapshot();
            if (vi.contentsLost()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            for (int x = 0; x < size; ++x) {
                for (int y = 0; y < size; ++y) {
                    if (bi.getRGB(x, y) != Color.GREEN.getRGB()) {
                        throw new RuntimeException("Test failed.");
                    }
                }
            }
            break;
        }
    }
}
