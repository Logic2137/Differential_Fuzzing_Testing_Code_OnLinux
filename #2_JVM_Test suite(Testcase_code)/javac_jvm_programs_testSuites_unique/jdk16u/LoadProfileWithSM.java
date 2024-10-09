

import java.awt.color.*;



public class LoadProfileWithSM {

    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());
        ICC_Profile profile =
            ((ICC_ColorSpace)(ColorSpace.getInstance(
                ColorSpace.CS_GRAY))).getProfile();
        
        profile.getData();
   }
}
