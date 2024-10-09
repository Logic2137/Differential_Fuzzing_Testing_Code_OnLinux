import java.awt.image.IndexColorModel;

public class HeadlessIndexColorModel {

    public static void main(String[] args) {
        IndexColorModel cm = new IndexColorModel(8, 1, new byte[] { (byte) 128 }, new byte[] { (byte) 128 }, new byte[] { (byte) 128 });
        cm.getTransparency();
        cm.getComponentSize();
        cm.isAlphaPremultiplied();
        cm.hasAlpha();
        cm.isAlphaPremultiplied();
        cm.getTransferType();
        cm.getPixelSize();
        cm.getComponentSize();
        cm.getNumComponents();
        cm.getNumColorComponents();
        cm.getRed(20);
        cm.getGreen(20);
        cm.getBlue(20);
        cm.getAlpha(20);
        cm.getRGB(20);
        cm.isAlphaPremultiplied();
    }
}
