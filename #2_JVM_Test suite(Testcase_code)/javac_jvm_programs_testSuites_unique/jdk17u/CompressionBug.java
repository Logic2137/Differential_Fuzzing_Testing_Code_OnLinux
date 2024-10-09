import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class CompressionBug {

    public CompressionBug() throws IOException {
        File fileHighComp = File.createTempFile("CompressionHigh", ".jpg");
        File fileLowComp = File.createTempFile("CompressionLow", ".jpg");
        fileHighComp.deleteOnExit();
        fileLowComp.deleteOnExit();
        ImageOutputStream iosHighComp = ImageIO.createImageOutputStream(fileHighComp);
        ImageOutputStream iosLowComp = ImageIO.createImageOutputStream(fileLowComp);
        int width = 100;
        int height = 100;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            int w = r.nextInt(width - x);
            int h = r.nextInt(height - y);
            g.setColor(c);
            g.fillRect(x, y, w, h);
        }
        ImageTypeSpecifier typeSpecifier = new ImageTypeSpecifier(bi.getColorModel(), bi.getSampleModel());
        ImageWriter writer = null;
        Iterator iter = ImageIO.getImageWriters(typeSpecifier, "jpeg");
        while (iter.hasNext()) {
            writer = (ImageWriter) iter.next();
            break;
        }
        IIOImage iioImg = new IIOImage(bi, null, null);
        ImageWriteParam wParam = writer.getDefaultWriteParam();
        wParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        wParam.setCompressionQuality(0.1f);
        writer.setOutput(iosHighComp);
        writer.write(null, iioImg, wParam);
        wParam.setCompressionQuality(0.9f);
        writer.setOutput(iosLowComp);
        writer.write(null, iioImg, wParam);
        long sizeOfFileLowComp = fileLowComp.length();
        long sizeOfFileHighComp = fileHighComp.length();
        if (sizeOfFileLowComp < sizeOfFileHighComp) {
            throw new RuntimeException("Lower compression quality did not " + "reduce file size!");
        }
    }

    public static void main(String[] args) throws IOException {
        new CompressionBug();
    }
}
