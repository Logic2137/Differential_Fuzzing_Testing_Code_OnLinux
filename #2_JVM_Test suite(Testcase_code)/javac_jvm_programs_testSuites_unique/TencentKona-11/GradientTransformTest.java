

import java.awt.*;
import java.awt.MultipleGradientPaint.*;
import java.awt.geom.*;
import java.awt.image.*;


public class GradientTransformTest extends Frame {
    BufferedImage srcImg;
    Image dstImg;

    public GradientTransformTest() {
        srcImg = createSrcImage();
        dstImg = getGraphicsConfiguration().createCompatibleVolatileImage(20,
                20);
    }

    protected void renderToVI(BufferedImage src, Image dst) {
        Graphics2D g = (Graphics2D) dst.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, dst.getWidth(null), dst.getHeight(null));

        AffineTransform at = new AffineTransform();
        at.translate(-100, 0);

        g.setPaint(new LinearGradientPaint(new Point2D.Float(100, 0),
                new Point2D.Float(120, 0), new float[] { 0.0f, 0.75f, 1.0f },
                new Color[] { Color.red, Color.green, Color.blue },
                CycleMethod.NO_CYCLE, ColorSpaceType.SRGB, at));

        g.fillRect(-10, -10, 30, 30);
    }

    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        renderToVI(createSrcImage(), dstImg);
        g.drawImage(dstImg, 20, 20, null);
    }

    public void showFrame() {
        setSize(500, 500);
        setVisible(true);
    }

    public void test() {
        renderToVI(createSrcImage(), dstImg);

        BufferedImage validationImg = new BufferedImage(20, 20,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D valG = (Graphics2D) validationImg.getGraphics();
        valG.drawImage(dstImg, 0, 0, null);

        
        
        boolean gradientTranslated = false;
        for (int x = 0; x < validationImg.getWidth() && !gradientTranslated; x++) {
            for (int y = 0; y < validationImg.getHeight()
                    && !gradientTranslated; y++) {
                int rgb = validationImg.getRGB(x, y);
                if (rgb != -65279) {
                    gradientTranslated = true;
                }
            }
        }

        if (gradientTranslated) {
            System.out.println("Passed!");
        } else {
            throw new RuntimeException("Test FAILED!");
        }
    }

    protected BufferedImage createSrcImage() {
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 10, 10);
        g.setColor(Color.black);
        g.drawLine(0, 0, 10, 10);
        return bi;
    }

    public static void main(String[] args) throws Exception {
        boolean show = (args.length > 0 && "-show".equals(args[0]));
        final GradientTransformTest t = new GradientTransformTest();

        if (show) {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    t.showFrame();
                }
            });
        } else {
            t.test();
        }
    }
}
