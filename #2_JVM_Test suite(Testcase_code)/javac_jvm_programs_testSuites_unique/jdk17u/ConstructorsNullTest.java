import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.ColorConvertOp;

public class ConstructorsNullTest {

    public static void main(final String[] args) {
        ColorConvertOp gp;
        boolean passed = false;
        try {
            gp = new ColorConvertOp((ColorSpace) null, (RenderingHints) null);
        } catch (NullPointerException e) {
            try {
                gp = new ColorConvertOp((ColorSpace) null, null, null);
            } catch (NullPointerException e1) {
                try {
                    gp = new ColorConvertOp((ICC_Profile[]) null, null);
                } catch (NullPointerException e2) {
                    passed = true;
                }
            }
        }
        if (!passed) {
            System.out.println("Test FAILED: one of constructors didn't throw NullPointerException.");
            throw new RuntimeException("Test FAILED: one of constructors didn't throw NullPointerException.");
        }
        System.out.println("Test PASSED: all constructors threw NullPointerException.");
    }
}
