



import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.Arrays;

public class bug8038000 {

    public static void main(String[] args) throws Exception {
        new bug8038000().checkOps();

        
    }

    private void checkOps() throws Exception {

        RasterOp[] ops = new RasterOp[] {
                new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB), null),
                new AffineTransformOp(AffineTransform.getScaleInstance(1, 1.1), null)
        };


        for (RasterOp op: ops) {
            
            checkOp(Raster.createBandedRaster(DataBuffer.TYPE_BYTE, 10, 1, 10,
                            new int[] {0, 1, 2}, new int[]{2,1,0}, null),
                    Raster.createBandedRaster(DataBuffer.TYPE_BYTE, 10, 1, 1001,
                            new int[] {0, 1, 2}, new int[]{2,1,0}, null), op);
            checkOp(Raster.createBandedRaster(DataBuffer.TYPE_USHORT, 10, 1, 10,
                    new int[] {0, 1, 2}, new int[]{2,1,0}, null),
                    Raster.createBandedRaster(DataBuffer.TYPE_USHORT, 10, 1, 1001,
                            new int[] {0, 1, 2}, new int[]{2,1,0}, null), op);
            checkOp(Raster.createBandedRaster(DataBuffer.TYPE_INT, 10, 1, 10,
                    new int[] {0, 1, 2}, new int[]{2,1,0}, null),
                    Raster.createBandedRaster(DataBuffer.TYPE_INT, 10, 1, 1001,
                            new int[] {0, 1, 2}, new int[]{2,1,0}, null), op);

            
            checkOp(Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                            10, 1, 30, 3, new int[]{0, 1, 2}, null),
                    Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                            10, 1, 1001, 3, new int[]{0, 1, 2}, null),
                    op);

            checkOp(Raster.createInterleavedRaster(DataBuffer.TYPE_USHORT,
                            10, 1, 30, 3, new int[]{0, 1, 2}, null),
                    Raster.createInterleavedRaster(DataBuffer.TYPE_USHORT,
                            10, 1, 1001, 3, new int[]{0, 1, 2}, null),
                    op);

            
            checkOp(Raster.createPackedRaster(new DataBufferByte(10), 10, 1, 10,
                            new int[] {0x01, 0x02, 0x04}, null),
                    Raster.createPackedRaster(new DataBufferByte(10), 10, 1, 2000,
                            new int[] {0x01, 0x02, 0x04}, null),
                    op);
            checkOp(Raster.createPackedRaster(new DataBufferInt(10), 10, 1, 10,
                        new int[] {0xff0000, 0x00ff00, 0x0000ff}, null),
                    Raster.createPackedRaster(new DataBufferInt(10), 10, 1, 20,
                            new int[] {0xff0000, 0x00ff00, 0x0000ff}, null),
                    op);

        }
    }

    
    private void checkOp(WritableRaster wr1, WritableRaster wr2, RasterOp op) {
        System.out.println("Checking " + op + " with rasters: \n    " + wr1 +
                "\n    " + wr2);
        try {
            WritableRaster r1 = op.filter(fillRaster(wr1), null);
            WritableRaster r2 = op.filter(fillRaster(wr2), null);
            compareRasters(r1, r2);
        } catch (ImagingOpException e) {
            System.out.println("    Skip: Op is not supported: " + e);
        }
    }

    private WritableRaster fillRaster(WritableRaster wr) {
        int c = 0;
        for(int x = wr.getMinX(); x < wr.getMinX() + wr.getWidth(); x++) {
            for(int y = wr.getMinY(); y < wr.getMinY() + wr.getHeight(); y++) {
                for (int b = 0; b < wr.getNumBands(); b++) {
                    wr.setSample(x, y, b, c++);
                }
            }
        }
        return wr;
    }

    private void compareRasters(Raster r1, Raster r2) {
        Rectangle bounds = r1.getBounds();
        if (!bounds.equals(r2.getBounds())) {
            throw new RuntimeException("Bounds differ.");
        }

        if (r1.getNumBands() != r2.getNumBands()) {
            throw new RuntimeException("Bands differ.");
        }

        int[] b1 = new int[r1.getNumBands()];
        int[] b2 = new int[r1.getNumBands()];

        for (int x = (int) bounds.getX(); x < bounds.getMaxX(); x++) {
            for (int y = (int) bounds.getY(); y < bounds.getMaxY(); y++) {
                r1.getPixel(x,y, b1);
                r2.getPixel(x,y, b2);
                if (!Arrays.equals(b1, b2)) {
                    throw new RuntimeException("Pixels differ.");
                }
            }
        }
    }
}
