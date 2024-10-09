

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.color.ICC_ProfileGray;
import java.awt.color.ProfileDataException;


public final class ICC_ProfileGrayTest {

    public static void main(String[] args) throws Exception {
        ICC_Profile csProfile = ICC_Profile.getInstance(ColorSpace.CS_GRAY);
        ICC_Profile dataProfile = ICC_Profile.getInstance(csProfile.getData());
        ICC_Profile stringProfile = ICC_Profile.getInstance("GRAY.pf");
        test(csProfile);
        test(dataProfile);
        test(stringProfile);
    }

    private static void test(ICC_Profile profile) {
        
        
        if (!(profile instanceof ICC_ProfileGray)
                || profile.getData(ICC_Profile.icSigMediaWhitePointTag) == null
                || profile.getData(ICC_Profile.icSigGrayTRCTag) == null) {
            throw new RuntimeException("Wrong profile: " + profile);
        }

        ICC_ProfileGray gray = (ICC_ProfileGray) profile;

        int length = gray.getMediaWhitePoint().length;
        if (length != 3) {
            throw new RuntimeException("Wrong data length: " + length);
        }

        
        boolean trc = false;
        try {
            gray.getTRC();
            trc = true;
            System.out.println("getTRC() works fine");
        } catch (ProfileDataException ignore) {
            gray.getGamma();
        }
        
        boolean gamma = false;
        try {
            gray.getGamma();
            gamma = true;
            System.out.println("getGamma() works fine");
        } catch (ProfileDataException ignore) {
            gray.getTRC();
        }

        if (gamma == trc) {
            
            throw new RuntimeException("Only one operation should work");
        }
    }
}
