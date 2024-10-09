



import javax.imageio.plugins.tiff.TIFFField;
import javax.imageio.plugins.tiff.TIFFTag;

public class TIFFCreateArrayForTypeTest {

    static int count = 0;
    static boolean unknownDataType, negativeCount, zeroCount, countNotOne;
    static String errorMsg = "";

    private static void testCase1() {
        
        count = 2;
        int dataType = 15;
        try {
            TIFFField.createArrayForType(dataType, count);
        } catch (IllegalArgumentException e) {
            unknownDataType = true;
        } catch (Exception e) {
            
        }
        if (!unknownDataType) {
            errorMsg = errorMsg + "testCase1 ";
        }
    }

    private static void testCase2() {
        
        count = -1;
        try {
            TIFFField.createArrayForType(TIFFTag.TIFF_LONG, count);
        } catch (IllegalArgumentException e) {
            negativeCount = true;
        } catch (Exception e) {
            
        }
        if (!negativeCount) {
            errorMsg = errorMsg + "testCase2 ";
        }
    }

    private static void testCase3() {
        
        count = 0;
        try {
            TIFFField.createArrayForType(TIFFTag.TIFF_RATIONAL, count);
        } catch (IllegalArgumentException e) {
            zeroCount = true;
        } catch (Exception e) {
            
        }
        if (!zeroCount) {
            errorMsg = errorMsg + "testCase3 ";
        }
    }

    private static void testCase4() {
        
        count = 2;
        try {
            TIFFField.createArrayForType(TIFFTag.TIFF_IFD_POINTER, count);
        } catch (IllegalArgumentException e) {
            countNotOne = true;
        } catch (Exception e) {
            
        }
        if (!countNotOne) {
            errorMsg = errorMsg + "testCase4 ";
        }
    }

    public static void main(String[] args) {
        
        testCase1();
        testCase2();
        testCase3();
        testCase4();
        if ((!unknownDataType) ||
            (!negativeCount) ||
            (!zeroCount) ||
            (!countNotOne))
        {
            throw new RuntimeException(errorMsg + "is/are not throwing"
                    + " required IllegalArgumentException");
        }
    }
}

