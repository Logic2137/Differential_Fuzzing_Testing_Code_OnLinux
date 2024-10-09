

import java.awt.image.*;



public class HeadlessColorModel {
    public static void main(String args[]) {
        ColorModel cm = new ColorModel(32) {
            public int getAlpha(int pixel) { return 255; }
            public int getBlue(int pixel) { return 255; }
            public int getGreen(int pixel) { return 255; }
            public int getRed(int pixel) { return 255; }
        };

        cm.hasAlpha();
        cm.isAlphaPremultiplied();
        cm.getTransferType();
        cm.getPixelSize();
        cm.getComponentSize();
        cm.getComponentSize();
        cm.getTransparency();
        cm.getNumComponents();
        cm.getNumColorComponents();
        cm.getRed(20);
        cm.getGreen(20);
        cm.getBlue(20);
        cm.getAlpha(20);
        cm.getRGB(20);
        cm.isAlphaPremultiplied();
        cm.isAlphaPremultiplied();

        cm = ColorModel.getRGBdefault();
    }
}
