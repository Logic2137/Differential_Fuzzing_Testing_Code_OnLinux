



import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class AlphaTest {
    public static void main(String[] args) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);

        ColorConvertOp op = new ColorConvertOp(cs, null);
        
        BufferedImage src = createSrc();
        int srcAlpha = getAlpha(src);

        System.out.printf("Src alpha: 0x%02x\n", srcAlpha);

        
        BufferedImage dst = createDst();
        int dstAlpha = getAlpha(dst);
        System.out.printf("Dst alpha: 0x%02x\n", dstAlpha);


        dst = op.filter(src, dst);
        dstAlpha = getAlpha(dst);
        
        
        
        System.out.printf("Result alpha: 0x%02x\n", dstAlpha);

        if (srcAlpha != dstAlpha) {
            throw new RuntimeException("Test failed!");
        }
        System.out.println("Test passed");
    }

    private static BufferedImage createSrc() {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = img.createGraphics();
        g.setColor(Color.red);
        g.fillRect(0, 0, w, h);
        g.dispose();

        return img;
    }

    private static BufferedImage createDst() {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = img.createGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, w, h);
        g.dispose();

        return img;
    }

    private static int getAlpha(BufferedImage img) {
        int argb = img.getRGB(w / 2, h / 2);
        return 0xff & (argb >> 24);
    }

    private static final int w = 100;
    private static final int h = 100;
}
