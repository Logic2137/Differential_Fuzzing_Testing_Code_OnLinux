



import java.awt.Dimension;

import javax.imageio.ImageWriteParam;

public class ImageWriteParamMisc {

    public static void main(String[] args) {
        test4434870();
        test4434886();
        test4441315();
        test4446842();
    }

    public static class ImageWriteParam4434870 extends ImageWriteParam {
        public ImageWriteParam4434870() {
            super(null);
            super.canWriteTiles = true;
            super.preferredTileSizes =
                new Dimension[] {new Dimension(1, 2), new Dimension(5, 6)};
        }
    }

    private static void test4434870() {
        ImageWriteParam iwp = new ImageWriteParam4434870();
        try {
            Dimension[] dimensions = iwp.getPreferredTileSizes();
            iwp.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setTiling(100, 100, 0,0);
            throw new RuntimeException("Failed to get IAE!");
        } catch (IllegalArgumentException e) {
        }
    }

    public static class ImageWriteParam4434886 extends ImageWriteParam {
        public ImageWriteParam4434886() {
            super(null);
            super.canWriteTiles = true;
            super.canOffsetTiles = true;
        }
    }

    private static void test4434886() {
        ImageWriteParam iwp = new ImageWriteParam4434886();
        iwp.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
        try {
            iwp.setTiling(-1,-2,-3,-4);
            throw new RuntimeException("Failed to get IAE!");
        } catch (IllegalArgumentException e) {
        }
    }

    public static class ImageWriteParam4441315 extends ImageWriteParam {
        public ImageWriteParam4441315() {
            super(null);
            super.canWriteProgressive = true;
        }
    }

    private static void test4441315() {
        ImageWriteParam iwp = new ImageWriteParam4441315();
        try {
            iwp.setProgressiveMode(ImageWriteParam.MODE_EXPLICIT);
            throw new RuntimeException("Failed to get IAE!");
        } catch (IllegalArgumentException e) {
        }
    }

    private static void test4446842() {
        ImageWriteParam iwp = new ImageWriteParam(null);
        try {
            iwp.getCompressionTypes();
            throw new RuntimeException("Failed to get UOE!");
        } catch (UnsupportedOperationException e) {
        }
    }
}
