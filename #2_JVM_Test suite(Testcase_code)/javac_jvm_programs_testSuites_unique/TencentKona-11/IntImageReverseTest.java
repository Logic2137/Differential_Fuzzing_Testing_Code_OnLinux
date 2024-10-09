



import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;

public class IntImageReverseTest {

    public static void main(String[] args) {
        LookupTable tbl = createReverseTable();
        LookupOp op = new LookupOp(tbl, null);

        for (ImageType t : ImageType.values()) {
            System.out.print(t);

            BufferedImage src = createSourceImage(t);

            BufferedImage dst = op.filter(src, null);

            int rgb = dst.getRGB(0, 0);

            System.out.printf(" Result: 0x%X ", rgb);

            if (rgb != argbReverse) {
                throw new RuntimeException("Test failed.");
            }
            System.out.println("Passed.");
        }
    }

    
    private static LookupTable createReverseTable() {
        byte[] data = new byte[256];

        for (int i = 0; i < 256; i++) {
            data[i] = (byte) (255 - i);
        }


        return new ByteLookupTable(0, data);
    }

    private static BufferedImage createSourceImage(ImageType type) {
        BufferedImage img = new BufferedImage(1, 1, type.bi_type);

        img.setRGB(0, 0, argbTest);

        return img;
    }
    private static final int argbTest = 0xFFDDAA77;
    private static final int argbReverse = 0xFF225588;

    private static enum ImageType {

        INT_ARGB(BufferedImage.TYPE_INT_ARGB),
        INT_ARGB_PRE(BufferedImage.TYPE_INT_ARGB_PRE),
        INT_RGB(BufferedImage.TYPE_INT_BGR),
        INT_BGR(BufferedImage.TYPE_INT_BGR);

        private ImageType(int bi_type) {
            this.bi_type = bi_type;
        }
        public final int bi_type;
    }
}
