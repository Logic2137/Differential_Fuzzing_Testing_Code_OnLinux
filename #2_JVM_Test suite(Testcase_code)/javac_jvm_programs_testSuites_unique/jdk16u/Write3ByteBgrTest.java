



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Write3ByteBgrTest {
    private static int width = 100;
    private static int height = 100;
    private static Color color = new Color(0x10, 0x20, 0x30);

    static int bufferedImageType[] = {
        BufferedImage.TYPE_CUSTOM,
        BufferedImage.TYPE_BYTE_BINARY,
        BufferedImage.TYPE_3BYTE_BGR
    };

    static String bufferedImageStringType[] = {
        "BufferedImage.TYPE_CUSTOM: test for BandedSampleModel",
        "BufferedImage.TYPE_BYTE_BINARY",
        "BufferedImage.TYPE_3BYTE_BGR"
    };

    private static String writingFormat = "BMP";
    private static ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName(writingFormat).next();
    private int type;

    public static void main(String[] args) {

        
        for(int i=0; i<bufferedImageType.length; i++) {
            Write3ByteBgrTest t1 = new Write3ByteBgrTest(bufferedImageType[i]);

            System.out.println("\n\nImage test for type " + bufferedImageStringType[i]);
            t1.doImageTest();
        }
    }

    private Write3ByteBgrTest(int type) {
        this.type = type;
    }

    private void doImageTest() {
        try {
            BufferedImage src = createTestImage(type);
            BufferedImage dst = writeImage(src);

            compareImages(src, dst);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Test failed: index out of array bounds!");
        }
    }


    private void compareImages(BufferedImage src, BufferedImage dst) {
        Object dstPixel = dst.getRaster().getDataElements(width/2, height/2, null);
        Object srcPixel = src.getRaster().getDataElements(width/2, height/2, null);

        if ( (src.getColorModel().getRed(srcPixel) != dst.getColorModel().getRed(dstPixel)) ||
             (src.getColorModel().getGreen(srcPixel) != dst.getColorModel().getGreen(dstPixel)) ||
             (src.getColorModel().getBlue(srcPixel) != dst.getColorModel().getBlue(dstPixel)) ||
             (src.getColorModel().getAlpha(srcPixel) != dst.getColorModel().getAlpha(dstPixel)) ) {

            showPixel(src, width/2, height/2);
            showPixel(dst, width/2, height/2);

            showRes(dst, src);
            throw new RuntimeException(
                "Colors are different: " +
                Integer.toHexString(src.getColorModel().getRGB(srcPixel)) + " and " +
                Integer.toHexString(dst.getColorModel().getRGB(dstPixel)));
        }
    }

    private BufferedImage writeImage(BufferedImage src) {
        try {
            BufferedImage dst = null;
            if (!writer.getOriginatingProvider().canEncodeImage(src)) {
                throw new RuntimeException(writingFormat+" writer does not support the image type "+type);
            }
            System.out.println(writingFormat+" writer claims it can encode the image "+type);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
            writer.setOutput(ios);
            IIOImage img = new IIOImage(src.getRaster(), null, null);
            writer.write(img);
            ios.close();
            baos.close();

            
            File f = new File("test"+src.getType()+".bmp");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(baos.toByteArray());
            fos.close();


            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            dst = ImageIO.read(bais);
            return dst;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void showPixel(BufferedImage src, int x, int y) {
        System.out.println("Img is " + src);
        Object p = src.getRaster().getDataElements(x, y, null);
        System.out.println("RGB:   " +
                           Integer.toHexString(src.getColorModel().getRGB(p)));
        System.out.println("Red:   " +
                           Integer.toHexString(src.getColorModel().getRed(p)));
        System.out.println("Green: " +
                           Integer.toHexString(src.getColorModel().getGreen(p)));
        System.out.println("Blue:  " +
                           Integer.toHexString(src.getColorModel().getBlue(p)));
        System.out.println("Alpha: " +
                           Integer.toHexString(src.getColorModel().getAlpha(p)));
    }

    private static BufferedImage createTestImage(int type) {
        return createTestImage(type, color);
    }

    private static BufferedImage createTestImage(int type, Color c) {
        BufferedImage i = null;
        if (type == BufferedImage.TYPE_CUSTOM) {
            WritableRaster wr = Raster.createBandedRaster(
                DataBuffer.TYPE_BYTE,
                width, height,
                width,               
                new int[] { 0, 1, 2},
                new int[] { 1, 2, 0},
                null);

            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);

            ColorModel cm = new ComponentColorModel(cs,
                                                    new int[] { 8, 8, 8},
                                                    false,
                                                    false,
                                                    ColorModel.OPAQUE,
                                                    DataBuffer.TYPE_BYTE);
            i = new BufferedImage(cm, wr, false, null);
        } else {
            i = new BufferedImage(width, height, type);
        }

        Graphics2D g = i.createGraphics();

        g.setColor(c);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.white);
        g.drawRect(10,10, width-20, height-20);

        return i;
    }

    private static void showRes(final BufferedImage src,
                                final BufferedImage dst)
        {
        final int w = src.getWidth()+  dst.getWidth();
        final int h = Math.max(src.getHeight(), dst.getHeight());

        JFrame f = new JFrame("Test results");
        f.getContentPane().add( new JComponent() {
                public Dimension getPreferredSize() {
                    return new Dimension(w,h);
                }

                public void paintComponent(Graphics g) {
                    g.drawImage(src,0,0, null);
                    g.drawImage(dst, src.getWidth(),0, null);
                }
            });
        f.pack();
        f.setVisible(true);
    }
}
