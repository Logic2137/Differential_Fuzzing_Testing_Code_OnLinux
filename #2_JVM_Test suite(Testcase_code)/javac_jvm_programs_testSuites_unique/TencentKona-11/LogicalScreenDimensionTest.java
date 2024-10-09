



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.sun.imageio.plugins.gif.GIFStreamMetadata;

public class LogicalScreenDimensionTest {
    public static void main(String[] args) throws IOException {
        String format = "GIF";
        ImageWriter writer =
                ImageIO.getImageWritersByFormatName(format).next();
        if (writer == null) {
            throw new RuntimeException("No available writers for " + format);
        }

        BufferedImage img = createTestImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);

        ImageWriteParam p = writer.getDefaultWriteParam();
        ImageTypeSpecifier type =
            ImageTypeSpecifier.createFromRenderedImage(img);
        IIOMetadata inImageMetadata =
            writer.getDefaultImageMetadata(type, p);

        IIOMetadata inStreamMetadata = writer.getDefaultStreamMetadata(p);

        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);

        writer.write(inStreamMetadata, new IIOImage(img, null, inImageMetadata), p);

        ios.flush();
        ios.close();

        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ImageInputStream iis = ImageIO.createImageInputStream(bais);
        ImageReader reader = ImageIO.getImageReader(writer);
        reader.setInput(iis);

        IIOMetadata outStreamMetadata = reader.getStreamMetadata();

        GIFStreamMetadata gifStreamMetadata = (GIFStreamMetadata)outStreamMetadata;

        if (gifStreamMetadata.logicalScreenWidth != img.getWidth() ||
                gifStreamMetadata.logicalScreenHeight != img.getHeight()) {
            throw new RuntimeException("Test failed due to wrong logical screen dimension.");
        }
    }

    private static BufferedImage createTestImage(int w, int h, int type) {
        BufferedImage res = new BufferedImage(w, h, type);
        Graphics2D g = res.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);
        g.fillRect(w/4, h/4, w/2, h/2);


        return res;
    }
}
