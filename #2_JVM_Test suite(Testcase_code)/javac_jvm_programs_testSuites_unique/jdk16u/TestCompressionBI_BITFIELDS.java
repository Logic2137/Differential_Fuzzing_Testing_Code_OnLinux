



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

public class TestCompressionBI_BITFIELDS {

    protected String format = "BMP";

    protected ImageReader reader;

    protected ImageWriter writer;

    protected boolean doSave = true;

    Color[] colors = {
        Color.red, Color.green, Color.blue,
        Color.yellow, Color.white, Color.black};

    int dx = 50;
    int h = 200;

    public TestCompressionBI_BITFIELDS() {
        this("BMP");
    }

    public TestCompressionBI_BITFIELDS(String format) {
        this.format = format;
        reader = ImageIO.getImageReadersByFormatName(format).next();
        writer = ImageIO.getImageWritersByFormatName(format).next();
    }

    protected ImageWriteParam prepareWriteParam(BufferedImage src) {
        ImageWriteParam wparam = writer.getDefaultWriteParam();
        wparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        wparam.setCompressionType("BI_BITFIELDS");

        return wparam;
    }

    public BufferedImage writeAndRead(BufferedImage src) throws IOException {
        writer.reset();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.setOutput(ImageIO.createImageOutputStream(baos));

        ImageWriteParam wparam = prepareWriteParam(src);

        IIOImage img = new IIOImage(src, null, null);

        writer.write(null, img, wparam);

        if (doSave) {
            
            
            File f = File.createTempFile("wr_test_", "." + format, new File("."));
            System.out.println("Save to file: " + f.getCanonicalPath());
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        }

        
        reader.reset();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        reader.setInput(ImageIO.createImageInputStream(bais));

        return reader.read(0);
    }

    public static void main(String[] args) throws IOException {
        
        int[] types = {BufferedImage.TYPE_3BYTE_BGR,
                       BufferedImage.TYPE_USHORT_555_RGB,
                       BufferedImage.TYPE_USHORT_565_RGB,
                       BufferedImage.TYPE_BYTE_GRAY,
                       BufferedImage.TYPE_BYTE_BINARY,
                       BufferedImage.TYPE_BYTE_INDEXED,
                       BufferedImage.TYPE_INT_BGR,
                       BufferedImage.TYPE_INT_RGB};

        for (int i = 0; i < types.length; i++) {
            System.out.println("Test image " + types[i]);
            TestCompressionBI_BITFIELDS t = new TestCompressionBI_BITFIELDS();

            BufferedImage src =
                t.createTestImage(types[i]);
            System.out.println("Image for test: " + src);
            System.out.println("SampleModel: " + src.getSampleModel());

            BufferedImage dst = null;
            try {
                dst = t.writeAndRead(src);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }


            t.compareImages(src, dst);
        }
    }

    protected BufferedImage createTestImage(int type) {
        BufferedImage bimg = new BufferedImage(dx * colors.length, h, type);
        Graphics2D g = bimg.createGraphics();

        for (int i = 0; i < colors.length; i++) {
            g.setColor(colors[i]);
            g.fillRect(dx * i, 0, dx, h);
        }
        return bimg;
    }

    protected void compareImages(BufferedImage src, BufferedImage dst) {
        ColorSpace srcCS = src.getColorModel().getColorSpace();
        ColorSpace dstCS = dst.getColorModel().getColorSpace();
        if (!srcCS.equals(dstCS) && srcCS.getType() == ColorSpace.TYPE_GRAY) {
            System.out.println("Workaround color difference with GRAY.");
            BufferedImage tmp  =
                new BufferedImage(src.getWidth(), src.getHeight(),
                                  BufferedImage.TYPE_INT_RGB);
            Graphics g = tmp.createGraphics();
            g.drawImage(src, 0, 0, null);
            src = tmp;
        }
        int y = h / 2;
        for (int i = 0; i < colors.length; i++) {
            int x = dx * i + dx / 2;
            int srcRgb = src.getRGB(x, y);
            int dstRgb = dst.getRGB(x, y);

            if (srcRgb != dstRgb) {
                throw new RuntimeException("Test failed due to color difference: " +
                                           "src_pixel=" + Integer.toHexString(srcRgb) +
                                           "dst_pixel=" + Integer.toHexString(dstRgb));
            }
        }
    }
}
