import java.awt.image.BufferedImage;

public class HeadlessBufferedImage {

    public static void main(String[] args) {
        BufferedImage bi;
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_3BYTE_BGR);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_4BYTE_ABGR);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_BYTE_BINARY);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_BYTE_GRAY);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_BYTE_INDEXED);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB_PRE);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_INT_BGR);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_USHORT_565_RGB);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_USHORT_GRAY);
        bi = new BufferedImage(300, 300, BufferedImage.TYPE_USHORT_555_RGB);
        bi.getType();
        bi.getColorModel();
        bi.getRaster();
        bi.getAlphaRaster();
        bi.getRGB(1, 1);
        bi.getWidth();
        bi.getHeight();
        bi.getSource();
        bi.flush();
        bi.getGraphics();
        bi.createGraphics();
        BufferedImage bi2 = bi.getSubimage(10, 10, 200, 200);
        bi.isAlphaPremultiplied();
        bi.coerceData(true);
        bi.coerceData(false);
        bi.toString();
        bi.getSources();
        bi.getPropertyNames();
        bi.getMinX();
        bi.getMinY();
        bi.getSampleModel();
        bi.getNumXTiles();
        bi.getNumYTiles();
        bi.getMinTileX();
        bi.getMinTileY();
        bi.getTileWidth();
        bi.getTileHeight();
        bi.getTileGridXOffset();
        bi.getTileGridYOffset();
        bi.getData();
    }
}
