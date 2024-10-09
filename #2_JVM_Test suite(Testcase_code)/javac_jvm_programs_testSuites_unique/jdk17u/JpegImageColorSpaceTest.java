import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class JpegImageColorSpaceTest {

    public static void main(String[] args) throws Exception {
        String fileName = "nomarkers.jpg";
        String sep = System.getProperty("file.separator");
        String dir = System.getProperty("test.src", ".");
        String filePath = dir + sep + fileName;
        System.out.println("Test file: " + filePath);
        File imageFile = new File(filePath);
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                if (bufferedImage.getRGB(i, j) != Color.white.getRGB()) {
                    throw new RuntimeException("ColorSpace is not determined " + "properly by ImageIO");
                }
            }
        }
    }
}
