
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class StrokeShapeTest {
  public static void main(String[] args) throws Exception {
    BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setPaint(Color.WHITE);
    g.fill(new Rectangle(image.getWidth(), image.getHeight()));
    g.translate(25, 100);

    Stroke stroke = new BasicStroke(200, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    Shape shape = new Polygon(new int[] {0, 1500, 0}, new int[] {750, 0, -750}, 3);

    g.scale(.1, .1);
    g.setPaint(Color.BLACK);
    g.setStroke(stroke);
    g.draw(shape);
    g.setPaint(Color.RED);
    g.fill(stroke.createStrokedShape(shape));

    
    

    boolean blackPixelFound = false;
    outer:
    for (int x = 0; x < 200; ++x) {
      for (int y = 0; y < 200; ++y) {
        if (image.getRGB(x, y) == Color.BLACK.getRGB()) {
          blackPixelFound = true;
          break outer;
        }
      }
    }
    if (blackPixelFound) {
      throw new RuntimeException("The shape hasn't been filled in red.");
    }
  }
}
