import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ThinLineTest {

    private static final int PIXEL = 381;

    public static void main(String[] args) throws Exception {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(Color.WHITE);
        g.fill(new Rectangle(image.getWidth(), image.getHeight()));
        g.scale(0.5 / PIXEL, 0.5 / PIXEL);
        g.setPaint(Color.BLACK);
        g.setStroke(new BasicStroke(PIXEL));
        g.draw(new Ellipse2D.Double(PIXEL * 50, PIXEL * 50, PIXEL * 300, PIXEL * 300));
        boolean nonWhitePixelFound = false;
        for (int x = 0; x < 200; ++x) {
            if (image.getRGB(x, 100) != Color.WHITE.getRGB()) {
                nonWhitePixelFound = true;
                break;
            }
        }
        if (!nonWhitePixelFound) {
            throw new RuntimeException("The thin line disappeared.");
        }
    }
}
