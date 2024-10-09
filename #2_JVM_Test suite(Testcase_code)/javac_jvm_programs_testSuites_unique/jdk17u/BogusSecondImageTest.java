import java.awt.Image;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.stream.*;

public class BogusSecondImageTest {

    public static void main(String[] args) throws Throwable {
        int failures = 0;
        try {
            testZeroEntryIFD();
        } catch (Exception e) {
            System.out.printf("Failed testZeroEntryIFD: %s%n", e);
            failures++;
        }
        try {
            testOutOfStreamIFD();
        } catch (Exception e) {
            System.out.printf("Failed testOutOfStreamIFD: %s%n", e);
            failures++;
        }
        if (failures == 0) {
            System.out.println("Test succeeded");
        } else {
            throw new RuntimeException("Test failed with " + failures + " errors");
        }
    }

    private static void testZeroEntryIFD() throws Exception {
        File f = createImageFile();
        ImageOutputStream s = new FileImageOutputStream(f);
        long length = s.length();
        s.skipBytes(4);
        long ifd0 = s.readUnsignedInt();
        s.seek(ifd0);
        int entries0 = s.readUnsignedShort();
        s.skipBytes(12 * entries0);
        s.write((int) length);
        s.seek(length);
        s.writeShort(0);
        s.close();
        try {
            Load(f);
        } catch (Exception e) {
            throw e;
        } finally {
            f.delete();
        }
    }

    private static void testOutOfStreamIFD() throws Exception {
        File f = createImageFile();
        ImageOutputStream s = new FileImageOutputStream(f);
        long length = s.length();
        s.skipBytes(4);
        long ifd0 = s.readUnsignedInt();
        s.seek(ifd0);
        int entries0 = s.readUnsignedShort();
        s.skipBytes(12 * entries0);
        s.write((int) length + 7);
        s.close();
        try {
            Load(f);
        } catch (Exception e) {
            throw e;
        } finally {
            f.delete();
        }
    }

    private static File createImageFile() throws Exception {
        BufferedImage im = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
        File f = File.createTempFile("BogusSecondImage", "tif", new File("."));
        f.deleteOnExit();
        if (!ImageIO.write(im, "TIFF", f)) {
            throw new RuntimeException("Failed to write " + f);
        }
        return f;
    }

    private final static boolean printTrace = false;

    public static void Load(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(file + " does not exist");
        } else if (!file.isFile()) {
            throw new IllegalArgumentException(file + " is not a regular file");
        } else if (!file.canRead()) {
            throw new IllegalArgumentException(file + " cannot be read");
        }
        ImageInputStream input = null;
        try {
            input = ImageIO.createImageInputStream(file);
        } catch (Throwable e) {
            System.err.println("NOK: createImageInputStream()\t" + e.getMessage());
            if (printTrace) {
                e.printStackTrace();
            }
            return;
        }
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("TIFF");
        if (!readers.hasNext()) {
            throw new RuntimeException("No readers available for TIFF");
        }
        ImageReader reader = readers.next();
        reader.setInput(input);
        Image[] images = null;
        int numImages = 0;
        int failures = 0;
        try {
            numImages = reader.getNumImages(true);
            images = new Image[numImages];
        } catch (Throwable e) {
            failures++;
            System.err.println("NOK: getNumImages()\t" + e.getMessage());
            if (printTrace) {
                e.printStackTrace();
            }
        }
        System.out.printf("numImages %d%n", numImages);
        for (int i = 0; i < numImages; i++) {
            System.out.printf("reading image %d%n", i);
            try {
                images[i] = reader.read(i);
            } catch (Throwable e) {
                failures++;
                System.err.println("NOK: read()\t" + e.getMessage());
                if (printTrace) {
                    e.printStackTrace();
                }
            }
        }
        if (failures == 0) {
            System.err.println("OK");
        } else {
            throw new RuntimeException("NOK");
        }
    }
}
