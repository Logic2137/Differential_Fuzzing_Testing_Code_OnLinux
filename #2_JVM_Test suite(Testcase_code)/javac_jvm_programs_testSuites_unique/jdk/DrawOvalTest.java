



import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

public class DrawOvalTest {
    public static void main(String[] args) throws IOException {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(10, 10, Transparency.TRANSLUCENT);

        
        BufferedImage snapshot = null;
        Graphics2D g2 = vi.createGraphics();

        do {
            vi.validate(gc);
            render(g2);
            snapshot = vi.getSnapshot();
        } while (vi.contentsLost());

        
        
        
        int sequence[] = {
            -16776961,
            -16776961,
            -16776961,
            -65536,
            -65536,
            -65536,
            -65536,
            -16776961,
            -16776961,
            -16776961
        };

        
        for (int i = 0; i < snapshot.getWidth(); i++) {

            
            if ( snapshot.getRGB(i, 0) != sequence[i] ||
                 snapshot.getRGB(i, 9) != sequence[i] ||
                 snapshot.getRGB(0, i) != sequence[i] ||
                 snapshot.getRGB(9, i) != sequence[i] ) {
                ImageIO.write(snapshot, "png", new File("DrawOvalTest_snapshot.png"));
                throw new RuntimeException("Test failed.");
            }
        }
    }

    private static void render(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 0, 10, 10);
        g2.setColor(Color.RED);
        g2.drawOval(0, 0, 9, 9);
    }
}
