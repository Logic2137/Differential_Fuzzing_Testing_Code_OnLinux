



import java.awt.color.CMMException;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.ColorConvertOp;
import java.awt.image.BufferedImage;

import static java.awt.color.ColorSpace.CS_sRGB;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

public class InvalidRenderIntentTest {

    public static void main(String[] args) {
        ICC_Profile pSRGB = ICC_Profile.getInstance(CS_sRGB);

        byte[] raw_data = pSRGB.getData();

        setRenderingIntent(0x1000000, raw_data);

        ICC_Profile p = ICC_Profile.getInstance(raw_data);

        ICC_ColorSpace cs = new ICC_ColorSpace(p);

        
        ColorConvertOp op = new ColorConvertOp(cs,
                ColorSpace.getInstance(CS_sRGB), null);
        BufferedImage src = new BufferedImage(1, 1, TYPE_3BYTE_BGR);
        BufferedImage dst = new BufferedImage(1, 1, TYPE_3BYTE_BGR);

        try {
            op.filter(src.getRaster(), dst.getRaster());
        } catch (CMMException e) {
            throw new RuntimeException("Test failed.", e);
        }
        System.out.println("Test passed.");
    }

    private static void setRenderingIntent(int intent, byte[] data) {
        final int pos = ICC_Profile.icHdrRenderingIntent;

        data[pos + 0] = (byte) (0xff & (intent >> 24));
        data[pos + 1] = (byte) (0xff & (intent >> 16));
        data[pos + 2] = (byte) (0xff & (intent >> 8));
        data[pos + 3] = (byte) (0xff & (intent));
    }
}
