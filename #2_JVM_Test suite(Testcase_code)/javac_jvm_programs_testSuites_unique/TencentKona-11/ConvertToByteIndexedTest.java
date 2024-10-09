



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ConvertToByteIndexedTest {
    static final int[] SRC_TYPES = new int[] {
        BufferedImage.TYPE_INT_RGB,
        BufferedImage.TYPE_INT_ARGB,
        BufferedImage.TYPE_INT_ARGB_PRE,
        BufferedImage.TYPE_INT_BGR,
        BufferedImage.TYPE_3BYTE_BGR,
        BufferedImage.TYPE_4BYTE_ABGR,
        BufferedImage.TYPE_4BYTE_ABGR_PRE,
        BufferedImage.TYPE_USHORT_565_RGB,
        BufferedImage.TYPE_USHORT_555_RGB,
        BufferedImage.TYPE_BYTE_INDEXED};

    static final String[] TYPE_NAME = new String[] {
        "INT_RGB",
        "INT_ARGB",
        "INT_ARGB_PRE",
        "INT_BGR",
        "3BYTE_BGR",
        "4BYTE_ABGR",
        "4BYTE_ABGR_PRE",
        "USHORT_565_RGB",
        "USHORT_555_RGB",
        "BYTE_INDEXED"};

    static final Color[] COLORS = new Color[] {
        
        Color.BLACK,
        Color.RED,
        Color.YELLOW,
        Color.GREEN,
        Color.MAGENTA,
        Color.CYAN,
        Color.BLUE};

    static final HashMap<Integer,String> TYPE_TABLE =
            new HashMap<Integer,String>();

    static {
        for (int i = 0; i < SRC_TYPES.length; i++) {
            TYPE_TABLE.put(new Integer(SRC_TYPES[i]), TYPE_NAME[i]);
        }
    }

    static int width = 50;
    static int height = 50;

    public static void ConvertToByteIndexed(Color color, int srcType) {
        
        BufferedImage srcImage = new BufferedImage(width, height, srcType);
        Graphics2D srcG2D = srcImage.createGraphics();
        srcG2D.setColor(color);
        srcG2D.fillRect(0, 0, width, height);

        
        int dstType = BufferedImage.TYPE_BYTE_INDEXED;
        BufferedImage dstImage = new BufferedImage(width, height, dstType);
        Graphics2D dstG2D = (Graphics2D)dstImage.getGraphics();
        
        dstG2D.drawImage(srcImage, 0, 0, null);

        
        BufferedImage argbImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D argbG2D = (Graphics2D)argbImage.getGraphics();
        argbG2D.drawImage(dstImage, 0, 0, null);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (color.getRGB() != argbImage.getRGB(i, j)) {
                    throw new RuntimeException("Conversion from " +
                            TYPE_TABLE.get(srcType) + " to BYTE_INDEXED is not"
                            + " done properly for " + color);
                }
            }
        }
    }

    public static void main(String args[]) {
        for (int srcType : SRC_TYPES) {
            for (Color color : COLORS) {
                ConvertToByteIndexed(color, srcType);
            }
        }
    }
}
