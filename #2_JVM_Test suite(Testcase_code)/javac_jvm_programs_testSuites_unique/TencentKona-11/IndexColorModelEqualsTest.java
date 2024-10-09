



import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.math.BigInteger;

public class IndexColorModelEqualsTest {

    private static void verifyEquals(IndexColorModel m1,
                                     IndexColorModel m2) {
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
                    + " IndexColorModels");
        }
    }

    private static void testColorMapEquality() {
        
        IndexColorModel model1 = new IndexColorModel(8, 3, new int[] {1, 2, 3},
                0, true, -1, DataBuffer.TYPE_BYTE);
        IndexColorModel model2 = new IndexColorModel(8, 3, new int[] {4, 5, 6},
                0, true, -1, DataBuffer.TYPE_BYTE);
        if (model1.equals(model2)) {
            throw new RuntimeException("equals() method is determining"
                    + " ColorMap equality improperly");
        }
        if (model2.equals(model1)) {
            throw new RuntimeException("equals() method is determining"
                    + " ColorMap equality improperly");
        }
    }

    private static void testSizeEquality() {
        
        IndexColorModel model1 = new IndexColorModel(8, 4,
                new int[] {1, 2, 3, 4},
                0, true, -1, DataBuffer.TYPE_BYTE);
        IndexColorModel model2 = new IndexColorModel(8, 3,
                new int[] {1, 2, 3},
                0, true, -1, DataBuffer.TYPE_BYTE);
        if (model1.equals(model2)) {
            throw new RuntimeException("equals() method is determining"
                    + " Map size equality improperly");
        }
        if (model2.equals(model1)) {
            throw new RuntimeException("equals() method is determining"
                    + " Map size equality improperly");
        }
    }

    private static void testTransparentIndexEquality() {
        
        IndexColorModel model1 = new IndexColorModel(8, 3, new int[] {1, 2, 3},
                0, true, 1, DataBuffer.TYPE_BYTE);
        IndexColorModel model2 = new IndexColorModel(8, 3, new int[] {1, 2, 3},
                0, true, 2, DataBuffer.TYPE_BYTE);
        if (model1.equals(model2)) {
            throw new RuntimeException("equals() method is determining"
                    + " TransparentIndex equality improperly");
        }
        if (model2.equals(model1)) {
            throw new RuntimeException("equals() method is determining"
                    + " TransparentIndex equality improperly");
        }
    }

    private static void testValidPixelsEquality() {
        
        
        int color = 16777216;
        IndexColorModel model1 = new IndexColorModel(8, 3, new int[] {color,
                color, color}, 0, DataBuffer.TYPE_BYTE, new BigInteger("1"));
        IndexColorModel model2 = new IndexColorModel(8, 3, new int[] {color,
                color, color}, 0, DataBuffer.TYPE_BYTE, new BigInteger("2"));
        if (model1.equals(model2)) {
            throw new RuntimeException("equals() method is determining"
                    + " Valid pixels equality improperly");
        }
        if (model2.equals(model1)) {
            throw new RuntimeException("equals() method is determining"
                    + " Valid pixels equality improperly");
        }
    }

    private static void testConstructor1() {
        
        IndexColorModel model1 = new IndexColorModel(8, 2,
                new byte[] {1, 2}, new byte[] {1, 2}, new byte[] {1, 2});
        IndexColorModel model2 = new IndexColorModel(8, 2,
                new byte[] {1, 2}, new byte[] {1, 2}, new byte[] {1, 2});
        verifyEquals(model1, model2);
    }

    private static void testConstructor2() {
        
        IndexColorModel model1 = new IndexColorModel(8, 2, new byte[] {1, 2},
                new byte[] {1, 2}, new byte[] {1, 2}, new byte[] {1, 2});
        IndexColorModel model2 = new IndexColorModel(8, 2, new byte[] {1, 2},
                new byte[] {1, 2}, new byte[] {1, 2}, new byte[] {1, 2});
        verifyEquals(model1, model2);
    }

    private static void testConstructor3() {
        
        IndexColorModel model1 = new IndexColorModel(8, 2, new byte[] {1, 2},
                new byte[] {1, 2}, new byte[] {1, 2}, 1);
        IndexColorModel model2 = new IndexColorModel(8, 2, new byte[] {1, 2},
                new byte[] {1, 2}, new byte[] {1, 2}, 1);
        verifyEquals(model1, model2);
    }

    private static void testConstructor4() {
        
        IndexColorModel model1 = new IndexColorModel(8, 1,
                new byte[] {1, 2, 3, 4}, 0, true);
        IndexColorModel model2 = new IndexColorModel(8, 1,
                new byte[] {1, 2, 3, 4}, 0, true);
        verifyEquals(model1, model2);
    }

    private static void testConstructor5() {
        
        IndexColorModel model1 = new IndexColorModel(8, 1,
                new byte[] {1, 2, 3, 4}, 0, true, 0);
        IndexColorModel model2 = new IndexColorModel(8, 1,
                new byte[] {1, 2, 3, 4}, 0, true, 0);
        verifyEquals(model1, model2);
    }

    private static void testConstructor6() {
        
        IndexColorModel model1 = new IndexColorModel(8, 3, new int[] {1, 2, 3},
                0, true, -1, DataBuffer.TYPE_BYTE);
        IndexColorModel model2 = new IndexColorModel(8, 3, new int[] {1, 2, 3},
                0, true, -1, DataBuffer.TYPE_BYTE);
        verifyEquals(model1, model2);
    }

    private static void testConstructor7() {
        
        
        int color = 16777216;
        IndexColorModel model1 = new IndexColorModel(8, 3, new int[] {color,
                color, color}, 0, DataBuffer.TYPE_BYTE, new BigInteger("1"));
        IndexColorModel model2 = new IndexColorModel(8, 3, new int[] {color,
                color, color}, 0, DataBuffer.TYPE_BYTE, new BigInteger("1"));
        verifyEquals(model1, model2);
    }

    private static void testSameIndexColorModel() {
        testConstructor1();
        testConstructor2();
        testConstructor3();
        testConstructor4();
        testConstructor5();
        testConstructor6();
        testConstructor7();
    }
    public static void main(String[] args) {
        
        testColorMapEquality();
        testSizeEquality();
        testTransparentIndexEquality();
        testValidPixelsEquality();
        
        testSameIndexColorModel();
    }
}

