



import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Bug7049339 {
    public static void main(String[] argv) {
        int x = 100, y = 100;
        BufferedImage src = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
        BufferedImage dst = new BufferedImage(x, y, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D dstg2d = dst.createGraphics();
        dstg2d.setComposite(new Composite() {
            @Override
            public CompositeContext createContext(
                    ColorModel srcColorModel,
                    ColorModel dstColorModel,
                    RenderingHints hints)
            {
                return new CompositeContext() {
                    @Override
                    public void compose(Raster src, Raster dstIn,
                            WritableRaster dstOut)
                    {
                        
                    }
                    @Override
                    public void dispose() {
                    }
                };
            }
        });
        Shape clip = new Ellipse2D.Double(x/4, y/4, x/2, y/2);
        dstg2d.setClip(clip);
        
        dstg2d.drawImage(src, 0, 0, null);
    }
}
