import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class BitDepth {

    public static void main(String[] args) throws IOException {
        new BitDepth(args);
    }

    private static boolean testPNGByteBinary() throws IOException {
        int width = 10;
        int height = 10;
        File f = new File("BlackStripe.png");
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = bi.createGraphics();
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, width, height);
        ImageIO.write(bi, "png", f);
        BufferedImage bi2 = ImageIO.read(f);
        if (bi2.getWidth() != width || bi2.getHeight() != height) {
            System.out.println("Dimensions changed!");
            return false;
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = bi2.getRGB(x, y);
                if (rgb != 0xffffffff) {
                    System.out.println("Found a non-white pixel!");
                    return false;
                }
            }
        }
        f.delete();
        return true;
    }

    private static final int[] biRGBTypes = { BufferedImage.TYPE_INT_RGB, BufferedImage.TYPE_INT_BGR, BufferedImage.TYPE_3BYTE_BGR, BufferedImage.TYPE_USHORT_565_RGB, BufferedImage.TYPE_USHORT_555_RGB, BufferedImage.TYPE_INT_ARGB, BufferedImage.TYPE_INT_ARGB_PRE, BufferedImage.TYPE_4BYTE_ABGR, BufferedImage.TYPE_4BYTE_ABGR_PRE };

    private static final String[] biTypeNames = { "CUSTOM", "INT_RGB", "INT_ARGB", "INT_ARGB_PRE", "INT_BGR", "3BYTE_BGR", "4BYTE_ABGR", "4BYTE_ABGR_PRE", "USHORT_565_RGB", "USHORT_555_RGB", "BYTE_GRAY", "USHORT_GRAY", "BYTE_BINARY", "BYTE_INDEXED" };

    private int width = 80;

    private int height = 80;

    private String[] formats = { "png", "jpeg", "tiff", "bmp", "gif" };

    public BitDepth(String[] args) throws IOException {
        if (args.length > 0) {
            formats = args;
        }
        for (String format : formats) {
            testFormat(format);
        }
    }

    private void testFormat(String format) throws IOException {
        boolean allOK = true;
        for (int type : biRGBTypes) {
            if (format.toLowerCase().equals("bmp") && ((type == BufferedImage.TYPE_INT_ARGB) || (type == BufferedImage.TYPE_INT_ARGB_PRE) || (type == BufferedImage.TYPE_4BYTE_ABGR) || (type == BufferedImage.TYPE_4BYTE_ABGR_PRE))) {
                System.err.println("cannot use " + biTypeNames[type] + " for bmp because of JDK-8147448.\t" + " please update the test after fix of this bug!");
                continue;
            }
            System.out.println("Testing " + format + " writer for type " + biTypeNames[type]);
            File f = testWriteRGB(format, type);
            if (f == null)
                continue;
            boolean ok = testReadRGB(f);
            if (ok) {
                f.delete();
            }
            allOK = allOK && ok;
        }
        if (format.equals("png")) {
            System.out.println("Testing png writer for black stripe");
            boolean ok = testPNGByteBinary();
            allOK = allOK && ok;
        }
        if (!allOK) {
            throw new RuntimeException("Test failed");
        }
    }

    private File testWriteRGB(String format, int type) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, type);
        Graphics2D g = bi.createGraphics();
        Color white = new Color(255, 255, 255);
        Color red = new Color(255, 0, 0);
        Color green = new Color(0, 255, 0);
        Color blue = new Color(0, 0, 255);
        g.setColor(white);
        g.fillRect(0, 0, width, height);
        g.setColor(red);
        g.fillRect(10, 10, 20, 20);
        g.setColor(green);
        g.fillRect(30, 30, 20, 20);
        g.setColor(blue);
        g.fillRect(50, 50, 20, 20);
        ImageTypeSpecifier spec = new ImageTypeSpecifier(bi);
        Iterator<ImageWriter> writers = ImageIO.getImageWriters(spec, format);
        File file = new File("BitDepth_" + biTypeNames[type] + "." + format);
        if (!writers.hasNext()) {
            System.out.println("\tNo writers available for type " + biTypeNames[type] + " BufferedImage!");
            return null;
        } else {
            ImageWriter writer = writers.next();
            try (ImageOutputStream out = ImageIO.createImageOutputStream(file)) {
                writer.setOutput(out);
                writer.write(bi);
            } catch (Exception e) {
                System.out.println("\tCan't write a type " + biTypeNames[type] + " BufferedImage!");
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    private int colorDistance(int color, int r, int g, int b) {
        int r0 = ((color >> 16) & 0xff) - r;
        int g0 = ((color >> 8) & 0xff) - g;
        int b0 = (color & 0xff) - b;
        return r0 * r0 + g0 * g0 + b0 * b0;
    }

    private boolean testReadRGB(File file) throws IOException {
        int[] rgb = new int[3];
        BufferedImage bi = ImageIO.read(file);
        if (bi == null) {
            System.out.println("Couldn't read image!");
            return false;
        }
        int r = bi.getRGB(15, 15);
        if (colorDistance(r, 255, 0, 0) > 20) {
            System.out.println("Red was distorted!");
            return false;
        }
        int g = bi.getRGB(35, 35);
        if (colorDistance(g, 0, 255, 0) > 20) {
            System.out.println("Green was distorted!");
            return false;
        }
        int b = bi.getRGB(55, 55);
        if (colorDistance(b, 0, 0, 255) > 20) {
            System.out.println("Blue was distorted!");
            return false;
        }
        int w = bi.getRGB(55, 15);
        if (colorDistance(w, 255, 255, 255) > 20) {
            System.out.println("White was distorted!");
            return false;
        }
        return true;
    }
}
