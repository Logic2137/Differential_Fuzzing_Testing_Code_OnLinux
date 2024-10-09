



import java.awt.color.ICC_Profile;

public class LoadStandardProfilesTest {

    public static void main(String args[]) {
        try {
            ICC_Profile sRGB      = ICC_Profile.getInstance("sRGB.pf");
            ICC_Profile gray      = ICC_Profile.getInstance("GRAY.pf");
            ICC_Profile pycc      = ICC_Profile.getInstance("PYCC.pf");
            ICC_Profile ciexyz    = ICC_Profile.getInstance("CIEXYZ.pf");
            ICC_Profile linearRGB = ICC_Profile.getInstance("LINEAR_RGB.pf");

            if (sRGB == null ||
                gray == null ||
                pycc == null ||
                ciexyz == null ||
                linearRGB == null)
            {
                throw new RuntimeException("null profile.");
            }
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
}
