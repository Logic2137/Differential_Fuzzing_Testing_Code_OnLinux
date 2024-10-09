import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

public class GenerateTestImage {

    private static final int IMAGE_SIZE = 20;

    public static void main(String[] args) throws Exception {
        File file = new File("test.png");
        if (file.exists()) {
            return;
        }
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(Color.red);
        graphics2D.fillOval(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        graphics2D.dispose();
        ;
        ImageIO.write(image, "png", file);
    }
}
