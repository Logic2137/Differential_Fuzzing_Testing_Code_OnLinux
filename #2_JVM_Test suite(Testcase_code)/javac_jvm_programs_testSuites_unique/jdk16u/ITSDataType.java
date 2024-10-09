



import java.awt.color.ColorSpace;
import java.awt.image.DataBuffer;

import javax.imageio.ImageTypeSpecifier;

public class ITSDataType {

    public static final int[] dataTypes = new int[] {
        DataBuffer.TYPE_BYTE,
        DataBuffer.TYPE_SHORT,
        DataBuffer.TYPE_USHORT,
        DataBuffer.TYPE_INT,
        DataBuffer.TYPE_FLOAT,
        DataBuffer.TYPE_DOUBLE,
    };

    public static void main(String[] args) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        int[] bankIndices = new int[] { 1 };
        int[] bandOffsets = new int[] { 0 };

        
        for (int i = 0; i < dataTypes.length; i++) {
            int dataType = dataTypes[i];

            try {
                ImageTypeSpecifier.createBanded(cs, bankIndices, bandOffsets,
                                                dataType, false, false);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("createBanded() test failed for " +
                                           "dataType = " + dataType);
            }
        }

        
        for (int i = 0; i < dataTypes.length; i++) {
            int dataType = dataTypes[i];

            try {
                ImageTypeSpecifier.createInterleaved(cs, bandOffsets,
                                                     dataType, false, false);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("createInterleaved() test failed " +
                                           "for dataType = " + dataType);
            }
        }
    }
}
