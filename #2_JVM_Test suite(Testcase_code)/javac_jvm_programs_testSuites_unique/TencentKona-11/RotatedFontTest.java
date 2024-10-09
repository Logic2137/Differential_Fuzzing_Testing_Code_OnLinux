



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import static java.awt.RenderingHints.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.AttributedString;
import java.util.Collections;

import javax.imageio.ImageIO;

public class RotatedFontTest {

    static final String TEXT = "MMMM"; 
    static final RenderingHints.Key AA_KEY = KEY_TEXT_ANTIALIASING;
    static final Object AA_OFF = VALUE_TEXT_ANTIALIAS_OFF;
    static final RenderingHints.Key FM_KEY = KEY_FRACTIONALMETRICS;
    static final Object FM_ON = VALUE_FRACTIONALMETRICS_ON;
    static final Object FM_OFF = VALUE_FRACTIONALMETRICS_OFF;

    static final int DRAWSTRING = 0;
    static final int TEXTLAYOUT = 1;
    static final int GLYPHVECTOR = 2;
    static final int LAYEDOUT_GLYPHVECTOR = 3;

    public static void main(String... args) throws Exception {

        
        int x = 100;
        int y =  10;
        AffineTransform gtx = new AffineTransform();

        
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 20);
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("mac")) {
            
            font = new Font("Courier New", Font.PLAIN, 20);
        }
        System.out.println(font);
        AffineTransform at = AffineTransform.getRotateInstance(Math.PI / 2);
        at.scale(2.0, 1.5);
        Font rotFont = font.deriveFont(at);

        test(FM_OFF, x, y, rotFont, gtx, "font-rotation-fm-off.png");
        test(FM_ON, x, y, rotFont, gtx, "font-rotation-fm-on.png");

        
        gtx = at;
        x = 10;
        y = -100;
        test(FM_OFF, x, y, font, gtx, "gx-rotation-fm-off.png");
        test(FM_ON, x, y, font, gtx, "gx-rotation-fm-on.png");

        
        gtx = AffineTransform.getRotateInstance(Math.PI / 4);
        at = AffineTransform.getRotateInstance(Math.PI / 4);
        at.scale(2.0, 1.5);
        rotFont = font.deriveFont(at);
        x = 140;
        y = -100;
        test(FM_OFF, x, y, rotFont, gtx, "gx-and-font-rotation-fm-off.png");
        test(FM_ON, x, y, rotFont, gtx, "gx-and-font-rotation-fm-on.png");
    }

    static void test(Object fm, int x, int y, Font font,
                     AffineTransform gtx, String fileName) throws Exception {

        BufferedImage img = createNewImage();
        draw(img, DRAWSTRING, TEXT, x, y, font, gtx, fm);
        ImageIO.write(img, "png", new File(fileName));
        checkImageForRotation(img);
        BufferedImage imageCopy = copyImage(img);

        draw(img, TEXTLAYOUT, TEXT, x, y, font, gtx, fm);
        compareImages(imageCopy, img);

        draw(img, GLYPHVECTOR, TEXT, x, y, font, gtx, fm);
        compareImages(imageCopy, img);

    }

    private static BufferedImage createNewImage() {
        BufferedImage img = new BufferedImage(500, 500,
                                              BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
        g2d.setColor(Color.BLACK);
        g2d.dispose();
        return img;
    }

    private static void checkImageForRotation(BufferedImage img)
                       throws Exception {
     
        int firstRowWithBlackPixel = -1;
        int lastRowWithBlackPixel = -1;
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int rgb = img.getRGB(x, y);
                if ((rgb & 0xffffff) == 0) {
                    lastRowWithBlackPixel = y;
                    if (firstRowWithBlackPixel == -1) {
                        firstRowWithBlackPixel = y;
                    }
                }
            }
        }
        if ((firstRowWithBlackPixel == -1) ||
            (lastRowWithBlackPixel - firstRowWithBlackPixel < 40)) {
            ImageIO.write(img, "png", new File("font-rotation-failed.png"));
                 throw new RuntimeException("no rotation " +
                    "first = " + firstRowWithBlackPixel +
                    " last = " + lastRowWithBlackPixel);
        }
    }

    private static BufferedImage copyImage(BufferedImage origImg) {
        int w = origImg.getWidth(null);
        int h = origImg.getHeight(null);
        BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = newImg.createGraphics();
        g2d.drawImage(origImg, 0, 0, null);
        g2d.dispose();
        return newImg;
    }

    private static void compareImages(BufferedImage i1, BufferedImage i2)
            throws Exception {
        final int MAXDIFFS = 40;
        int maxDiffs = MAXDIFFS;
        int diffCnt = 0;
        boolean failed = false;
        int width = i1.getWidth(null);
        int height = i1.getHeight(null);
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
               if (maxDiffs == MAXDIFFS) {
                   int b1 = i1.getRGB(x, y) & 0x0ff;
                   int b2 = i2.getRGB(x, y) & 0x0ff;
                   
                   if ((b1 > 0 && b1 < 255) || (b2 > 0 && b2 < 255)) {
                       System.out.println("AA text, skip.");
                       return;
                   }
               }
               if (i1.getRGB(x, y) != i2.getRGB(x, y)) {
                   
                   diffCnt++;
                   if (diffCnt > maxDiffs) {
                       failed = true;
                   }
               }
            }
        }
        if (failed) {
            ImageIO.write(i2, "png", new File("font-rotation-failed.png"));
                 throw new RuntimeException("images differ, diffCnt="+diffCnt);
        }
    }

    private static void draw(BufferedImage img, int api, String s, int x, int y,
                             Font font, AffineTransform gtx, Object fm) {

        System.out.print("Font:" + font + " GTX:"+ gtx + " FM:" + fm + " using ");
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.black);
        g2d.transform(gtx);
        g2d.setRenderingHint(AA_KEY, AA_OFF);
        g2d.setRenderingHint(FM_KEY, fm);
        g2d.setFont(font);
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv;
        Rectangle2D bds = null;
        char[] chs;
        switch (api) {
            case DRAWSTRING:
                 System.out.println("drawString");
                 g2d.drawString(s, x, y);
                 chs = s.toCharArray();
                 bds = font.getStringBounds(chs, 0, chs.length, frc);
                 System.out.println("drawString Bounds="+bds);
                 break;
            case TEXTLAYOUT:
                 System.out.println("TextLayout");
                 TextLayout tl = new TextLayout(s, font, frc);
                 tl.draw(g2d, (float)x, (float)y);
                 System.out.println("TextLayout Bounds="+tl.getBounds());
                 System.out.println("TextLayout Pixel Bounds="+tl.getPixelBounds(frc, (float)x, (float)y));
                 break;
            case GLYPHVECTOR:
                 System.out.println("GlyphVector");
                 gv = font.createGlyphVector(frc, s);
                 g2d.drawGlyphVector(gv, (float)x, (float)y);
                 System.out.println("Default GlyphVector Logical Bounds="+gv.getLogicalBounds());
                 System.out.println("Default GlyphVector Visual Bounds="+gv.getVisualBounds());
                 System.out.println("Default GlyphVector Pixel Bounds="+gv.getPixelBounds(frc, (float)x, (float)y));
                 break;
            case LAYEDOUT_GLYPHVECTOR:
                 System.out.println("Layed out GlyphVector");
                 chs = s.toCharArray();
                 gv = font.layoutGlyphVector(frc, chs, 0, chs.length, 0);
                 g2d.drawGlyphVector(gv, (float)x, (float)y);
                 System.out.println("Layed out GlyphVector Logical Bounds="+gv.getLogicalBounds());
                 System.out.println("Layed out GlyphVector Visual Bounds="+gv.getVisualBounds());
                 System.out.println("Layed out GlyphVector Pixel Bounds="+gv.getPixelBounds(frc, (float)x, (float)y));
                 break;
            default: 
        }
        g2d.dispose();
    }

}
