



import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;


public class SampleModelConstructorTest {

    public static void main(String[] a) throws RuntimeException {

        SampleModel model = Raster.createBandedRaster(DataBuffer.TYPE_INT,
                10, 5, 4, null).getSampleModel();

        final int inputWidths[]
                = {Integer.MIN_VALUE, -1000, -1, 0, 1, 1000, Integer.MAX_VALUE};

        final int inputHeights[]
                = {Integer.MIN_VALUE, -1000, -1, 0, 1, 1000, Integer.MAX_VALUE};

        
        

        
        
        
        
        
        
        
        
        final int expectedCount = 43;
        int count = 0;

        for (int i : inputWidths) {
            for (int j : inputHeights) {
                try {
                    SampleModel model2 = model.createCompatibleSampleModel(i, j);
                } catch (IllegalArgumentException e) {
                    count++;
                }
            }
        }

        if (count != expectedCount) {
            throw new RuntimeException(
                "Test Failed. Expected IllegalArgumentException Count = " +
                expectedCount + " Got Count = " + count);
        }
    }
}

