import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.SinglePixelPackedSampleModel;

public class RasterCreationTest {

    public static void main(String[] args) {
        final int width = 10;
        final int height = 5;
        final int imageSize = width * height;
        Point location = new Point(0, 0);
        int[] bandOffsets = { 0 };
        int[] bitMask = { 0x00ff0000, 0x0000ff00, 0xff, 0x0 };
        SampleModel[] inputSampleModels = { new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, 1, 1, 1, 1, bandOffsets), new PixelInterleavedSampleModel(DataBuffer.TYPE_USHORT, 1, 1, 1, 1, bandOffsets), new PixelInterleavedSampleModel(DataBuffer.TYPE_INT, 1, 1, 1, 1, bandOffsets), new SinglePixelPackedSampleModel(DataBuffer.TYPE_BYTE, width, height, bitMask), new SinglePixelPackedSampleModel(DataBuffer.TYPE_USHORT, width, height, bitMask), new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, width, height, bitMask), new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE, width, height, 4), new MultiPixelPackedSampleModel(DataBuffer.TYPE_USHORT, width, height, 2), new MultiPixelPackedSampleModel(DataBuffer.TYPE_INT, width, height, 2) };
        DataBuffer[] inputDataBuffer = { new DataBufferByte(imageSize), new DataBufferUShort(imageSize), new DataBufferInt(imageSize, 1), new DataBufferShort(imageSize), new DataBufferFloat(imageSize), new DataBufferDouble(imageSize) };
        for (SampleModel sm : inputSampleModels) {
            for (DataBuffer db : inputDataBuffer) {
                Raster.createRaster(sm, db, location);
                Raster.createWritableRaster(sm, db, location);
                Raster.createWritableRaster(sm, location);
            }
        }
        DataBuffer[] myDataBuffer = { new MyDataBufferByte(imageSize), new MyDataBufferUShort(imageSize), new MyDataBufferInt(imageSize), new MyDataBufferShort(imageSize), new MyDataBufferDouble(imageSize), new MyDataBufferFloat(imageSize) };
        for (SampleModel sm : inputSampleModels) {
            for (DataBuffer db : myDataBuffer) {
                Raster.createRaster(sm, db, location);
                Raster.createWritableRaster(sm, db, location);
                Raster.createWritableRaster(sm, location);
            }
        }
        int[] interleavedInputDataTypes = { DataBuffer.TYPE_BYTE, DataBuffer.TYPE_USHORT };
        int numBands = 1;
        for (int i : interleavedInputDataTypes) {
            Raster.createInterleavedRaster(i, width, height, 1, location);
            Raster.createInterleavedRaster(i, width, height, width * numBands, numBands, bandOffsets, location);
        }
        for (int i = 0; i < interleavedInputDataTypes.length; i++) {
            DataBuffer d1 = inputDataBuffer[i];
            DataBuffer d2 = myDataBuffer[i];
            Raster.createInterleavedRaster(d1, width, height, width * numBands, numBands, bandOffsets, location);
            Raster.createInterleavedRaster(d2, width, height, width * numBands, numBands, bandOffsets, location);
        }
        int[] bankIndices = new int[numBands];
        bankIndices[0] = 0;
        int[] bandedInputDataTypes = { DataBuffer.TYPE_BYTE, DataBuffer.TYPE_USHORT, DataBuffer.TYPE_INT };
        for (int i : bandedInputDataTypes) {
            Raster.createBandedRaster(i, width, height, 1, location);
            Raster.createBandedRaster(i, width, height, width, bankIndices, bandOffsets, location);
        }
        for (int i = 0; i < bandedInputDataTypes.length; i++) {
            DataBuffer d1 = inputDataBuffer[i];
            DataBuffer d2 = myDataBuffer[i];
            Raster.createBandedRaster(d1, width, height, width, bankIndices, bandOffsets, location);
            Raster.createBandedRaster(d2, width, height, width, bankIndices, bandOffsets, location);
        }
        int[] bandMasks = new int[numBands];
        bandMasks[0] = 0;
        int[] packedInputDataTypes = { DataBuffer.TYPE_BYTE, DataBuffer.TYPE_USHORT, DataBuffer.TYPE_INT };
        for (int i : packedInputDataTypes) {
            Raster.createPackedRaster(i, width, height, bandMasks, location);
            for (int bits = 1; bits < 5; bits *= 2) {
                Raster.createPackedRaster(i, width, height, 1, bits, location);
            }
        }
        for (int i = 0; i < packedInputDataTypes.length; i++) {
            DataBuffer d1 = inputDataBuffer[i];
            DataBuffer d2 = myDataBuffer[i];
            for (int bits = 1; bits < 5; bits *= 2) {
                Raster.createPackedRaster(d1, width, height, bits, location);
                Raster.createPackedRaster(d2, width, height, bits, location);
            }
            Raster.createPackedRaster(d1, width, height, 1, bandMasks, location);
            Raster.createPackedRaster(d2, width, height, 1, bandMasks, location);
        }
    }
}

