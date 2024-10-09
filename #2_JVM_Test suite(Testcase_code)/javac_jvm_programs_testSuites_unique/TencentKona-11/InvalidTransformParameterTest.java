



import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.image.RasterOp;
import java.awt.image.SampleModel;

public class InvalidTransformParameterTest {

    public static void main(String[] args) {
        int count = 0;
        final int testScenarios = 12;
        double NaNArg = 0.0 / 0.0;
        double positiveInfArg = 1.0 / 0.0;
        double negativeInfArg = -1.0 / 0.0;

        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);

        AffineTransform[] inputTransforms = new AffineTransform[testScenarios];

        for (int i = 0; i < inputTransforms.length; i++) {
            inputTransforms[i] = new AffineTransform();
        }

        inputTransforms[0].rotate(NaNArg, img.getWidth()/2, img.getHeight()/2);
        inputTransforms[1].translate(NaNArg, NaNArg);
        inputTransforms[2].scale(NaNArg, NaNArg);
        inputTransforms[3].shear(NaNArg, NaNArg);

        inputTransforms[4].rotate(positiveInfArg, img.getWidth()/2, img.getHeight()/2);
        inputTransforms[5].translate(positiveInfArg, positiveInfArg);
        inputTransforms[6].scale(positiveInfArg, positiveInfArg);
        inputTransforms[7].shear(positiveInfArg, positiveInfArg);

        inputTransforms[8].rotate(negativeInfArg, img.getWidth()/2, img.getHeight()/2);
        inputTransforms[9].translate(negativeInfArg, negativeInfArg);
        inputTransforms[10].scale(negativeInfArg, negativeInfArg);
        inputTransforms[11].shear(negativeInfArg, negativeInfArg);

        

        for (int i = 0; i < inputTransforms.length; i++) {
            try {
                testImageTransform(img, inputTransforms[i]);
            } catch (ImagingOpException ex) {
                count++;
            }
        }

        if (count != testScenarios) {
            throw new RuntimeException("Test failed. All test scenarios did not"
                                       + " result in exception as expected.");
        }

        

        count = 0;
        int[] bandOffsets = {0};
        Point location = new Point(0, 0);
        DataBuffer db = new DataBufferByte(10 * 10);
        SampleModel sm = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE,
                                                         10, 10, 1, 10,
                                                         bandOffsets);

        Raster src = Raster.createRaster(sm, db, location);
        WritableRaster dst = Raster.createWritableRaster(sm, db, location);

        for (int i = 0; i < inputTransforms.length; i++) {
            try {
                testRasterTransform(src, dst, inputTransforms[i]);
            } catch (ImagingOpException ex) {
                count++;
            }
        }

        if (count != testScenarios) {
            throw new RuntimeException("Test failed. All test scenarios did not"
                                       + " result in exception as expected.");
        }
    }

    public static BufferedImage testImageTransform(BufferedImage image,
                                                   AffineTransform transform) {
        AffineTransformOp op =
                new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage transformedImage = new BufferedImage(image.getWidth(),
                                                           image.getHeight(),
                                                           image.getType());

        return op.filter(image, transformedImage);
    }

    public static Raster testRasterTransform(Raster src, WritableRaster dst,
                                             AffineTransform transform) {
        AffineTransformOp op =
                new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        return op.filter(src, dst);
    }
}

