

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.color.ICC_ProfileRGB;
import java.awt.color.ProfileDataException;
import java.util.Arrays;


public final class ICC_ProfileRGBTest {

    public static void main(String[] args) throws Exception {
        ICC_Profile csProfile = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
        ICC_Profile dataProfile = ICC_Profile.getInstance(csProfile.getData());
        ICC_Profile string2Profile = ICC_Profile.getInstance("sRGB.pf");
        ICC_Profile string1Profile = ICC_Profile.getInstance("LINEAR_RGB.pf");
        test(csProfile);
        test(dataProfile);
        test(string1Profile);
        test(string2Profile);
    }

    private static void test(ICC_Profile profile) {
        
        
        
        if (!(profile instanceof ICC_ProfileRGB)
                || profile.getData(ICC_Profile.icSigRedColorantTag) == null
                || profile.getData(ICC_Profile.icSigGreenColorantTag) == null
                || profile.getData(ICC_Profile.icSigBlueColorantTag) == null
                || profile.getData(ICC_Profile.icSigRedTRCTag) == null
                || profile.getData(ICC_Profile.icSigGreenTRCTag) == null
                || profile.getData(ICC_Profile.icSigBlueTRCTag) == null
                || profile.getData(ICC_Profile.icSigMediaWhitePointTag) == null)
        {
            throw new RuntimeException("Wrong profile: " + profile);
        }

        ICC_ProfileRGB rgb = (ICC_ProfileRGB) profile;

        int length = rgb.getMediaWhitePoint().length;
        if (length != 3) {
            throw new RuntimeException("Wrong data length: " + length);
        }

        
        boolean trc = false;
        try {
            rgb.getTRC(ICC_ProfileRGB.REDCOMPONENT);
            rgb.getTRC(ICC_ProfileRGB.GREENCOMPONENT);
            rgb.getTRC(ICC_ProfileRGB.BLUECOMPONENT);
            trc = true;
            System.out.println("getTRC() works fine");
        } catch (ProfileDataException ignore) {
            rgb.getGamma(ICC_ProfileRGB.REDCOMPONENT);
            rgb.getGamma(ICC_ProfileRGB.GREENCOMPONENT);
            rgb.getGamma(ICC_ProfileRGB.BLUECOMPONENT);
        }
        
        boolean gamma = false;
        try {
            rgb.getGamma(ICC_ProfileRGB.REDCOMPONENT);
            rgb.getGamma(ICC_ProfileRGB.GREENCOMPONENT);
            rgb.getGamma(ICC_ProfileRGB.BLUECOMPONENT);
            gamma = true;
            System.out.println("getGamma() works fine");
        } catch (ProfileDataException ignore) {
            rgb.getTRC(ICC_ProfileRGB.REDCOMPONENT);
            rgb.getTRC(ICC_ProfileRGB.GREENCOMPONENT);
            rgb.getTRC(ICC_ProfileRGB.BLUECOMPONENT);
        }

        if (gamma == trc) {
            
            throw new RuntimeException("Only one operation should work");
        }

        
        try {
            rgb.getGamma(10);
            throw new RuntimeException("IllegalArgumentException was expected");
        } catch (IllegalArgumentException ignored) {}
        try {
            rgb.getGamma(-1);
            throw new RuntimeException("IllegalArgumentException was expected");
        } catch (IllegalArgumentException ignored) {}
        try {
            rgb.getTRC(10);
            throw new RuntimeException("IllegalArgumentException was expected");
        } catch (IllegalArgumentException ignored) {}
        try {
            rgb.getTRC(-1);
            throw new RuntimeException("IllegalArgumentException was expected");
        } catch (IllegalArgumentException ignored) {}

        
        float[][] matrix = rgb.getMatrix(); 
        float[][] old = getMatrix(rgb); 
        if (!Arrays.deepEquals(matrix, old)) {
            System.err.println("Expected: " + Arrays.deepToString(old));
            System.err.println("Actual: " + Arrays.deepToString(matrix));
            throw new RuntimeException("Wrong matrix");
        }
    }

    
    private static float[][] getMatrix(ICC_ProfileRGB profile) {
        float[] tmpMatrix = getXYZTag(profile, ICC_Profile.icSigRedColorantTag);
        float[][] theMatrix = new float[3][3];
        theMatrix[0][0] = tmpMatrix[0];
        theMatrix[1][0] = tmpMatrix[1];
        theMatrix[2][0] = tmpMatrix[2];
        tmpMatrix = getXYZTag(profile, ICC_Profile.icSigGreenColorantTag);
        theMatrix[0][1] = tmpMatrix[0];
        theMatrix[1][1] = tmpMatrix[1];
        theMatrix[2][1] = tmpMatrix[2];
        tmpMatrix = getXYZTag(profile, ICC_Profile.icSigBlueColorantTag);
        theMatrix[0][2] = tmpMatrix[0];
        theMatrix[1][2] = tmpMatrix[1];
        theMatrix[2][2] = tmpMatrix[2];
        return theMatrix;
    }

    private static float[] getXYZTag(ICC_ProfileRGB profile, int theTagSignature) {
        byte[] theData = profile.getData(theTagSignature);
        float[] theXYZNumber = new float[3];
        for (int i1 = 0, i2 = profile.icXYZNumberX; i1 < 3; i1++, i2 += 4) {
            int theS15Fixed16 = intFromBigEndian(theData, i2);
            theXYZNumber [i1] = theS15Fixed16 / 65536.0f;
        }
        return theXYZNumber;
    }

    static int intFromBigEndian(byte[] array, int index) {
        return (((array[index]   & 0xff) << 24) |
                ((array[index+1] & 0xff) << 16) |
                ((array[index+2] & 0xff) <<  8) |
                (array[index+3] & 0xff));
    }
}
