import java.awt.*;
import java.awt.image.*;

public class FontAccess {

    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());
        Font f = new Font("Verdana", Font.PLAIN, 12);
        BufferedImage bi = new BufferedImage(1, 1, 1);
        Graphics2D g = bi.createGraphics();
        g.setFont(f);
        System.out.println(g.getFontMetrics());
    }
}
