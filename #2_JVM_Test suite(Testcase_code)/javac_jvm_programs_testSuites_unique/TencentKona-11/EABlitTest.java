

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;


public class EABlitTest extends Frame {
    protected void test() {
        BufferedImage srcImg = createSrcImage();
        Image dstImg = getGraphicsConfiguration().createCompatibleVolatileImage(20, 50);

        
        renderToVI(srcImg, dstImg);
        renderToVI(srcImg, dstImg);

        BufferedImage validationImg = new BufferedImage(20, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D valG = (Graphics2D) validationImg.getGraphics();
        valG.drawImage(dstImg, 0, 0, null);

        
        TreeSet<Integer> colorCntSet = new TreeSet<>();
        for (int x=0; x < validationImg.getWidth(); x++) {
            for (int y=0; y < validationImg.getHeight(); y++) {
                int rgb = validationImg.getRGB(x, y);
                colorCntSet.add(rgb);
            }
        }

        
        if (colorCntSet.size() == 3) {
            System.out.println("Passed!");
        } else {
            throw new RuntimeException("Test FAILED!");
        }
    }

    protected void renderToVI(BufferedImage src, Image dst) {
        Graphics2D g = (Graphics2D) dst.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 50, 50);
        g.rotate(0.5f);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                           RenderingHints.VALUE_RENDER_QUALITY);

        g.setComposite(AlphaComposite.SrcOver.derive(1f));
        g.drawImage(src, 10, 10, null);

        g.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        g.drawImage(src, 20, 20, null);
    }

    protected BufferedImage createSrcImage() {
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 10, 10);
        return bi;
    }

    public static void main(String[] args) {
         new EABlitTest().test();
    }
}
