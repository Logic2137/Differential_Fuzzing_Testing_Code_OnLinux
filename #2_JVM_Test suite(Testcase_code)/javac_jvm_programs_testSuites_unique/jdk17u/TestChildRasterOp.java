import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class TestChildRasterOp {

    private static AffineTransform at = new AffineTransform();

    private static final AffineTransformOp rop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

    private static int[] offsets = { 0 };

    public static void main(String[] args) {
        testByteRaster();
        testShortRaster();
        testIntRaster();
    }

    private static void testByteRaster() {
        WritableRaster srcRaster, dstRaster;
        byte[] pixels = { 11, 12, 13, 14, 21, 22, 23, 24, 31, 32, 33, 34, 41, 42, 43, 44 };
        DataBuffer db = new DataBufferByte(pixels, pixels.length);
        srcRaster = Raster.createInterleavedRaster(db, 4, 4, 4, 1, offsets, null);
        srcRaster = srcRaster.createWritableChild(1, 1, 3, 3, 0, 0, null);
        dstRaster = rop.filter(srcRaster, null);
    }

    private static void testShortRaster() {
        WritableRaster srcRaster, dstRaster;
        short[] pixels = { 11, 12, 13, 14, 21, 22, 23, 24, 31, 32, 33, 34, 41, 42, 43, 44 };
        DataBuffer db = new DataBufferUShort(pixels, pixels.length);
        srcRaster = Raster.createInterleavedRaster(db, 4, 4, 4, 1, offsets, null);
        srcRaster = srcRaster.createWritableChild(1, 1, 3, 3, 0, 0, null);
        dstRaster = rop.filter(srcRaster, null);
    }

    private static void testIntRaster() {
        WritableRaster srcRaster, dstRaster;
        int[] pixels = { 11, 12, 13, 14, 21, 22, 23, 24, 31, 32, 33, 34, 41, 42, 43, 44 };
        DataBuffer db = new DataBufferInt(pixels, pixels.length);
        srcRaster = Raster.createPackedRaster(db, 4, 4, 4, offsets, null);
        srcRaster = srcRaster.createWritableChild(1, 1, 3, 3, 0, 0, null);
        dstRaster = rop.filter(srcRaster, null);
    }
}
