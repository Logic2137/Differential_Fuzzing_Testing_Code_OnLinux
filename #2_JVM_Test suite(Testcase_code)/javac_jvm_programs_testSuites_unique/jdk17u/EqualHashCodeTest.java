import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;

public class EqualHashCodeTest {

    static DataFlavor df1, df2;

    static Insets insets1, insets2;

    static Dimension dim1, dim2;

    static ColorModel cm1, cm2;

    static int[] ColorModelBits = { 8, 8, 8, 8 };

    public static void main(String[] args) throws Exception {
        boolean passed = true;
        try {
            df1 = new DataFlavor("application/postscript");
            df2 = new DataFlavor("application/*");
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException("Could not create DataFlavors. This should never happen.");
        } catch (IllegalArgumentException e2) {
            passed = false;
        }
        if (df1.hashCode() != df2.hashCode()) {
            passed = false;
        }
        dim1 = new Dimension(3, 18);
        dim2 = new Dimension(3, 18);
        if (dim1.hashCode() != dim2.hashCode()) {
            passed = false;
        }
        insets1 = new Insets(3, 4, 7, 11);
        insets2 = new Insets(3, 4, 7, 11);
        if (insets1.hashCode() != insets2.hashCode()) {
            passed = false;
        }
        cm1 = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), ColorModelBits, true, true, Transparency.OPAQUE, 0);
        cm2 = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), ColorModelBits, true, true, Transparency.OPAQUE, 0);
        if (cm1.hashCode() != cm2.hashCode()) {
            passed = false;
        }
        if (!passed)
            throw new RuntimeException("Test FAILED");
    }
}
