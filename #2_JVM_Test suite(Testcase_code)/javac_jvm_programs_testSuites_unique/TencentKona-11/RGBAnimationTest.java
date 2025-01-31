



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

import com.sun.imageio.plugins.gif.GIFImageMetadata;

public class RGBAnimationTest {
    protected static String format = "GIF";
    protected static boolean doSave = true;

    Frame[] frames;
    ImageWriter writer;
    ImageReader reader;

    public static void main(String[] args) throws IOException  {
        RGBAnimationTest test = new RGBAnimationTest();
        test.doTest();
    }
    
    public RGBAnimationTest() {
        frames = new Frame[4];


        frames[0] = new Frame(new Color[] {Color.red, Color.green});
        frames[1] = new Frame(new Color[] {Color.green, Color.cyan});
        frames[2] = new Frame(new Color[] {Color.cyan, Color.yellow});
        frames[3] = new Frame(new Color[] {Color.yellow, Color.red});

        writer = ImageIO.getImageWritersByFormatName(format).next();
        reader = ImageIO.getImageReadersByFormatName(format).next();
    }

    public void doTest() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.reset();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);

        ImageWriteParam wparam = prepareWriteParam();

        IIOMetadata streamMetadata = prepareStreamMetadata(wparam);

        writer.prepareWriteSequence(streamMetadata);

        for (int i = 0; i < frames.length; i++) {
            BufferedImage src = frames[i].getImage();
            IIOMetadata imageMetadata = prepareImageMetadata(i, src, wparam);
            IIOImage img = new IIOImage(src,  null, imageMetadata);

            writer.writeToSequence(img, wparam);
        }
        writer.endWriteSequence();
        ios.flush();
        ios.close();

        if (doSave) {
            File f = File.createTempFile("wr_test_", "." + format, new File("."));
            System.out.println("Save to file: " + f.getCanonicalPath());
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        }
        
        reader.reset();
        ByteArrayInputStream bais =
                new ByteArrayInputStream(baos.toByteArray());
        reader.setInput(ImageIO.createImageInputStream(bais));

        int minIndex = reader.getMinIndex();
        int numImages = reader.getNumImages(true);

        for (int i = 0; i < numImages; i++) {
            BufferedImage dst = reader.read(i + minIndex);
            frames[i].checkResult(dst);
        }
    }

    protected IIOMetadata prepareImageMetadata(int i, BufferedImage img, ImageWriteParam wparam) {
        GIFImageMetadata idata = (GIFImageMetadata)
        writer.getDefaultImageMetadata(ImageTypeSpecifier.createFromRenderedImage(img), wparam);

        idata.delayTime = 100;
        idata.disposalMethod = 0;
        idata.transparentColorFlag = false;

        if (i == 0) {
            ArrayList<byte[]> appIDs = new ArrayList<byte[]>();
            appIDs.add(new String("NETSCAPE").getBytes());
            ArrayList<byte[]> authCodes = new ArrayList<byte[]>();
            authCodes.add(new String("2.0").getBytes());
            ArrayList<byte[]> appData = new ArrayList<byte[]>();
            byte[] authData = {1, 0, 0};
            appData.add(authData);

            idata.applicationIDs = appIDs;
            idata.authenticationCodes = authCodes;
            idata.applicationData = appData;
        }
        return idata;
    }

    protected ImageWriteParam prepareWriteParam() {
        return writer.getDefaultWriteParam();
    }

    protected IIOMetadata prepareStreamMetadata(ImageWriteParam wparam) {
        return writer.getDefaultStreamMetadata(wparam);
    }

}

class Frame {
    protected static int type = BufferedImage.TYPE_INT_RGB;
    protected static int dx = 100;
    protected static int h = 100;

    protected Color[] colors;
    protected BufferedImage img;

    public Frame(Color[] colors) {
        this.colors = colors;
        img = null;
    }

    public BufferedImage getImage() {
        if (img == null) {
            img = new BufferedImage(dx * colors.length, h, type);
            Graphics2D g = img.createGraphics();
            for (int i = 0; i < colors.length; i++) {
                g.setColor(colors[i]);
                g.fillRect(dx * i, 0, dx, h);
            }
        }
        return img;
    }

    public void checkResult(BufferedImage dst) {
        int y = h / 2;
        int x = dx / 2;
        for (int i = 0; i < colors.length; i++) {

            int srcRgb = img.getRGB(i * dx + x, y);
            int dstRgb = dst.getRGB(i * dx + x, y);

            if (srcRgb != dstRgb) {
                throw new RuntimeException("Test failed due to color difference: " +
                        Integer.toHexString(dstRgb) + " instead of " +
                        Integer.toHexString(srcRgb));
            }
        }
    }
}
