

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;


public final class ICC_ProfileSetNullDataTest {

    public static void main(String[] args) {
        test(ICC_Profile.getInstance(ColorSpace.CS_sRGB));
        test(ICC_Profile.getInstance(ColorSpace.CS_LINEAR_RGB));
        test(ICC_Profile.getInstance(ColorSpace.CS_CIEXYZ));
        test(ICC_Profile.getInstance(ColorSpace.CS_PYCC));
        test(ICC_Profile.getInstance(ColorSpace.CS_GRAY));
    }

    private static void test(ICC_Profile profile) {
        byte[] tagData = null;
        try {
            profile.setData(ICC_Profile.icSigCmykData, tagData);
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new RuntimeException("IllegalArgumentException expected");
    }
}
