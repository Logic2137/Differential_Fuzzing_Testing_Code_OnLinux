import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.Color;
import java.awt.Frame;
import java.io.IOException;

public class RescaleAlphaTest {

    BufferedImage bimg = null, bimg1;

    int w = 10, h = 10;

    float scaleFactor = 0.5f;

    float offset = 0.0f;

    public static void main(String[] args) throws Exception {
        RescaleAlphaTest test = new RescaleAlphaTest();
        test.startTest();
    }

    private void startTest() throws Exception {
        bimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bimg.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, w, h);
        RescaleOp res = new RescaleOp(scaleFactor, offset, null);
        bimg1 = res.filter(bimg, null);
        checkForAlpha(bimg1);
        bimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        g2d = bimg.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, w, h);
        res = new RescaleOp(scaleFactor, offset, null);
        bimg1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bimg1 = res.filter(bimg, bimg1);
        checkForAlpha(bimg1);
    }

    private void checkForAlpha(BufferedImage bi) throws IOException {
        int argb = bi.getRGB(w / 2, h / 2);
        if ((argb >>> 24) != 255) {
            throw new RuntimeException("Wrong alpha in destination image.RescaleOp with alpha failed.");
        }
    }
}
