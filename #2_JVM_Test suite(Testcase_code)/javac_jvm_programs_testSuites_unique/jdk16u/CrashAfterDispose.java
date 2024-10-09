



import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class CrashAfterDispose {

    public static void main(String[] args) throws IOException {
        InputStream bais = new ByteArrayInputStream(new byte[100]);
        ImageInputStream iis = ImageIO.createImageInputStream(bais);

        
        ImageReader reader = null;
        Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
        if (readers.hasNext()) {
            reader = (ImageReader)readers.next();
        } else {
            throw new RuntimeException("Unable to find a reader!");
        }

        
        
        
        reader.dispose();

        try {
            reader.setInput(iis);
        } catch (IllegalStateException e) {
        }

        try {
            reader.read(0);
        } catch (IllegalStateException e) {
        }

        try {
            reader.abort();
        } catch (IllegalStateException e) {
        }

        try {
            reader.reset();
        } catch (IllegalStateException e) {
        }

        try {
            reader.dispose();
        } catch (IllegalStateException e) {
        }

        
        ImageWriter writer = null;
        Iterator writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (writers.hasNext()) {
            writer = (ImageWriter)writers.next();
        } else {
            throw new RuntimeException("Unable to find a writer!");
        }

        
        OutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        BufferedImage bi = new BufferedImage(10, 10,
                                             BufferedImage.TYPE_INT_RGB);

        
        
        
        writer.dispose();

        try {
            writer.setOutput(ios);
        } catch (IllegalStateException e) {
        }

        try {
            writer.write(bi);
        } catch (IllegalStateException e) {
        }

        try {
            writer.abort();
        } catch (IllegalStateException e) {
        }

        try {
            writer.reset();
        } catch (IllegalStateException e) {
        }

        try {
            writer.dispose();
        } catch (IllegalStateException e) {
        }
    }
}
