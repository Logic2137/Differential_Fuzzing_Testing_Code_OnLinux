



import javax.imageio.ImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;

public class CompressionVals {

    public static void main(String[] args) {
        ImageWriteParam iwp = new JPEGImageWriteParam(null);
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        float[] vals = iwp.getCompressionQualityValues();
        String[] descs = iwp.getCompressionQualityDescriptions();
        if (vals.length != (descs.length + 1)) {
            throw new RuntimeException("Test failed: Values array is not " +
                                       "one larger than descriptions array");
        }
    }
}
