



import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class ImageIOWriteFile {

    public static void main(String[] args) {
        long length0 = -1L;
        long length1 = -1L;

        try {
            BufferedImage bi =
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

            File outFile = File.createTempFile("imageiowritefile", ".tmp");

            
            outFile.delete();
            ImageIO.write(bi, "png", outFile);
            length0 = outFile.length();

            
            outFile.delete();
            FileOutputStream fos = new FileOutputStream(outFile);
            for (int i = 0; i < length0*2; i++) {
                fos.write(1);
            }
            fos.close();

            
            ImageIO.write(bi, "png", outFile);
            length1 = outFile.length();

            outFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception!");
        }

        if (length0 == 0) {
            throw new RuntimeException("File length is zero!");
        }
        if (length1 != length0) {
            throw new RuntimeException("File length changed!");
        }
    }
}
