



import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;

public class ComponentColorModelEqualsTest {

    private static void verifyEquals(ComponentColorModel m1,
                                     ComponentColorModel m2) {
        if (m1.equals(null)) {
            throw new RuntimeException("equals(null) returns true");
        }
        if (!(m1.equals(m2))) {
            throw new RuntimeException("equals() method is not working"
                    + " properly");
        }
        if (!(m2.equals(m1))) {
            throw new RuntimeException("equals() method is not working"
                    + " properly");
        }
        if (m1.hashCode() != m2.hashCode()) {
            throw new RuntimeException("HashCode is not same for same"
                    + " ComponentColorModels");
        }
    }

    private static void testConstructor1() {
        
        ComponentColorModel model1 =
            new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                    new int[] {8, 8, 8},
                                    false,
                                    false,
                                    Transparency.OPAQUE,
                                    DataBuffer.TYPE_BYTE);
        ComponentColorModel model2 =
            new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                    new int[] {8, 8, 8},
                                    false,
                                    false,
                                    Transparency.OPAQUE,
                                    DataBuffer.TYPE_BYTE);
        verifyEquals(model1, model2);
    }

    private static void testConstructor2() {
        
        ComponentColorModel model1 =
            new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                    false,
                                    false,
                                    Transparency.OPAQUE,
                                    DataBuffer.TYPE_BYTE);
        ComponentColorModel model2 =
            new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                    false,
                                    false,
                                    Transparency.OPAQUE,
                                    DataBuffer.TYPE_BYTE);
        verifyEquals(model1, model2);
    }

    private static void testSameComponentColorModel() {
        testConstructor1();
        testConstructor2();
    }
    public static void main(String[] args) {
        
        testSameComponentColorModel();
    }
}

