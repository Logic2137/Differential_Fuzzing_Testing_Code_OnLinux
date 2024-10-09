

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;


public final class MTPerLineTransformValidation {

    private volatile static BufferedImage[] lines;

    public static final int SIZE = 255;
    private static volatile boolean failed = false;

    private static final int[] spaces = {
            ColorSpace.CS_CIEXYZ, ColorSpace.CS_GRAY, ColorSpace.CS_LINEAR_RGB,
            ColorSpace.CS_PYCC, ColorSpace.CS_sRGB
    };

    private static final int[] types = new int[]{
            BufferedImage.TYPE_INT_RGB, BufferedImage.TYPE_INT_ARGB,
            BufferedImage.TYPE_INT_ARGB_PRE, BufferedImage.TYPE_INT_BGR,
            BufferedImage.TYPE_3BYTE_BGR, BufferedImage.TYPE_4BYTE_ABGR,
            BufferedImage.TYPE_4BYTE_ABGR_PRE,
            BufferedImage.TYPE_USHORT_565_RGB,
            BufferedImage.TYPE_USHORT_555_RGB, BufferedImage.TYPE_BYTE_GRAY,
            BufferedImage.TYPE_USHORT_GRAY, BufferedImage.TYPE_BYTE_BINARY,
            BufferedImage.TYPE_BYTE_INDEXED
    };

    
    public static void main(String[] args) throws Exception {
        for (int srcCS : spaces) {
            for (int dstCS : spaces) {
                if(srcCS != dstCS) {
                    for (int type : types) {
                        checkTypes(ColorSpace.getInstance(srcCS),
                                   ColorSpace.getInstance(dstCS), type);
                    }
                }
            }
        }
    }

    private static void checkTypes(ColorSpace srcCS, ColorSpace dstCS, int type)
            throws Exception {
        lines = new BufferedImage[SIZE];
        ColorConvertOp goldOp = new ColorConvertOp(srcCS, dstCS, null);
        BufferedImage src = createSrc(type);
        BufferedImage gold = goldOp.filter(src, null);

        
        
        
        ColorConvertOp sharedOp = new ColorConvertOp(srcCS, dstCS, null);
        Thread[] threads = new Thread[SIZE];
        for (int y = 0; y < SIZE; ++y) {
            BufferedImage line = src.getSubimage(0, y, SIZE, 1);
            threads[y] = test(sharedOp, line, y);
        }

        for (Thread t: threads) {
            t.start();
        }
        for (Thread t: threads) {
            t.join();
        }
        for (int y = 0; y < SIZE; ++y) {
            validate(gold, lines[y], y);
        }
        if (failed) {
            throw new RuntimeException("Unexpected exception");
        }
    }

    private static Thread test(ColorConvertOp sharedOp,
                               BufferedImage line, int y){
        return new Thread(() -> {
            try {
                BufferedImage image = sharedOp.filter(line, null);
                lines[y] = image;
            } catch (Throwable t) {
                t.printStackTrace();
                failed = true;
            }
        });
    }

    private static BufferedImage createSrc(int type) {
        BufferedImage img = new BufferedImage(SIZE, SIZE, type);
        fill(img);
        return img;
    }

    private static void fill(BufferedImage image) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                image.setRGB(i, j,
                             (i << 24) | (i << 16) | (j << 8) | ((i + j) >> 1));
            }
        }
    }

    private static void validate(BufferedImage full, BufferedImage line, int y) {
        for (int i = 0; i < SIZE; i++) {
            int rgb1 = full.getRGB(i, y);
            int rgb2 = line.getRGB(i, 0);
            if (rgb1 != rgb2) {
                System.err.println("rgb1 = " + Integer.toHexString(rgb1));
                System.err.println("rgb2 = " + Integer.toHexString(rgb2));
                throw new RuntimeException();
            }
        }
    }
}
