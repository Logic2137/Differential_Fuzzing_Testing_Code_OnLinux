import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

public class AlphaCompositeTest {

    public static void main(String[] args) throws IOException {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(100, 100, Transparency.TRANSLUCENT);
        BufferedImage gold = gc.createCompatibleImage(100, 100, Transparency.TRANSLUCENT);
        render(gold.createGraphics());
        BufferedImage snapshot = null;
        Graphics2D g2 = vi.createGraphics();
        do {
            render(g2);
            snapshot = vi.getSnapshot();
        } while (vi.contentsLost());
        for (int x = 0; x < gold.getWidth(); ++x) {
            for (int y = 0; y < gold.getHeight(); ++y) {
                if (gold.getRGB(x, y) != snapshot.getRGB(x, y)) {
                    ImageIO.write(gold, "png", new File("gold.png"));
                    ImageIO.write(snapshot, "png", new File("bi.png"));
                    throw new RuntimeException("Test failed.");
                }
            }
        }
    }

    private static void render(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 0, 100, 100);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));
        g2.setColor(Color.RED);
        g2.fillRect(10, 10, 80, 80);
    }
}
