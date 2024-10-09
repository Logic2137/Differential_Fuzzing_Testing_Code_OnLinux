



import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

public class DisableCompressionTest {

    public static void main(String[] args) {
        testFormat("GIF");
    }

    protected static void testFormat(String format) {
        ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
        if (writer == null) {
            throw new RuntimeException("No writer for " + format);
        }

        ImageWriteParam param = writer.getDefaultWriteParam();
        int[] supported_modes = new int[] {
            ImageWriteParam.MODE_COPY_FROM_METADATA,
                    ImageWriteParam.MODE_DEFAULT,
                    ImageWriteParam.MODE_EXPLICIT };

        for (int mode : supported_modes) {
            String mode_name = getModeName(mode);
            System.out.println("Test mode " + mode_name + "...");
            
            
            
            param.setCompressionMode(mode);

            
            
            
            
            
            
            boolean gotException = false;
            try {
                param.setCompressionMode(ImageWriteParam.MODE_DISABLED);
            } catch (UnsupportedOperationException e) {
                gotException = true;
            } catch (Throwable e) {
                throw new RuntimeException("Test failed due to unexpected exception", e);
            }

            if (!gotException) {
                throw new RuntimeException("Test failed.");
            }

            if (param.getCompressionMode() != mode) {
                throw new RuntimeException("Param state was changed.");
            }
            System.out.println("Test passed.");
        }
    }

    private static String getModeName(int mode) {
        switch(mode) {
            case ImageWriteParam.MODE_COPY_FROM_METADATA:
                return "MODE_COPY_FROM_METADATA";
            case ImageWriteParam.MODE_DEFAULT:
                return "MODE_DEFAULT";
            case ImageWriteParam.MODE_DISABLED:
                return "MODE_DISABLED";
            case ImageWriteParam.MODE_EXPLICIT:
                return "MODE_EXPLICIT";
            default:
                throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }
}
