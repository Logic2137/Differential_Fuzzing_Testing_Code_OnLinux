
package org.netbeans.jemmy.util;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;


public class PNGEncoder extends Object {

    
    public static final byte BW_MODE = 0;
    
    public static final byte GREYSCALE_MODE = 1;
    
    public static final byte COLOR_MODE = 2;

    OutputStream out;
    CRC32 crc;
    byte mode;

    
    public PNGEncoder(OutputStream out) {
        this(out, GREYSCALE_MODE);
    }

    
    public PNGEncoder(OutputStream out, byte mode) {
        crc = new CRC32();
        this.out = out;
        if (mode < 0 || mode > 2) {
            throw new IllegalArgumentException("Unknown color mode");
        }
        this.mode = mode;
    }

    void write(int i) throws IOException {
        byte b[] = {(byte) ((i >> 24) & 0xff), (byte) ((i >> 16) & 0xff), (byte) ((i >> 8) & 0xff), (byte) (i & 0xff)};
        write(b);
    }

    void write(byte b[]) throws IOException {
        out.write(b);
        crc.update(b);
    }

    
    public void encode(BufferedImage image) throws IOException {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        final byte id[] = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13};
        write(id);
        crc.reset();
        write("IHDR".getBytes());
        write(width);
        write(height);
        byte head[];
        switch (mode) {
            case BW_MODE:
                head = new byte[]{1, 0, 0, 0, 0};
                break;
            case GREYSCALE_MODE:
                head = new byte[]{8, 0, 0, 0, 0};
                break;
            case COLOR_MODE:
                head = new byte[]{8, 2, 0, 0, 0};
                break;
            default:
                throw new AssertionError("Unexpected mode: " + mode);
        }
        write(head);
        write((int) crc.getValue());
        ByteArrayOutputStream compressed = new ByteArrayOutputStream(65536);
        BufferedOutputStream bos = new BufferedOutputStream(new DeflaterOutputStream(compressed, new Deflater(9)));
        int pixel;
        int color;
        int colorset;
        switch (mode) {
            case BW_MODE:
                int rest = width % 8;
                int bytes = width / 8;
                for (int y = 0; y < height; y++) {
                    bos.write(0);
                    for (int x = 0; x < bytes; x++) {
                        colorset = 0;
                        for (int sh = 0; sh < 8; sh++) {
                            pixel = image.getRGB(x * 8 + sh, y);
                            color = ((pixel >> 16) & 0xff);
                            color += ((pixel >> 8) & 0xff);
                            color += (pixel & 0xff);
                            colorset <<= 1;
                            if (color >= 3 * 128) {
                                colorset |= 1;
                            }
                        }
                        bos.write((byte) colorset);
                    }
                    if (rest > 0) {
                        colorset = 0;
                        for (int sh = 0; sh < width % 8; sh++) {
                            pixel = image.getRGB(bytes * 8 + sh, y);
                            color = ((pixel >> 16) & 0xff);
                            color += ((pixel >> 8) & 0xff);
                            color += (pixel & 0xff);
                            colorset <<= 1;
                            if (color >= 3 * 128) {
                                colorset |= 1;
                            }
                        }
                        colorset <<= 8 - rest;
                        bos.write((byte) colorset);
                    }
                }
                break;
            case GREYSCALE_MODE:
                for (int y = 0; y < height; y++) {
                    bos.write(0);
                    for (int x = 0; x < width; x++) {
                        pixel = image.getRGB(x, y);
                        color = ((pixel >> 16) & 0xff);
                        color += ((pixel >> 8) & 0xff);
                        color += (pixel & 0xff);
                        bos.write((byte) (color / 3));
                    }
                }
                break;
            case COLOR_MODE:
                for (int y = 0; y < height; y++) {
                    bos.write(0);
                    for (int x = 0; x < width; x++) {
                        pixel = image.getRGB(x, y);
                        bos.write((byte) ((pixel >> 16) & 0xff));
                        bos.write((byte) ((pixel >> 8) & 0xff));
                        bos.write((byte) (pixel & 0xff));
                    }
                }
                break;
        }
        bos.close();
        write(compressed.size());
        crc.reset();
        write("IDAT".getBytes());
        write(compressed.toByteArray());
        write((int) crc.getValue());
        write(0);
        crc.reset();
        write("IEND".getBytes());
        write((int) crc.getValue());
        out.close();
    }

    
    public static void captureScreen(Rectangle rect, String fileName) {
        captureScreen(rect, fileName, GREYSCALE_MODE);
    }

    
    public static void captureScreen(Rectangle rect, String fileName, byte mode) {
        try {
            BufferedImage capture = new Robot().createScreenCapture(rect);
            BufferedOutputStream file = new BufferedOutputStream(new FileOutputStream(fileName));
            PNGEncoder encoder = new PNGEncoder(file, mode);
            encoder.encode(capture);
        } catch (AWTException awte) {
            awte.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    
    public static void captureScreen(Component comp, String fileName) {
        captureScreen(comp, fileName, GREYSCALE_MODE);
    }

    
    public static void captureScreen(Component comp, String fileName, byte mode) {
        captureScreen(new Rectangle(comp.getLocationOnScreen(),
                comp.getSize()),
                fileName, mode);
    }

    
    public static void captureScreen(String fileName) {
        captureScreen(fileName, GREYSCALE_MODE);
    }

    
    public static void captureScreen(String fileName, byte mode) {
        captureScreen(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()), fileName, mode);
    }
}
