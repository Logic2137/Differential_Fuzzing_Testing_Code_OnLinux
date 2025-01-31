



import java.awt.image.*;
import java.awt.color.*;
import java.awt.geom.AffineTransform;

public class ImagingOpsNoExceptionsTest {
    private static final String opsName[] = {
        "Threshold", "RescaleOp" ,"Invert", "Yellow Invert", "3x3 Blur",
        "3x3 Sharpen", "3x3 Edge", "5x5 Edge", "Color Convert", "Rotate"};
    private static BufferedImageOp biop[] = new BufferedImageOp[opsName.length];
    private static RasterOp rop[] = new RasterOp[opsName.length];
    private static int low = 100, high = 200;

    private static final int SIZE = 100;

    public static void runTest() {
        int exceptions = 0;
        for (int i = 0; i < opsName.length; i++) {
            
            
            for (int j = BufferedImage.TYPE_INT_RGB;
                 j <= BufferedImage.TYPE_INT_RGB; j++)
            {
                BufferedImage srcImage =
                    new BufferedImage(SIZE, SIZE, j);
                BufferedImage dstImage =
                    new BufferedImage(SIZE, SIZE, j);
                System.err.println("bi type="+j);
                System.err.println("  biop ="+opsName[i]);
                try {
                    biop[i].filter(srcImage, dstImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    exceptions++;
                }
                try {
                    biop[i].filter(srcImage, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    exceptions++;
                }

                
                
                if (! (rop[i] instanceof LookupOp)) {
                    System.err.println("  rop  ="+opsName[i]);
                    try {
                        rop[i].filter(srcImage.getRaster(),
                                      (WritableRaster)dstImage.getRaster());
                    } catch (Exception e) {
                        e.printStackTrace();
                        exceptions++;
                    }
                }
            }
        }

        if (exceptions > 0) {
            throw new RuntimeException("Test Failed, " + exceptions +
                                       " exceptions were thrown");
        }
        System.err.println("Test Passed, no exceptions were thrown.");
    }

    public static void thresholdOp(int low, int high) {
        byte threshold[] = new byte[256];
        for (int j = 0; j < 256 ; j++) {
            if (j > high) {
                threshold[j] = (byte) 255;
            } else if (j < low) {
                threshold[j] = (byte) 0;
            } else {
                threshold[j] = (byte) j;
            }
        }
        LookupOp lop = new LookupOp(new ByteLookupTable(0,threshold), null);
        biop[0] = lop;
        rop[0] = lop;
    }

    public static void main (String[] args) {
        thresholdOp(low, high);
        int i = 1;
        RescaleOp resop = new RescaleOp(1.0f, 0, null);
        biop[i] = resop;
        rop[i] = resop;
        i++;

        byte invert[] = new byte[256];
        byte ordered[] = new byte[256];
        for (int j = 0; j < 256 ; j++) {
            invert[j] = (byte) (256-j);
            ordered[j] = (byte) j;
        }
        LookupOp lop = new LookupOp(new ByteLookupTable(0,invert), null);
        biop[i] = lop;
        rop[i] = lop;
        i++;

        byte[][] yellowInvert = new byte[][] { invert, invert, ordered };
        lop = new LookupOp(new ByteLookupTable(0,yellowInvert), null);
        biop[i] = lop;
        rop[i] = lop;
        i++;
        int dim[][] = {{3,3}, {3,3}, {3,3}, {5,5}};
        float data[][] = { {0.1f, 0.1f, 0.1f,              
                            0.1f, 0.2f, 0.1f,
                            0.1f, 0.1f, 0.1f},
                           {-1.0f, -1.0f, -1.0f,           
                            -1.0f, 9.0f, -1.0f,
                            -1.0f, -1.0f, -1.0f},
                           { 0.f, -1.f,  0.f,                  
                             -1.f,  5.f, -1.f,
                             0.f, -1.f,  0.f},
                           {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f,  
                            -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                            -1.0f, -1.0f, 24.0f, -1.0f, -1.0f,
                            -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                            -1.0f, -1.0f, -1.0f, -1.0f, -1.0f}};
        for (int j = 0; j < data.length; j++, i++) {
            ConvolveOp cop = new ConvolveOp(new Kernel(dim[j][0],dim[j][1],data[j]));
            biop[i] = cop;
            rop[i] = cop;
        }

        ColorSpace cs1 = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorSpace cs2 = ColorSpace.getInstance(ColorSpace.CS_PYCC);
        ColorConvertOp ccop = new ColorConvertOp(cs1, cs2, null);
        biop[i] = ccop;
        rop[i] = ccop;
        i++;

        AffineTransform at =
            AffineTransform.getRotateInstance(0.5*Math.PI, SIZE/2, SIZE/2);
        AffineTransformOp atOp =
            new AffineTransformOp(at, null);
        biop[i] = atOp;
        rop[i] = atOp;

        runTest();
    }
}
