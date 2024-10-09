



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class PngMultipleImageReadTest {

    private static final ImageReader PNG_READER =
            ImageIO.getImageReadersByMIMEType("image/png").next();

    public static void main(String[] args) throws IOException {

        
        BufferedImage imageWithoutPalette =
                new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = imageWithoutPalette.createGraphics();
        g1.setColor(Color.WHITE);
        g1.fillRect(0, 0, 20, 20);
        g1.dispose();
        
        writeAndReadImage(imageWithoutPalette);

        
        IndexColorModel cm = new IndexColorModel(
                3,
                1,
                new byte[]{10}, 
                new byte[]{10}, 
                new byte[]{10}); 
        BufferedImage imageWithPalette = new BufferedImage(
                10, 10,
                BufferedImage.TYPE_BYTE_INDEXED,
                cm);
        Graphics2D g2 = imageWithPalette.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 10, 10);
        g2.dispose();
        
        writeAndReadImage(imageWithPalette);
    }

    private static void writeAndReadImage(BufferedImage image)
            throws IOException {
        File output = File.createTempFile("output", ".png");
        ImageInputStream stream = null;
        try {
            ImageIO.write(image, "png", output);

            stream = ImageIO.createImageInputStream(output);
            ImageReadParam param = PNG_READER.getDefaultReadParam();
            PNG_READER.setInput(stream, true, true);
            PNG_READER.read(0, param);
        } finally {
            if (stream != null) {
                stream.close();
            }
            Files.delete(output.toPath());
        }
    }
}

