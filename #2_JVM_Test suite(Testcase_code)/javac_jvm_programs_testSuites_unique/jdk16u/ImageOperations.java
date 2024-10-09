

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;


public final class ImageOperations {

    public static void main(String[] args) throws Exception {
        BufferedImage gold = new BufferedImage(255, 255, TYPE_INT_ARGB_PRE);
        BufferedImage target = new BufferedImage(255, 255, TYPE_INT_ARGB_PRE);
        fill(gold);

        FileDialog fd = new FileDialog((Dialog) null);
        fd.pack();
        try {
            Image image = fd.createImage(gold.getSource());
            if (image == null) {
                throw new NullPointerException();
            }
            if (!fd.prepareImage(image, null)) {
                Thread.sleep(100);
            }
            if ((fd.checkImage(image, null) & ImageObserver.ALLBITS) == 0) {
                throw new RuntimeException("Image should be loaded already");
            }

            Graphics2D graphics = (Graphics2D) target.getGraphics();
            graphics.setComposite(AlphaComposite.Src);
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();

            validate(gold, target);
        } finally {
            fd.dispose();
        }
    }

    
    private static void fill(Image image) {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setComposite(AlphaComposite.Src);
        graphics.setColor(Color.GREEN);
        graphics.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
        for (int i = 0; i < 255; ++i) {
            graphics.setColor(new Color(23, 127, 200, i));
            graphics.fillRect(0, i, image.getWidth(null), 1);
        }
        graphics.dispose();
    }

    private static void validate(BufferedImage bi, BufferedImage goldbi)
            throws IOException {
        for (int x = 0; x < bi.getWidth(); ++x) {
            for (int y = 0; y < bi.getHeight(); ++y) {
                if (goldbi.getRGB(x, y) != bi.getRGB(x, y)) {
                    System.out.println("x = " + x);
                    System.out.println("y = " + y);
                    ImageIO.write(bi, "png", new File("actual.png"));
                    ImageIO.write(goldbi, "png", new File("expected.png"));
                    throw new RuntimeException("Test failed.");
                }
            }
        }
    }
}
