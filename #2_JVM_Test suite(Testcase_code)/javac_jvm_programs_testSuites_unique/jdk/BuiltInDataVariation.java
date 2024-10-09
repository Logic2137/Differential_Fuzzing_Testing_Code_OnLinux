

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.util.Arrays;
import java.util.Set;

import static java.awt.color.ColorSpace.*;


public final class BuiltInDataVariation {

    public static void main(String[] args) {
        testColorProfiles();
        testColorSpaces();
    }

    private static void testColorProfiles() {
        ICC_Profile srgb = ICC_Profile.getInstance(CS_sRGB);
        ICC_Profile lrgb = ICC_Profile.getInstance(CS_LINEAR_RGB);
        ICC_Profile xyz = ICC_Profile.getInstance(CS_CIEXYZ);
        ICC_Profile pycc = ICC_Profile.getInstance(CS_PYCC);
        ICC_Profile gray = ICC_Profile.getInstance(CS_GRAY);

        test(srgb, lrgb, xyz, pycc, gray);
        test(Arrays.hashCode(srgb.getData()), Arrays.hashCode(lrgb.getData()),
             Arrays.hashCode(xyz.getData()), Arrays.hashCode(pycc.getData()),
             Arrays.hashCode(gray.getData()));
    }

    private static void testColorSpaces() {
        var srgb = (ICC_ColorSpace) ColorSpace.getInstance(CS_sRGB);
        var lrgb = (ICC_ColorSpace) ColorSpace.getInstance(CS_LINEAR_RGB);
        var xyz = (ICC_ColorSpace) ColorSpace.getInstance(CS_CIEXYZ);
        var pycc = (ICC_ColorSpace) ColorSpace.getInstance(CS_PYCC);
        var gray = (ICC_ColorSpace) ColorSpace.getInstance(CS_GRAY);

        test(srgb, lrgb, xyz, pycc, gray);
        test(srgb.getProfile(), lrgb.getProfile(), xyz.getProfile(),
             pycc.getProfile(), gray.getProfile());
        test(Arrays.hashCode(srgb.getProfile().getData()),
             Arrays.hashCode(lrgb.getProfile().getData()),
             Arrays.hashCode(xyz.getProfile().getData()),
             Arrays.hashCode(pycc.getProfile().getData()),
             Arrays.hashCode(gray.getProfile().getData()));
    }

    private static void test(Object srgb, Object lrgb, Object xyz,
                             Object pycc, Object gray) {
        Set.of(srgb, lrgb, xyz, pycc, gray);
    }
}
