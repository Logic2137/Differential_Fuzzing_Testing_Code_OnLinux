import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.List;

public class MacEmoji {

    private static final int IMG_WIDTH = 20;

    private static final int IMG_HEIGHT = 20;

    public static void main(String[] args) {
        GraphicsConfiguration cfg = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage vImg = cfg.createCompatibleVolatileImage(IMG_WIDTH, IMG_HEIGHT);
        BufferedImage refImg;
        int attempt = 0;
        do {
            if (++attempt > 10) {
                throw new RuntimeException("Failed to render to VolatileImage");
            }
            if (vImg.validate(cfg) == VolatileImage.IMAGE_INCOMPATIBLE) {
                throw new RuntimeException("Unexpected validation failure");
            }
            drawEmoji(vImg);
            refImg = vImg.getSnapshot();
        } while (vImg.contentsLost());
        boolean rendered = false;
        for (int x = 0; x < IMG_WIDTH; x++) {
            for (int y = 0; y < IMG_HEIGHT; y++) {
                if (refImg.getRGB(x, y) != 0xFFFFFFFF) {
                    rendered = true;
                    break;
                }
            }
        }
        if (!rendered) {
            throw new RuntimeException("Emoji character wasn't rendered");
        }
        List<Integer> imageTypes = List.of(BufferedImage.TYPE_INT_RGB, BufferedImage.TYPE_INT_ARGB, BufferedImage.TYPE_INT_ARGB_PRE, BufferedImage.TYPE_INT_BGR, BufferedImage.TYPE_3BYTE_BGR, BufferedImage.TYPE_4BYTE_ABGR, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        for (Integer type : imageTypes) {
            BufferedImage img = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
            drawEmoji(img);
            for (int x = 0; x < IMG_WIDTH; x++) {
                for (int y = 0; y < IMG_HEIGHT; y++) {
                    if (refImg.getRGB(x, y) != img.getRGB(x, y)) {
                        throw new RuntimeException("Rendering differs for image type " + type);
                    }
                }
            }
        }
    }

    private static void drawEmoji(Image img) {
        Graphics g = img.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);
        g.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        g.drawString("\uD83D\uDE00", 2, 15);
        g.dispose();
    }
}
