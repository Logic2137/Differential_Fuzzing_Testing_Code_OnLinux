

import java.awt.color.ColorSpace;


public final class GetNameExceptionTest {

    public static void main(String[] args) {
        test(ColorSpace.getInstance(ColorSpace.CS_sRGB));
        test(ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB));
        test(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ));
        test(ColorSpace.getInstance(ColorSpace.CS_PYCC));
        test(ColorSpace.getInstance(ColorSpace.CS_GRAY));
    }

    private static void test(ColorSpace cs) {
        try {
            cs.getName(cs.getNumComponents());
            throw new RuntimeException("Method ColorSpace.getName(int) should" +
                                       " throw exception for incorrect input");
        } catch (IllegalArgumentException ignored) {
            
        }
    }
}
