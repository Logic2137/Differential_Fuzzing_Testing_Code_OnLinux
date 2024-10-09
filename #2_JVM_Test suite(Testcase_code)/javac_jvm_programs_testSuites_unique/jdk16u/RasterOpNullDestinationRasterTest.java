



import java.awt.Point;
import java.awt.image.ByteLookupTable;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.LookupOp;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.SampleModel;

public class RasterOpNullDestinationRasterTest {

    public static void main(String[] args) {

        byte[][] data = new byte[1][10];
        ByteLookupTable lut = new ByteLookupTable(0, data);
        RasterOp op = new LookupOp(lut, null);

        int[] bandOffsets = {0};
        Point location = new Point(0, 0);
        DataBuffer db = new DataBufferByte(10 * 10);
        SampleModel sm = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE,
                                                         10, 10, 1, 10,
                                                         bandOffsets);

        Raster src = Raster.createRaster(sm, db, location);

        op.filter(src, null); 
    }
}
