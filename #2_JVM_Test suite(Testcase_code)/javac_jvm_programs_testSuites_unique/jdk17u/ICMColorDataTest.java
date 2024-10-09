import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

public class ICMColorDataTest {

    private static final int WIDTH = 90;

    private static final int HEIGHT = 90;

    private static final int BITS_PER_PIXEL = 1;

    private static final int PIXELS_IN_BYTE = 8;

    private static final byte[] RED = { (byte) 255, 0 };

    private static final byte[] GREEN = { (byte) 255, 0 };

    private static final byte[] BLUE = { (byte) 255, 0 };

    public static void main(String[] args) {
        try {
            for (long i = 0; i < 300_000; i++) {
                makeImage();
            }
        } catch (OutOfMemoryError | NullPointerException e) {
            System.err.println("Caught expected exception:\n" + e.getClass() + ": " + e.getMessage());
        }
        System.err.println("Test passed");
    }

    private static void makeImage() {
        int scanLineBytes = WIDTH / PIXELS_IN_BYTE;
        if ((WIDTH & (PIXELS_IN_BYTE - 1)) != 0) {
            scanLineBytes += 1;
        }
        byte[] bits = new byte[scanLineBytes * HEIGHT];
        DataBuffer dataBuf = new DataBufferByte(bits, bits.length, 0);
        SampleModel sampleModel = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE, WIDTH, HEIGHT, BITS_PER_PIXEL);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuf, null);
        IndexColorModel indexModel = new IndexColorModel(2, 2, RED, GREEN, BLUE);
        BufferedImage bufImage = new BufferedImage(indexModel, raster, indexModel.isAlphaPremultiplied(), null);
        Graphics g = bufImage.getGraphics();
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        g.dispose();
    }
}
