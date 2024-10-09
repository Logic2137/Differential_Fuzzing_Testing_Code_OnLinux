



import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class TestAATMorxFont {
    public static void main(String[] args) {
        String osName = System.getProperty("os.name");
        System.out.println("OS is " + osName);
        osName = osName.toLowerCase();
        if (!osName.startsWith("mac")) {
            return;
        }
        BufferedImage bi = new BufferedImage(1200, 400, TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        test(g);
        g.dispose();
    }

    private static void test(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int y = 50;
        g.setFont(new Font("Gujarati MT", Font.PLAIN, 40));
        System.out.println(g.getFont());
        g.drawString("\u0A95\u0ACD \u0A95\u0A95\u0A95 \u0A95\u0ACD\u0A95\u0ACD\u0A95", 20, y);
        y += 50;
        g.setFont(new Font("Tamil Sangam MN", Font.PLAIN, 40));
        System.out.println(g.getFont());
        g.drawString("\u0b95\u0bCD \u0b95\u0b95\u0b95 \u0b95\u0bCD\u0b95\u0bCD\u0b95", 20, y);
        y += 50;
        g.setFont(new Font("Telugu Sangam MN", Font.PLAIN, 40));
        System.out.println(g.getFont());
        g.drawString("\u0c15\u0c4D \u0c15\u0c15\u0c15 \u0c15\u0c4D\u0c15\u0c4D\u0c15", 20, y);
        y += 50;
        g.setFont(new Font("Devanagari Sangam MN", Font.PLAIN, 40));
        System.out.println(g.getFont());
        g.drawString("\u0915\u0940 \u0915\u0947 \u0915\u0942", 20, y);
        y += 50;
        g.drawString("\u0907\u0930\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915", 20, y);
        y += 50;
        g.drawString("\u0930\u093F\u0935\u094D\u092F\u0942 \u0915\u0947 \u092C\u093E\u0926 \u0935\u093F\u0915\u093E\u0938 \u0913\u0932\u0902\u092A\u093F\u0915 \u0938\u0947 \u092C\u093E\u0939\u0930 (\u0926\u0947\u0935\u0928\u093E\u0917\u0930\u0940) (\u0939\u093F\u0928\u094D\u0926\u0940) \u0907\u0930\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915\u094D\u0915", 20, y);

    }
}

