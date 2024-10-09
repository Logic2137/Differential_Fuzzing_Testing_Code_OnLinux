import java.awt.color.ICC_Profile;
import java.awt.color.ICC_ProfileGray;
import java.awt.color.ICC_ProfileRGB;

public final class GetInstanceNullData {

    public static void main(String[] argv) {
        byte[] b = null;
        try {
            ICC_ProfileRGB p = (ICC_ProfileRGB) ICC_ProfileRGB.getInstance(b);
            throw new RuntimeException("IllegalArgumentException is expected");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            ICC_ProfileGray p = (ICC_ProfileGray) ICC_ProfileGray.getInstance(b);
            throw new RuntimeException("IllegalArgumentException is expected");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            ICC_Profile p = ICC_Profile.getInstance(b);
            throw new RuntimeException("IllegalArgumentException is expected");
        } catch (IllegalArgumentException ignored) {
        }
    }
}
