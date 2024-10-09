

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.util.Arrays;


public final class SetHeaderInfo {

    public static void main(String[] args) {
        int[] cspaces = {ColorSpace.CS_sRGB, ColorSpace.CS_LINEAR_RGB,
                         ColorSpace.CS_CIEXYZ, ColorSpace.CS_PYCC,
                         ColorSpace.CS_GRAY};
        for (int cspace : cspaces) {
            ICC_Profile icc = ICC_Profile.getInstance(cspace);
            testSame(icc);
            testCustom(icc);
            
            negative(icc, null);
            negative(icc, new byte[0]);
            negative(icc, new byte[1]);
            byte[] header = icc.getData(ICC_Profile.icSigHead);
            negative(icc, new byte[header.length - 1]);
        }
    }

    private static void testSame(ICC_Profile icc) {
        byte[] expected = icc.getData(ICC_Profile.icSigHead);
        icc.setData(ICC_Profile.icSigHead, expected);
        byte[] actual = icc.getData(ICC_Profile.icSigHead);
        if (!Arrays.equals(expected, actual)) {
            System.err.println("Expected: " + Arrays.toString(expected));
            System.err.println("Actual:   " + Arrays.toString(actual));
            throw new RuntimeException();
        }
    }

    private static void testCustom(ICC_Profile icc) {
        byte[] expected = icc.getData(ICC_Profile.icSigHead);
        
        expected[ICC_Profile.icHdrFlags + 3] = 1;
        expected[ICC_Profile.icHdrModel + 3] = 1;
        icc.setData(ICC_Profile.icSigHead, expected);
        byte[] actual = icc.getData(ICC_Profile.icSigHead);
        if (!Arrays.equals(expected, actual)) {
            System.err.println("Expected: " + Arrays.toString(expected));
            System.err.println("Actual:   " + Arrays.toString(actual));
            throw new RuntimeException();
        }
    }

    private static void negative(ICC_Profile icc, byte[] tagData) {
        try {
            icc.setData(ICC_Profile.icSigHead, tagData);
            throw new RuntimeException("IllegalArgumentException expected");
        } catch (IllegalArgumentException iae) {
            
        }
    }
}
