import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

public final class GetTileGridOffset {

    public static void main(final String[] args) {
        final BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_BYTE_GRAY);
        validate(image);
        final BufferedImage subImage = image.getSubimage(5, 5, 10, 10);
        verifyConsistency(subImage);
        validate(subImage);
    }

    private static void verifyConsistency(final RenderedImage image) {
        final int tileWidth = image.getTileWidth();
        final int tileHeight = image.getTileHeight();
        final int x = image.getMinX() + tileWidth - 1;
        final int y = image.getMinY() + tileHeight - 1;
        int tileX = Math.floorDiv(x - image.getTileGridXOffset(), tileWidth);
        int tileY = Math.floorDiv(y - image.getTileGridYOffset(), tileHeight);
        int minTileX = image.getMinTileX();
        int minTileY = image.getMinTileY();
        if (tileX != minTileX || tileY != minTileY) {
            throw new RuntimeException("Tile indices of upper-left tile" + " shall be (" + minTileX + ", " + minTileY + ")." + " But using the provided tileGridOffsets we got" + " (" + tileX + ", " + tileY + ").");
        }
    }

    private static void validate(final BufferedImage image) {
        final int tileGridXOffset = image.getTileGridXOffset();
        final int tileGridYOffset = image.getTileGridYOffset();
        if (tileGridXOffset != 0) {
            throw new RuntimeException("BufferedImage.getTileGridXOffset()" + " shall be zero. Got " + tileGridXOffset);
        }
        if (tileGridYOffset != 0) {
            throw new RuntimeException("BufferedImage.getTileGridTOffset()" + " shall be zero. Got " + tileGridYOffset);
        }
    }
}
