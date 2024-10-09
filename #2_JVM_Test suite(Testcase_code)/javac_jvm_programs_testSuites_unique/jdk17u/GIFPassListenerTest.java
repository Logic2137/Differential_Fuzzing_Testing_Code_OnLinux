import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.event.*;
import javax.imageio.stream.*;

public class GIFPassListenerTest {

    private static ImageInputStream createTestImageStream(boolean progressive) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("gif");
        if (!writers.hasNext()) {
            return null;
        }
        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setProgressiveMode(progressive ? ImageWriteParam.MODE_DEFAULT : ImageWriteParam.MODE_DISABLED);
        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(output);
        writer.setOutput(imageOutput);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        writer.write(null, new IIOImage(image, null, null), param);
        imageOutput.flush();
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        return ImageIO.createImageInputStream(input);
    }

    private static void checkImage(boolean progressive) throws Exception {
        ImageInputStream iis = createTestImageStream(progressive);
        ImageReader reader = ImageIO.getImageReaders(iis).next();
        reader.setInput(iis);
        ReadUpdateHandler handler = new ReadUpdateHandler();
        reader.addIIOReadUpdateListener(handler);
        reader.readAll(null);
        if (handler.isPassStarted) {
            throw new RuntimeException("passStarted without passComplete.");
        }
        if (progressive && (handler.numPasses == 0)) {
            throw new RuntimeException("passStarted wasn't called for progressive image");
        }
        if (!progressive && (handler.numPasses != 0)) {
            throw new RuntimeException("passStarted was called for non-progressive image");
        }
        iis.close();
    }

    public static void main(String[] args) throws Exception {
        checkImage(true);
        checkImage(false);
    }

    private static class ReadUpdateHandler implements IIOReadUpdateListener {

        public boolean isPassStarted = false;

        public int numPasses = 0;

        @Override
        public void imageUpdate(ImageReader source, BufferedImage theImage, int minX, int minY, int width, int height, int periodX, int periodY, int[] bands) {
        }

        @Override
        public void passStarted(ImageReader source, BufferedImage theImage, int pass, int minPass, int maxPass, int minX, int minY, int periodX, int periodY, int[] bands) {
            if (isPassStarted) {
                throw new RuntimeException("reentered passStarted!");
            }
            isPassStarted = true;
            numPasses++;
        }

        @Override
        public void passComplete(ImageReader source, BufferedImage theImage) {
            if (!isPassStarted) {
                throw new RuntimeException("passComplete without passStarted!");
            }
            isPassStarted = false;
        }

        @Override
        public void thumbnailPassStarted(ImageReader source, BufferedImage theThumbnail, int pass, int minPass, int maxPass, int minX, int minY, int periodX, int periodY, int[] bands) {
        }

        @Override
        public void thumbnailPassComplete(ImageReader source, BufferedImage theThumbnail) {
        }

        @Override
        public void thumbnailUpdate(ImageReader source, BufferedImage theThumbnail, int minX, int minY, int width, int height, int periodX, int periodY, int[] bands) {
        }
    }
}
