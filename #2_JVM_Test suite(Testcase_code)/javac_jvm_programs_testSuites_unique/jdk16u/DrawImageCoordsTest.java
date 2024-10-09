



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DrawImageCoordsTest {

    public static void main(String[] args) {

        
        BufferedImage srcImg =
             new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics srcG = srcImg.createGraphics();
        srcG.setColor(Color.red);
        int w = srcImg.getWidth(null);
        int h = srcImg.getHeight(null);
        srcG.fillRect(0, 0, w, h);

        
        BufferedImage dstImage =
           new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D dstG = dstImage.createGraphics();
        
        AffineTransform tx = new AffineTransform(0.5, 0, 0, 0.5,
                                                  0, 5.8658460197478485E9);
        dstG.setTransform(tx);
        dstG.drawImage(srcImg, 0, 0, null );
        
        dstG.drawImage(srcImg, 0, 0, 2*w, 2*h, null);
        if (Color.red.getRGB() == dstImage.getRGB(w/2, h/2)) {
             throw new RuntimeException("Unexpected color: clipping failed.");
        }
        System.out.println("Test Thread Completed");
    }
}
