import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;
import static java.awt.image.BufferedImage.TYPE_BYTE_BINARY;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static java.awt.image.BufferedImage.TYPE_BYTE_INDEXED;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;
import static java.awt.image.BufferedImage.TYPE_INT_BGR;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.awt.image.BufferedImage.TYPE_USHORT_555_RGB;
import static java.awt.image.BufferedImage.TYPE_USHORT_565_RGB;
import static java.awt.image.BufferedImage.TYPE_USHORT_GRAY;

public final class ColCvtAlphaDifferentSrcDst {

    private static final int WIDTH = 256;

    private static final int HEIGHT = 256;

    public static void main(String[] args) throws Exception {
        differentToOpaqueDst();
        differentToTransparentDst(TYPE_INT_ARGB);
        differentToTransparentDst(TYPE_4BYTE_ABGR);
        differentToTransparentDst(TYPE_INT_ARGB_PRE);
    }

    private static void differentToOpaqueDst() {
        opaqueDst(TYPE_INT_ARGB, TYPE_INT_RGB);
        opaqueDst(TYPE_INT_ARGB, TYPE_INT_BGR);
        opaqueDst(TYPE_4BYTE_ABGR, TYPE_INT_BGR);
    }

    private static void differentToTransparentDst(int typeDst) {
        transparentDst(TYPE_INT_RGB, typeDst);
        transparentDst(TYPE_INT_ARGB, typeDst);
        transparentDst(TYPE_INT_ARGB_PRE, typeDst);
        transparentDst(TYPE_INT_BGR, typeDst);
        transparentDst(TYPE_3BYTE_BGR, typeDst);
        transparentDst(TYPE_4BYTE_ABGR, typeDst);
        transparentDst(TYPE_4BYTE_ABGR_PRE, typeDst);
        transparentDst(TYPE_USHORT_565_RGB, typeDst);
        transparentDst(TYPE_USHORT_555_RGB, typeDst);
        transparentDst(TYPE_BYTE_GRAY, typeDst);
        transparentDst(TYPE_USHORT_GRAY, typeDst);
        transparentDst(TYPE_BYTE_BINARY, typeDst);
        transparentDst(TYPE_BYTE_INDEXED, typeDst);
    }

    private static void opaqueDst(int transparent, int opaque) {
        ColorSpace to = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
        ColorSpace from = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorConvertOp op = new ColorConvertOp(from, to, null);
        BufferedImage timgSrc = createSrc(transparent);
        BufferedImage oimgSrc = createSrc(opaque);
        BufferedImage timgDst = createDst(TYPE_INT_RGB);
        BufferedImage oimgDst = createDst(TYPE_INT_RGB);
        op.filter(timgSrc, timgDst);
        op.filter(oimgSrc, oimgDst);
        validate(timgDst, oimgDst, false);
    }

    private static void transparentDst(int typeSrc, int typeDst) {
        ColorSpace to = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);
        ColorSpace from = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorConvertOp op = new ColorConvertOp(from, to, null);
        BufferedImage src = createSrc(typeSrc);
        BufferedImage dst = createDst(typeDst);
        op.filter(src, dst);
        validate(src, dst, true);
    }

    private static void validate(BufferedImage img1, BufferedImage img2, boolean alphaOnly) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                int rgb1 = img1.getRGB(i, j);
                int rgb2 = img2.getRGB(i, j);
                if (alphaOnly) {
                    rgb1 |= 0x00FFFFFF;
                    rgb2 |= 0x00FFFFFF;
                }
                if (rgb1 != rgb2) {
                    System.out.println("rgb1 = " + Integer.toHexString(rgb1));
                    System.out.println("rgb2 = " + Integer.toHexString(rgb2));
                    throw new RuntimeException();
                }
            }
        }
    }

    private static BufferedImage createSrc(int type) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, type);
        fill(img);
        return img;
    }

    private static BufferedImage createDst(int type) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, type);
        Graphics2D g = img.createGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.dispose();
        return img;
    }

    private static void fill(BufferedImage image) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                image.setRGB(i, j, (i << 24) | (i << 16) | (j << 8) | ((i + j) >> 1));
            }
        }
    }
}
