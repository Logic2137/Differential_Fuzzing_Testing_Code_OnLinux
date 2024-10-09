



import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

public class CompressionModeTest {

    public static void main(String args[]) {
        int[] iModes = { ImageWriteParam.MODE_DISABLED,
                         ImageWriteParam.MODE_EXPLICIT,
                         ImageWriteParam.MODE_COPY_FROM_METADATA,
                         ImageWriteParam.MODE_DEFAULT };

        String[] strModes = { "ImageWriteParam.MODE_DISABLED",
                              "ImageWriteParam.MODE_EXPLICIT",
                              "ImageWriteParam.MODE_COPY_FROM_METADATA",
                              "ImageWriteParam.MODE_DEFAULT" };

        for(int i=0; i<iModes.length; i++) {
            System.out.println("Test compression mode "+strModes[i]);
            doTest(iModes[i]);
        }
    }

    private static void doTest(int mode) {
        String fileFormat = "bmp";
        try {
            ImageWriter iw = (ImageWriter)ImageIO.getImageWritersBySuffix(fileFormat).next();
            if(iw == null) {
                throw new RuntimeException("No available image writer for "
                                           + fileFormat
                                           + " Test failed.");
            }

            File file = new File("image." + fileFormat);
            ImageOutputStream ios = ImageIO.createImageOutputStream(file);
            iw.setOutput(ios);

            BufferedImage bimg = new BufferedImage(100,
                                                   100, BufferedImage.TYPE_INT_RGB);
            Graphics g = bimg.getGraphics();
            g.setColor(Color.green);
            g.fillRect(0,0,100,100);

            ImageWriteParam param = iw.getDefaultWriteParam();

            param.setCompressionMode(mode);

            IIOMetadata meta = iw.getDefaultImageMetadata(new ImageTypeSpecifier(bimg),
                                                          param);

            IIOImage iioImg = new IIOImage(bimg, null, meta);
            iw.write(null, iioImg, param);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed.");
        }
    }
}
