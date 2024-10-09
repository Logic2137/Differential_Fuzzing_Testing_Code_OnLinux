
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sun.font.Font2DHandle;
import sun.font.Font2D;
import sun.font.FontScaler;
import sun.font.Type1Font;


public class FontDisposeTest
{
    public static void main(String[] args) throws Exception
    {
        
        
        
        
        String path = args[0];

        
        InputStream stream = new FileInputStream(path);
        Font font = Font.createFont(Font.TYPE1_FONT,stream);

        
        BufferedImage img = new BufferedImage(100,100,
                                 BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        FontRenderContext frc = g2d.getFontRenderContext();

        font.getLineMetrics("derp",frc);

        
        
        Field font2DHandleField = Font.class.getDeclaredField("font2DHandle");
        font2DHandleField.setAccessible(true);
        sun.font.Font2DHandle font2DHandle =
                      (sun.font.Font2DHandle)font2DHandleField.get(font);

        sun.font.Font2D font2D = font2DHandle.font2D;
        sun.font.Type1Font type1Font = (sun.font.Type1Font)font2D;

        Method getScalerMethod =
        sun.font.Type1Font.class.getDeclaredMethod("getScaler");
        getScalerMethod.setAccessible(true);
        sun.font.FontScaler scaler =
                  (sun.font.FontScaler)getScalerMethod.invoke(type1Font);

        
        scaler.dispose();
    }
}
