import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class OSXLigatureTest {

    public static void main(String[] args) {
        if (!System.getProperty("os.name").startsWith("Mac")) {
            return;
        }
        String ligStr = "ffi";
        int w = 50, h = 50;
        BufferedImage bi1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D bi1Graphics = bi1.createGraphics();
        bi1Graphics.setColor(Color.white);
        bi1Graphics.fillRect(0, 0, w, h);
        bi1Graphics.setColor(Color.black);
        Font noLigFont = new Font("Gill Sans", Font.PLAIN, 30);
        bi1Graphics.setFont(noLigFont);
        bi1Graphics.drawString(ligStr, 10, 40);
        BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D bi2Graphics = bi2.createGraphics();
        bi2Graphics.setColor(Color.white);
        bi2Graphics.fillRect(0, 0, w, h);
        bi2Graphics.setColor(Color.black);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
        Font ligFont = noLigFont.deriveFont(attributes);
        bi2Graphics.setFont(ligFont);
        bi2Graphics.drawString(ligStr, 10, 40);
        boolean same = true;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int c1 = bi1.getRGB(x, y);
                int c2 = bi2.getRGB(x, y);
                same &= (c1 == c2);
            }
            if (!same) {
                break;
            }
        }
        if (same) {
            throw new RuntimeException("Images do not differ - no ligature");
        }
    }
}
