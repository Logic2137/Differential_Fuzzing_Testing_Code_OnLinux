import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import static java.awt.color.ColorSpace.CS_CIEXYZ;
import static java.awt.color.ColorSpace.CS_GRAY;
import static java.awt.color.ColorSpace.CS_LINEAR_RGB;
import static java.awt.color.ColorSpace.CS_PYCC;
import static java.awt.color.ColorSpace.CS_sRGB;

public final class HotStaticLocks {

    public static void main(String[] args) throws Exception {
        testICCProfile();
        testColorSpace();
    }

    private static void testICCProfile() throws Exception {
        int[] spaces = { CS_sRGB, CS_LINEAR_RGB, CS_CIEXYZ, CS_PYCC, CS_GRAY };
        for (int cs : spaces) {
            synchronized (ICC_Profile.class) {
                Thread t = new Thread(() -> ICC_Profile.getInstance(cs));
                t.start();
                t.join();
            }
        }
    }

    private static void testColorSpace() throws Exception {
        int[] spaces = { CS_sRGB, CS_LINEAR_RGB, CS_CIEXYZ, CS_PYCC, CS_GRAY };
        for (int cs : spaces) {
            synchronized (ColorSpace.class) {
                Thread t = new Thread(() -> ColorSpace.getInstance(cs));
                t.start();
                t.join();
            }
        }
    }
}
