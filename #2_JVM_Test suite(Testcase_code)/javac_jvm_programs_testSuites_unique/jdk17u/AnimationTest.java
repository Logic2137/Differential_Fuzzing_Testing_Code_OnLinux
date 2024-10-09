import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

public class AnimationTest {

    BufferedImage img = null;

    int x, y;

    int w, h;

    protected static String fname = "animtest.gif";

    public AnimationTest() {
        w = h = 100;
    }

    private void initFrame() {
        if (img != null) {
            return;
        }
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];
        for (int i = 0; i < 256; i++) {
            r[i] = g[i] = b[i] = (byte) 0x00;
        }
        r[0] = (byte) 0x00;
        g[0] = (byte) 0x00;
        b[0] = (byte) 0x00;
        r[1] = (byte) 0xFF;
        g[1] = (byte) 0xFF;
        b[1] = (byte) 0xFF;
        r[2] = (byte) 0xFF;
        g[3] = (byte) 0xFF;
        b[4] = (byte) 0xFF;
        IndexColorModel icm = new IndexColorModel(8, 256, r, g, b);
        img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_INDEXED, icm);
    }

    private BufferedImage createNextFrame() {
        Graphics g = img.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.red);
        g.drawLine(x, 0, x, h);
        g.setColor(Color.blue);
        g.drawLine(0, y, w, y);
        x += 2;
        y += 2;
        x %= w;
        y %= h;
        return img;
    }

    ImageWriter writer = null;

    private ImageWriter initWriter() throws IOException {
        ImageOutputStream ios = ImageIO.createImageOutputStream(new File(fname));
        writer = ImageIO.getImageWritersByFormatName("GIF").next();
        writer.setOutput(ios);
        return writer;
    }

    public static void main(String[] args) {
        try {
            AnimationTest t = new AnimationTest();
            t.initFrame();
            ImageWriter w = t.initWriter();
            ImageWriteParam p = w.getDefaultWriteParam();
            IIOMetadata streamMetadata = w.getDefaultStreamMetadata(p);
            w.prepareWriteSequence(streamMetadata);
            for (int i = 0; i < 50; i++) {
                BufferedImage f = t.createNextFrame();
                ImageTypeSpecifier type = new ImageTypeSpecifier(f);
                IIOMetadata m = w.getDefaultImageMetadata(type, p);
                w.writeToSequence(new IIOImage(f, null, m), p);
            }
            w.endWriteSequence();
            t.checkAnimation();
        } catch (Exception e) {
            throw new RuntimeException("Test failed.", e);
        }
    }

    protected void checkAnimation() throws IOException {
        ImageReader r = ImageIO.getImageReadersByFormatName("GIF").next();
        r.setInput(ImageIO.createImageInputStream(new File(fname)));
        int n = r.getNumImages(true);
        for (int i = 0; i < n; i++) {
            BufferedImage f = r.read(i);
            checkFrame(i, f);
        }
        System.out.println("Test passed.");
    }

    protected void checkFrame(int i, BufferedImage f) {
        int x = 2 * i + 1;
        for (int y = 0; y < h; y++) {
            int argb = f.getRGB(x, y);
            if (argb != 0xffffffff && !(argb == 0xff0000ff && y == 2 * i)) {
                throw new RuntimeException("Test failed - bad frame");
            }
            argb = f.getRGB(y, x);
            if (argb != 0xffffffff && !(argb == 0xffff0000 && y == 2 * i)) {
                throw new RuntimeException("Test failed - bad frame");
            }
        }
    }
}