final class MyDataBufferByte extends DataBuffer {

    byte[] data;

    byte[][] bankdata;

    public MyDataBufferByte(int size) {
        super(TYPE_BYTE, size);
        data = new byte[size];
        bankdata = new byte[1][];
        bankdata[0] = data;
    }

    @Override
    public int getElem(int bank, int i) {
        return bankdata[bank][i + offsets[bank]];
    }

    @Override
    public void setElem(int bank, int i, int val) {
        bankdata[bank][i + offsets[bank]] = (byte) val;
    }
}

final class MyDataBufferDouble extends DataBuffer {

    double[] data;

    double[][] bankdata;

    public MyDataBufferDouble(int size) {
        super(TYPE_DOUBLE, size);
        data = new double[size];
        bankdata = new double[1][];
        bankdata[0] = data;
    }

    @Override
    public int getElem(int bank, int i) {
        return (int) bankdata[bank][i + offsets[bank]];
    }

    @Override
    public void setElem(int bank, int i, int val) {
        bankdata[bank][i + offsets[bank]] = (double) val;
    }
}

final class MyDataBufferFloat extends DataBuffer {

    float[] data;

    float[][] bankdata;

    public MyDataBufferFloat(int size) {
        super(TYPE_FLOAT, size);
        data = new float[size];
        bankdata = new float[1][];
        bankdata[0] = data;
    }

    @Override
    public int getElem(int bank, int i) {
        return (int) bankdata[bank][i + offsets[bank]];
    }

    @Override
    public void setElem(int bank, int i, int val) {
        bankdata[bank][i + offsets[bank]] = (float) val;
    }
}

final class MyDataBufferShort extends DataBuffer {

    short[] data;

    short[][] bankdata;

    public MyDataBufferShort(int size) {
        super(TYPE_SHORT, size);
        data = new short[size];
        bankdata = new short[1][];
        bankdata[0] = data;
    }

    @Override
    public int getElem(int bank, int i) {
        return bankdata[bank][i + offsets[bank]];
    }

    @Override
    public void setElem(int bank, int i, int val) {
        bankdata[bank][i + offsets[bank]] = (short) val;
    }
}

final class MyDataBufferUShort extends DataBuffer {

    short[] data;

    short[][] bankdata;

    public MyDataBufferUShort(int size) {
        super(TYPE_USHORT, size);
        data = new short[size];
        bankdata = new short[1][];
        bankdata[0] = data;
    }

    @Override
    public int getElem(int bank, int i) {
        return bankdata[bank][i + offsets[bank]];
    }

    @Override
    public void setElem(int bank, int i, int val) {
        bankdata[bank][i + offsets[bank]] = (short) val;
    }
}

final class MyDataBufferInt extends DataBuffer {

    int[] data;

    int[][] bankdata;

    public MyDataBufferInt(int size) {
        super(TYPE_INT, size);
        data = new int[size];
        bankdata = new int[1][];
        bankdata[0] = data;
    }

    @Override
    public int getElem(int bank, int i) {
        return bankdata[bank][i + offsets[bank]];
    }

    @Override
    public void setElem(int bank, int i, int val) {
        bankdata[bank][i + offsets[bank]] = (int) val;
    }
}
