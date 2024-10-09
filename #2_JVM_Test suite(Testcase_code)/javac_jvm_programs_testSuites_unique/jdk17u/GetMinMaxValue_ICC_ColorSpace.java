import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.IOException;

public class GetMinMaxValue_ICC_ColorSpace {

    public static void main(String[] a) throws Exception {
        ICC_Profile cmyk_profile = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
        ICC_ColorSpace colorSpace = new ICC_ColorSpace(cmyk_profile);
        String minstr = null;
        String maxstr = null;
        colorSpace.fromRGB(new float[] { 4.3f, 3.1f, 2.2f });
        try {
            System.out.println("minvalue " + colorSpace.getMinValue(3));
        } catch (IllegalArgumentException iae) {
            minstr = iae.toString();
        }
        try {
            System.out.println("maxvalue " + colorSpace.getMaxValue(3));
        } catch (IllegalArgumentException iae) {
            maxstr = iae.toString();
        }
        if (minstr.endsWith("+ component") || maxstr.endsWith("+ component")) {
            System.out.println("Test failed");
            throw new RuntimeException("IllegalArgumentException contains incorrect text message");
        } else {
            System.out.println("Test passed");
        }
    }
}
