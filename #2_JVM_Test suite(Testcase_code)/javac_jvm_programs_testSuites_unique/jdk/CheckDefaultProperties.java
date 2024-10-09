

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;

import static java.awt.color.ColorSpace.TYPE_3CLR;
import static java.awt.color.ColorSpace.TYPE_GRAY;
import static java.awt.color.ColorSpace.TYPE_RGB;
import static java.awt.color.ColorSpace.TYPE_XYZ;
import static java.awt.color.ICC_Profile.CLASS_ABSTRACT;
import static java.awt.color.ICC_Profile.CLASS_COLORSPACECONVERSION;
import static java.awt.color.ICC_Profile.CLASS_DISPLAY;


public final class CheckDefaultProperties {

    public static void main(String[] args) {
        ICC_Profile srgb = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
        ICC_Profile gray = ICC_Profile.getInstance(ColorSpace.CS_GRAY);
        ICC_Profile xyz = ICC_Profile.getInstance(ColorSpace.CS_CIEXYZ);
        ICC_Profile lrgb = ICC_Profile.getInstance(ColorSpace.CS_LINEAR_RGB);
        ICC_Profile pycc = ICC_Profile.getInstance(ColorSpace.CS_PYCC);

        
        test(srgb, TYPE_RGB, 3, CLASS_DISPLAY);
        test(gray, TYPE_GRAY, 1, CLASS_DISPLAY);
        test(xyz, TYPE_XYZ, 3, CLASS_ABSTRACT);
        test(lrgb, TYPE_RGB, 3, CLASS_DISPLAY);
        test(pycc, TYPE_3CLR, 3, CLASS_COLORSPACECONVERSION);

        
        srgb.getData();
        gray.getData();
        xyz.getData();
        lrgb.getData();
        pycc.getData();

        
        test(srgb, TYPE_RGB, 3, CLASS_DISPLAY);
        test(gray, TYPE_GRAY, 1, CLASS_DISPLAY);
        test(xyz, TYPE_XYZ, 3, CLASS_ABSTRACT);
        test(lrgb, TYPE_RGB, 3, CLASS_DISPLAY);
        test(pycc, TYPE_3CLR, 3, CLASS_COLORSPACECONVERSION);
    }

    private static void test(ICC_Profile profile, int type, int num, int pcls) {
        int profileClass = profile.getProfileClass();
        int colorSpaceType = profile.getColorSpaceType();
        int numComponents = profile.getNumComponents();
        if (profileClass != pcls) {
            throw new RuntimeException("Wrong profile class: " + profileClass);
        }
        if (colorSpaceType != type) {
            throw new RuntimeException("Wrong profile type: " + colorSpaceType);
        }
        if (numComponents != num) {
            throw new RuntimeException("Wrong profile comps: " + numComponents);
        }
    }
}
