

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;


public final class StandardProfileTest {

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            throw new RuntimeException("SecurityManager is null");
        }

        int[] types = {
            ColorSpace.CS_CIEXYZ,
            ColorSpace.CS_GRAY,
            ColorSpace.CS_LINEAR_RGB,
            ColorSpace.CS_PYCC,
            ColorSpace.CS_sRGB } ;

        for (int t = 0; t<types.length; t++) {
            System.out.println("type " + t);
            ICC_Profile p = ICC_Profile.getInstance(types[t]);
            p.getPCSType();
        }
    }
}
