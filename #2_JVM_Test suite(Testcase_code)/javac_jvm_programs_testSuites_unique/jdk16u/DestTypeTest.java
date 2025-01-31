



import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOReadWarningListener;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

public class DestTypeTest implements IIOWriteWarningListener, IIOReadWarningListener {

    ImageWriter w;
    ImageReader r;

    public static void main(String[] args) throws IOException {
        BufferedImage bi_rgb = createTestImage(BufferedImage.TYPE_INT_RGB);

        DestTypeTest bug = new DestTypeTest();
        byte[] rgb_data = bug.writeTest(bi_rgb);

        System.out.println("rgb jpeg data length is " + rgb_data.length);

        BufferedImage bi_argb = createTestImage(BufferedImage.TYPE_INT_ARGB);

        ImageWriteParam p = bug.getWriteParam();
        IIOMetadata m = bug.getMetadata(p);

        byte[] submeta_data = bug.writeTest(bi_argb, p, m);
        System.out.println("desttype and metadata jpeg data length is " + submeta_data.length);

        p = bug.getWriteParam();
        byte[] subbanded_data = bug.writeTest(bi_argb, p);
        System.out.println("desttype jpeg data length is " + subbanded_data.length);

        if (submeta_data.length > rgb_data.length) {
            throw new  RuntimeException("Too big result jpeg: " + submeta_data.length +
                                        "(rgb image size is " + rgb_data.length + ")");
        }
        if (subbanded_data.length > rgb_data.length) {
            throw new  RuntimeException("Too big result jpeg: " + subbanded_data.length +
                                        "(rgb image size is " + rgb_data.length + ")");
        }
    }

    public DestTypeTest() {
        w = (ImageWriter)
            ImageIO.getImageWritersByFormatName("jpeg").next();
        w.addIIOWriteWarningListener(this);

        r = (ImageReader)
            ImageIO.getImageReadersByFormatName("jpeg").next();
        r.addIIOReadWarningListener(this);
    }

    public ImageWriteParam getWriteParam() {
        ImageWriteParam p =  w.getDefaultWriteParam();
        p.setSourceBands(new int[] {0, 1, 2});
        ImageTypeSpecifier type =
            ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
        p.setDestinationType(type);

        return p;
    }

    public IIOMetadata getMetadata(ImageWriteParam p) {
        return w.getDefaultImageMetadata(p.getDestinationType(), null);
    }

    public byte[] writeTest(BufferedImage bi) throws IOException {
        return writeTest(bi, null);
    }

    public byte[] writeTest(BufferedImage bi,
                          ImageWriteParam p) throws IOException {
        return writeTest(bi, p, null);
    }
    public byte[] writeTest(BufferedImage bi,
                            ImageWriteParam p,
                            IIOMetadata m) throws IOException {
        ByteArrayOutputStream baos =
            new ByteArrayOutputStream();

        
        ImageOutputStream ios =
            ImageIO.createImageOutputStream(baos);
        w.setOutput(ios);
        w.write(null,
                new IIOImage(bi, null, m),
                p);
        ios.close();
        return baos.toByteArray();
    }

    public static BufferedImage createTestImage(int type) {
        int w = 100;
        int h = 500;
        BufferedImage bi = new BufferedImage(3*w, h, type);
        Graphics g = bi.createGraphics();
        g.setColor(Color.red);
        g.fillRect(0,0,w,h);
        g.setColor(Color.green);
        g.fillRect(w, 0,w,h);
        g.setColor(Color.blue);
        g.fillRect(2*w,0,w,h);

        return bi;
    }

    public void warningOccurred(ImageWriter source,
                                int imageIndex,
                                String warning) {
        System.out.println("WRITING WARNING: " + warning);
    }

    public void warningOccurred(ImageReader source,
                                String warning) {
        System.out.println("READING WARNING: " + warning);
    }
}
