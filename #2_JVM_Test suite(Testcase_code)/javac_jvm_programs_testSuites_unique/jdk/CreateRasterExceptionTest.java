



import java.awt.Point;
import java.awt.image.BandedSampleModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferFloat;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;

public class CreateRasterExceptionTest {

    static int[] bankIndices = new int[] { 0, 0};
    static int[] negBankIndices = new int[] { -1, 0};
    static int[] bandOffsets = new int[] { 0, 0};
    static int[] bandOffsets2 = new int[] { 0, 0, 0, 0};
    static int[] zeroBandOffsets = new int[] {};
    static DataBuffer dBuffer = new DataBufferByte(15);

    static void noException() {
         Thread.dumpStack();
         throw new RuntimeException("No expected exception");
    }

    
    static void checkIsOldVersion(Throwable t) {
        String version = System.getProperty("java.version");
        version = version.split("\\D")[0];
        int v = Integer.parseInt(version);
        if (v >= 17) {
            t.printStackTrace();
            throw new RuntimeException(
                           "Unexpected exception for version " + v);
        }
    }

    public static void main(String[] args) {
         componentSampleModelTests1();
         componentSampleModelTests2();
         bandedSampleModelTests1();
         bandedSampleModelTests2();
         bandedRasterTests1();
         bandedRasterTests2();
         bandedRasterTests3();
         interleavedRasterTests1();
         interleavedRasterTests2();
         interleavedRasterTests3();
         System.out.println();
         System.out.println(" ** Test Passed **");
    }


    
    static void componentSampleModelTests1() {

        System.out.println();
        System.out.println("** componentSampleModelTests1");

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT, -5, 1, 3, 15,
                                     bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                         "Got expected exception for negative width");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT,
                   Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2,
                   3, 15, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                        "Got expected exception for exceeding max int");
            System.out.println(t);
        }

        try {
             
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, -3, 15,
                                     bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                "Got expected exception for negative pixel stride");
            System.out.println(t);
        }

        try {
             
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, 3, -15,
                                     bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                "Got expected exception for negative scanline stride");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, 3, 15,
                                     bankIndices, null);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                        "Got expected exception for null bandOffsets");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, 3, 15,
                                     bankIndices, zeroBandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                        "Got expected exception for 0 bandOffsets");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(-1234, 5, 1, 3, 15,
                                     bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                      "Got expected exception for bad databuffer type");
            System.out.println(t);
        }
    }

    
    static void componentSampleModelTests2() {

        System.out.println();
        System.out.println("** componentSampleModelTests2");

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT, -5, 1, 3, 15,
                                     bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                     "Got expected exception for negative width");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT,
                   Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2,
                   3, 15, bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                     "Got expected exception for exceeding max int");
            System.out.println(t);
        }

        try {
             
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, -3, 15,
                                     bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                "Got expected exception for negative pixel stride");
            System.out.println(t);
        }

        try {
             
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, 3, -15,
                                     bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                "Got expected exception for negative scanline stride");
            System.out.println(t);
        }

        try {
         
             new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, 3, 15,
                                      null, bandOffsets);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null bankIndices");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, 3, 15,
                                     bankIndices, null);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null bandOffsets");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, 3, 15,
                                     bankIndices, zeroBandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for 0 bandOffsets");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, 3, 15,
                                     bankIndices, bandOffsets2);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for " +
                "bandOffsets.length != bankIndices.length");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(DataBuffer.TYPE_INT, 5, 1, 3, 15,
                                     negBankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for " +
                "negative bank Index");
            System.out.println(t);
        }

        try {
            
            new ComponentSampleModel(-1234, 5, 1, 3, 15,
                                     bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for bad databuffer type");
            System.out.println(t);
        }
    }

    
    static void bandedSampleModelTests1() {

        System.out.println();
        System.out.println("** bandedSampleModelTests1");

        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT, -5, 1, 1);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for negative width");
            System.out.println(t);
        }

        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT,
                   Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2, 1);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for exceeding max int");
            System.out.println(t);
        }

        
        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT, 5, 1, 0);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for 0 numBands");
            System.out.println(t);
        }

        
        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT, 5, 1, -1);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for < 0 numBands");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println(
                   "Got expected exception for < 0 numBands");
            System.out.println(t);
        }

        try {
            
            new BandedSampleModel(-1234, 5, 1, 3);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for bad databuffer type");
            System.out.println(t);
        }
    }

    
    static void bandedSampleModelTests2() {

        System.out.println();
        System.out.println("** bandedSampleModelTests2");

        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT, -5, 1, 15,
                                     bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                      "Got expected exception for negative width");
            System.out.println(t);
        }

        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT,
                   Integer.MAX_VALUE / 8, Integer.MAX_VALUE / 8,
                   Integer.MAX_VALUE, bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for exceeding max int");
            System.out.println(t);
        }

        try {
             
            new BandedSampleModel(DataBuffer.TYPE_INT, 5, 1, -15,
                                     bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                "Got expected exception for negative scanline stride");
            System.out.println(t);
        }

        try {
         
             new BandedSampleModel(DataBuffer.TYPE_INT, 5, 1, 15,
                                      null, bandOffsets);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null bankIndices");
            System.out.println(t);
        }

        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT, 5, 1, 15,
                                     bankIndices, null);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null bandOffsets");
            System.out.println(t);
        }

        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT, 5, 1, 15,
                                     bankIndices, zeroBandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for 0 bandOffsets");
            System.out.println(t);
        }

        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT, 5, 1, 15,
                                     bankIndices, bandOffsets2);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for " +
                "bandOffsets.length != bankIndices.length");
            System.out.println(t);
        }

        try {
            
            new BandedSampleModel(DataBuffer.TYPE_INT, 5, 1, 15,
                                     negBankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for " +
                "negative bank Index");
            System.out.println(t);
        }

        try {
            
            new BandedSampleModel(-1234, 5, 1, 15,
                                     bankIndices, bandOffsets);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for bad databuffer type");
            System.out.println(t);
        }
    }

      
    static void bandedRasterTests1() {

        System.out.println();
        System.out.println("** bandedRasterTests1");

        Point p = new Point();

        try {
            
            
            Raster.createBandedRaster(DataBuffer.TYPE_INT,
                                      1,  -1, 3, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(DataBuffer.TYPE_INT,
                    Integer.MAX_VALUE/8, Integer.MAX_VALUE/8, 3, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
            
            Point pt = new Point(5, 1);
            Raster.createBandedRaster(DataBuffer.TYPE_INT,
                    Integer.MAX_VALUE-2, 1, 1, pt);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        } catch (NegativeArraySizeException | OutOfMemoryError t) {
            checkIsOldVersion(t);
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(DataBuffer.TYPE_INT,
                                      1, 1, 0, null);
            noException();
        } catch (ArrayIndexOutOfBoundsException t) {
            System.out.println("Got expected exception for zero bands");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(DataBuffer.TYPE_FLOAT,
                                       5, 1, 3, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for bad databuffer type");
            System.out.println(t);
        }
    }

    
    static void bandedRasterTests2() {

        System.out.println();
        System.out.println("** bandedRasterTests2");

        Point p = new Point();

        try {
            
            
            Raster.createBandedRaster(DataBuffer.TYPE_INT, 1, -1, 3,
                                      bankIndices, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(DataBuffer.TYPE_INT,
                    Integer.MAX_VALUE/8, Integer.MAX_VALUE/8,
                    Integer.MAX_VALUE,
                    bankIndices, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
            
            Point pt = new Point(5, 1);
            Raster.createBandedRaster(DataBuffer.TYPE_INT,
                    Integer.MAX_VALUE-2, 1,
                    Integer.MAX_VALUE-2, bankIndices, bandOffsets, pt);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
             
            Raster.createBandedRaster(DataBuffer.TYPE_INT, 1, 1, -3,
                                      bankIndices, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                "Got expected exception for negative scanline stride");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(DataBuffer.TYPE_INT, 1, 1, 0,
                                      null, bandOffsets, null);
            noException();
        } catch (ArrayIndexOutOfBoundsException t) {
            System.out.println(
                   "Got expected exception for null bankIndices");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(DataBuffer.TYPE_INT, 1, 1, 0,
                                      bankIndices, null, null);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null bandoffsets");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(DataBuffer.TYPE_FLOAT, 5, 1, 3,
                                      bankIndices, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for bad databuffer type");
            System.out.println(t);
        }
    }

    
    static void bandedRasterTests3() {

        System.out.println();
        System.out.println("** bandedRasterTests3");

        Point p = new Point();

        try {
            
            Raster.createBandedRaster(dBuffer, 1, -1, 3,
                                      bankIndices, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(dBuffer,
                    Integer.MAX_VALUE/8, Integer.MAX_VALUE/8,
                    Integer.MAX_VALUE, bankIndices, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
        }

        try {
            
            Point pt = new Point(5, 1);
            Raster.createBandedRaster(dBuffer,
                    Integer.MAX_VALUE-2, 1, Integer.MAX_VALUE,
                    bankIndices, bandOffsets, pt);
            noException();
        } catch (RasterFormatException t) {
            System.out.println(
                   "Got expected raster exception for overflow");
            System.out.println(t);
        }

        try {
             
            Raster.createBandedRaster(dBuffer, 1, 1, -3,
                                      bankIndices, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                "Got expected exception for negative scanline stride");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(dBuffer, 1, 1, 0,
                                      null, bandOffsets, null);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null bankIndices");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(null, 1, 1, 3,
                                      bankIndices, bandOffsets, null);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null dataBuffer");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(dBuffer, 1, 1, 3,
                                      bankIndices, bandOffsets2, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for different arrlen");
            System.out.println(t);
        }

        try {
            
            Raster.createBandedRaster(dBuffer, 1, 1, 0,
                                      bankIndices, null, null);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null bandoffsets");
            System.out.println(t);
        }

        try {
            
            DataBufferFloat dbFloat = new DataBufferFloat(20);
            Raster.createBandedRaster(dbFloat, 1, 1, 3,
                                      bankIndices, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for bad dataBuffer");
            System.out.println(t);
        }
    }

    
    static void interleavedRasterTests1() {

        System.out.println();
        System.out.println("** interleavedRasterTests1");

        Point p = new Point();

        try {
            
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                                            1, -1, 3, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        }

        try {
            
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                    Integer.MAX_VALUE/8, Integer.MAX_VALUE/8, 1, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
            
            Point pt = new Point(5, 1);
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                    Integer.MAX_VALUE-2, 1, 1, pt);
            noException();
        } catch (RasterFormatException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
            
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                                            1, 1, 0, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception zero bands");
            System.out.println(t);
        }

        try {
            
             Raster.createInterleavedRaster(DataBuffer.TYPE_INT,
                                                5, 1, 3, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for bad databuffer type");
            System.out.println(t);
        }
    }

     
    static void interleavedRasterTests2() {

        System.out.println();
        System.out.println("** interleavedRasterTests2 ");

        Point p = new Point();

        try {
            
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                                       1, -1, 3, 1, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        }

        try {
            
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                    Integer.MAX_VALUE/8, Integer.MAX_VALUE/8,
                    Integer.MAX_VALUE/2 , 1,
                    bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
            
            Point pt = new Point(5, 1);
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                    Integer.MAX_VALUE-2, 1,
                    Integer.MAX_VALUE, 1, bandOffsets, pt);
            noException();
        } catch (RasterFormatException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
             
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                                      1, 1, -3, 1, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                "Got expected exception for negative scanline stride");
            System.out.println(t);
        }

        try {
             
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                                      1, 1, 3, -1, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for pixelStride < 0");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
            checkIsOldVersion(t);
            System.out.println(
                   "Got expected exception for pixelStride < 0");
            System.out.println(t);
        }

        try {
            
            Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                                  1, 1, 0, 1, null, null);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null bandoffsets");
            System.out.println(t);
        }

        try {
            
            Raster.createInterleavedRaster(DataBuffer.TYPE_INT,
                                        5, 1, 3, 1, bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for bad databuffer type");
            System.out.println(t);
        }
    }

     
    static void interleavedRasterTests3() {

        System.out.println();
        System.out.println("** interleavedRasterTests3 ");

        Point p = new Point();

        try {
            
            Raster.createInterleavedRaster(dBuffer, 1, -1, 3, 1,
                                      bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for width <= 0");
            System.out.println(t);
        }

        try {
            
            Raster.createInterleavedRaster(dBuffer,
                    Integer.MAX_VALUE/8, Integer.MAX_VALUE/8,
                     Integer.MAX_VALUE/4, 1,
                    bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
            
            Point pt = new Point(5, 1);
            Raster.createInterleavedRaster(dBuffer,
                    Integer.MAX_VALUE-2, 1,
                    Integer.MAX_VALUE, 1, bandOffsets, pt);
            noException();
        } catch (RasterFormatException t) {
            System.out.println("Got expected exception for overflow");
            System.out.println(t);
        }

        try {
             
            Raster.createInterleavedRaster(dBuffer, 5, 1, -15, 1,
                                      bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                "Got expected exception for negative scanline stride");
            System.out.println(t);
        }

        try {
             
            Raster.createInterleavedRaster(dBuffer, 5, 1, 15, -1,
                                      bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for pixelStride < 0");
            System.out.println(t);
        } catch (NegativeArraySizeException t) {
if (t != null) throw t;
            checkIsOldVersion(t);
            System.out.println(
                   "Got expected exception for pixelStride < 0");
            System.out.println(t);
        }

        try {
            
            Raster.createInterleavedRaster(dBuffer, 5, 1, 15, 1,
                                      null, null);
            noException();
        } catch (NullPointerException t) {
            System.out.println(
                   "Got expected exception for null bandoffsets");
            System.out.println(t);
        }

        try {
            
            DataBufferFloat dbFloat = new DataBufferFloat(20);
            Raster.createInterleavedRaster(dbFloat, 5, 1, 15, 1,
                                      bandOffsets, null);
            noException();
        } catch (IllegalArgumentException t) {
            System.out.println(
                   "Got expected exception for bad databuffer type");
            System.out.println(t);
        }

        try {
            
            DataBufferByte dbb = new DataBufferByte(100, 2);
            Raster.createInterleavedRaster(dbb, 5, 1, 15, 1,
                                      bandOffsets, null);
            noException();
        } catch (RasterFormatException t) {
            System.out.println(
                   "Got expected exception for bad databuffer type");
            System.out.println(t);
        }
    }
}
