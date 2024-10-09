

import java.awt.color.ICC_Profile;


public final class GetInstanceBrokenData {

    public static void main(String[] argv) {
        byte b[] = {-21, -22, -23};
        try {
            ICC_Profile p = ICC_Profile.getInstance(b);
            throw new RuntimeException("IllegalArgumentException is expected");
        } catch (IllegalArgumentException ignored) {
            
        }
    }
}
