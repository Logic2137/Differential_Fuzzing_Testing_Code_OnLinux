



import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ShortPaletteTest {

    public static void main(String[] args) {

        for (int numberColors = 2; numberColors <= 16; numberColors++) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedImage image = createImage(numberColors);
                ImageIO.write(image, "png", baos);
                baos.close();
                System.out.println("Number of colors: " + numberColors);
                byte[] buffer = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                ImageIO.read(bais);
                System.out.println("OK");
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                throw new RuntimeException("Test failed.");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Unexpected exception was thrown."
                                           + " Test failed.");
            }
        }
    }

    private static IndexColorModel createColorModel(int numberColors) {

        byte[] colors = new byte[numberColors*3];
        int depth = 4;
        int startIndex = 0;

        return new IndexColorModel(depth,
                                   numberColors,
                                   colors,
                                   startIndex,
                                   false);
    }

    private static BufferedImage createImage(int numberColors) {
        return new BufferedImage(32,
                                 32,
                                 BufferedImage.TYPE_BYTE_BINARY,
                                 createColorModel(numberColors));
    }
}
