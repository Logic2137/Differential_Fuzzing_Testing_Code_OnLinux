



import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public class ScaledImageAlphaTest {

    static final int translucentAlpha = 128, opaqueAlpha = 255;
    static final int[] translucentVariants = new int[] {
        BufferedImage.TYPE_INT_ARGB,
        BufferedImage.TYPE_INT_ARGB_PRE,
        BufferedImage.TYPE_4BYTE_ABGR,
        BufferedImage.TYPE_4BYTE_ABGR_PRE
    };
    static final int[] alphaValues = new int[] {
        translucentAlpha,
        opaqueAlpha
    };
    static int width = 50, height = 50;
    static int scaleX = 5, scaleY = 5, scaleWidth = 40, scaleHeight = 40;

    private static void verifyAlpha(Color color, int alpha) {

        
        int extractedAlpha = color.getAlpha();

        if (extractedAlpha != alpha) {
            throw new RuntimeException("Alpha channel for background"
                    + " is lost while scaling");
        }
    }

    private static void validateBufferedImageAlpha() {

        Color backgroundColor, extractedColor;
        
        for (int type : translucentVariants) {
            
            for (int alpha : alphaValues) {
                
                BufferedImage img = new
                    BufferedImage(width, height, type);
                Graphics2D imgGraphics = (Graphics2D)img.getGraphics();
                
                backgroundColor = new Color(0, 255, 0, alpha);
                imgGraphics.
                    drawImage(img, scaleX, scaleY, scaleWidth, scaleHeight,
                              backgroundColor, null);
                imgGraphics.dispose();

                
                extractedColor = new Color(img.getRGB(scaleX, scaleY), true);
                verifyAlpha(extractedColor, alpha);
            }
        }
    }

    private static void validateVolatileImageAlpha() {

        Color backgroundColor, extractedColor;
        VolatileImage img;
        BufferedImage bufImg = new
                    BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int alpha : alphaValues) {
            backgroundColor = new Color(0, 255, 0, alpha);
            do {
                img = createVolatileImage(width, height,
                                          Transparency.TRANSLUCENT);
                Graphics2D imgGraphics = (Graphics2D)img.getGraphics();
                
                imgGraphics.setComposite(AlphaComposite.Clear);
                imgGraphics.fillRect(0,0, width, height);

                imgGraphics.setComposite(AlphaComposite.SrcOver);
                
                imgGraphics.
                    drawImage(img, scaleX, scaleY, scaleWidth, scaleHeight,
                              backgroundColor, null);
                
                bufImg = img.getSnapshot();
                imgGraphics.dispose();
            } while (img.contentsLost());

            
            extractedColor = new Color(bufImg.getRGB(scaleX, scaleY), true);
            verifyAlpha(extractedColor, alpha);
        }
    }

    private static VolatileImage createVolatileImage(int width, int height,
                                                     int transparency) {
        GraphicsEnvironment ge = GraphicsEnvironment.
                                 getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().
                                   getDefaultConfiguration();

        VolatileImage image = gc.createCompatibleVolatileImage(width, height,
                                                               transparency);
        return image;
    }

    public static void main(String[] args) {
        
        validateBufferedImageAlpha();
        
        validateVolatileImageAlpha();
    }
}
