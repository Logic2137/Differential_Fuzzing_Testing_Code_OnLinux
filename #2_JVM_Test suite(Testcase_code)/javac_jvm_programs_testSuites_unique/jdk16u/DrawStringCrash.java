



import java.awt.*;
import java.awt.image.*;

public class DrawStringCrash {

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        String s = "abcdefghijklmnopqrstuzwxyz";
        for (int x = 0; x < 100000 ; x++) {
           sb.append(s);
        }
        
        
        
        
        
        
        long maxLen = 8L * 1024 * 1024 * 1024;
        int len = sb.length();

        BufferedImage bi =
            new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        while (len < maxLen) {
            try {
                g2d.drawString(sb.toString(), 20, 20);
                sb.append(sb);
                len *= 2;
            } catch (OutOfMemoryError e) {
                return;
            }
        }
        return;
    }
}
