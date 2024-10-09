



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImagingOpException;
import java.awt.image.Kernel;
import java.util.Arrays;


public class SamePackingTypeTest {

    public static void main(String[] args) {
        BufferedImageOp op = createTestOp();

        try {
            System.out.print("Integer-based images... ");
            doTest(op, TYPE_INT_ARGB, TYPE_INT_ARGB_PRE);
            System.out.println("done.");

            System.out.print("Byte-based images... ");
            doTest(op, TYPE_4BYTE_ABGR, TYPE_4BYTE_ABGR_PRE);
            System.out.println("done");
        } catch (ImagingOpException e) {
            throw new RuntimeException("Test FAILED", e);
        }
    }

    private static void doTest(BufferedImageOp op, int stype, int dtype) {
        final int size = 100;

        final BufferedImage src = new BufferedImage(size, size, stype);
        Graphics2D g = src.createGraphics();
        g.setColor(Color.red);
        g.fillRect(0, 0, size, size);
        g.dispose();


        final BufferedImage dst = new BufferedImage(size, size, dtype);
        g = dst.createGraphics();
        g.setColor(Color.blue);
        g.fillRect(0, 0, size, size);
        g.dispose();

        op.filter(src, dst);

        final int rgb = dst.getRGB(size - 1, size - 1);
        System.out.printf("dst: 0x%X ", rgb);

        if (rgb != 0xFFFF0000) {
            throw new RuntimeException(String.format("Wrong color in dst: 0x%X", rgb));
        }
    }

    private static BufferedImageOp createTestOp() {
        final int size = 1;
        final float v = 1f / (size * size);
        final float[] k_data = new float[size * size];
        Arrays.fill(k_data, v);

        Kernel k = new Kernel(size, size, k_data);
        return new ConvolveOp(k);
    }
}
