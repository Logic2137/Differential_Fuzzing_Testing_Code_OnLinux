


import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class TestNullSetColor {

    public static void main(String[] argv) {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();

        g.setColor(Color.RED);
        g.setColor(null);

        if (g.getColor() != Color.RED) {
            throw new RuntimeException("Setting setColor(null) is not ignored");
        }
    }
}
