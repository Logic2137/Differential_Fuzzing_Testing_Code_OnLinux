
import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.image.RasterFormatException;



public class OpenJDKFillBug
{
    

    public static void main(String args[])
    {
        BufferedImage bi = new BufferedImage(801,1202,
                                             BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        GeneralPath gp = new GeneralPath();
        AffineTransform m = new AffineTransform(2.483489907915543,
                                                0.0,
                                                0.0,
                                                -2.4844977263331955,
                                                0.0,
                                                1202.0);
        Composite c = new CustomComposite();

        gp.moveTo(-4.511, -14.349);
        gp.lineTo(327.489, -14.349);
        gp.lineTo(327.489, 494.15);
        gp.lineTo(-4.511, 494.15);
        gp.closePath();

        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                             RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                             RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                             RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST,
                             Integer.valueOf(140));
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING,
                             RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                             RenderingHints.VALUE_STROKE_NORMALIZE);
        g2d.setPaint(Color.red);
        g2d.setComposite(c);
        g2d.setTransform(m);
        try {
            g2d.fill(gp);
        } catch (RasterFormatException rfe) {
            System.out.println("Test failed");
            throw new RuntimeException("xmax/ymax rounding cause RasterFormatException: " + rfe);
        }
        g2d.dispose();
        System.out.println("Test passed");
    }

    

    

    public static class CustomComposite implements Composite
    {
        @Override
        public CompositeContext createContext(ColorModel srcColorModel,
                                              ColorModel dstColorModel,
                                              RenderingHints hints)
        {
            return new CustomCompositeContext();
        }

        

        

        public static class CustomCompositeContext implements CompositeContext
        {

            @Override
            public void dispose()
            {
                
            }

            @Override
            public void compose(Raster src,Raster dstIn,WritableRaster dstOut)
            {
                
            }
        }
    }
}
