



import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class UshortGrayTest {

    public static void main(String[] args) {
        Iterator iter;
        BufferedImage bi = new BufferedImage(10, 10,
                                             BufferedImage.TYPE_USHORT_GRAY);

        
        
        ImageWriter writer = null;
        iter = ImageIO.getImageWritersByFormatName("jpeg");
        if (iter.hasNext()) {
            writer = (ImageWriter)iter.next();
        } else {
            throw new RuntimeException("No JPEG reader found");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = null;
        boolean exceptionThrown = false;

        try {
            ios = ImageIO.createImageOutputStream(baos);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not create ImageOutputStream");
        }

        try {
            writer.setOutput(ios);
            writer.write(bi);
        } catch (IOException ioe) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            throw new RuntimeException("JPEG writer should not be able to " +
                                       "write USHORT_GRAY images");
        }

        
        
        ImageTypeSpecifier its =
            ImageTypeSpecifier.createFromRenderedImage(bi);

        iter = ImageIO.getImageWriters(its, "jpeg");
        if (iter.hasNext()) {
            throw new RuntimeException("JPEG writer should not be available" +
                                       " for USHORT_GRAY images");
        }
    }
}
