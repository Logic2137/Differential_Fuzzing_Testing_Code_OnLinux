import java.awt.Color;
import java.awt.Transparency;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.SampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.DataBuffer;
import java.awt.color.ColorSpace;

public class ColCvtAlpha {

    public static void main(String[] args) {
        BufferedImage src = new BufferedImage(1, 10, BufferedImage.TYPE_INT_ARGB);
        Color pelColor = new Color(100, 100, 100, 128);
        for (int i = 0; i < 10; i++) {
            src.setRGB(0, i, pelColor.getRGB());
        }
        ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 8, 8 }, true, src.getColorModel().isAlphaPremultiplied(), Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        SampleModel sm = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, 100, 100, 2, 200, new int[] { 0, 1 });
        WritableRaster wr = Raster.createWritableRaster(sm, new Point(0, 0));
        BufferedImage dst = new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
        dst = dst.getSubimage(0, 0, 1, 10);
        ColorConvertOp op = new ColorConvertOp(null);
        op.filter(src, dst);
        for (int i = 0; i < 10; i++) {
            int rgb = (dst.getRGB(0, i) >> 24) & 0xff;
            if (rgb != 128) {
                throw new RuntimeException("Incorrect destination alpha value: " + rgb);
            }
        }
    }
}
