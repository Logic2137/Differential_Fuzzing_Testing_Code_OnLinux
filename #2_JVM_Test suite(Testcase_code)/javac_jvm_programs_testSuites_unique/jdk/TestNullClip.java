


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Shape;


public class TestNullClip {

    public static void main(String[] argv) {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D)bi.getGraphics();

        g2d.clip(null); 

        g2d.setClip(0, 0, 100, 100);
        g2d.setClip(null);
        Shape clip1 = g2d.getClip();
        if (clip1 != null) {
            throw new RuntimeException("Clip is not cleared");
        }
        g2d.setClip(0, 0, 100, 100);
        try {
            g2d.clip(null);
            throw new RuntimeException("NPE is expected");
        } catch (NullPointerException e) {
            
            System.out.println("NPE is thrown");
        }
    }
}
