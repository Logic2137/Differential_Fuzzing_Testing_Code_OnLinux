


package compiler.floatingpoint;

public class NaNTest {
    static void testFloat() {
        int originalValue = 0x7f800001;
        int readBackValue = Float.floatToRawIntBits(Float.intBitsToFloat(originalValue));
        if (originalValue != readBackValue) {
            String errorMessage = String.format("Original and read back float values mismatch\n0x%X 0x%X\n",
                                                originalValue,
                                                readBackValue);
            throw new RuntimeException(errorMessage);
        } else {
            System.out.printf("Written and read back float values match\n0x%X 0x%X\n",
                              originalValue,
                              readBackValue);
        }
    }

    static void testDouble() {
        long originalValue = 0xFFF0000000000001L;
        long readBackValue = Double.doubleToRawLongBits(Double.longBitsToDouble(originalValue));
        if (originalValue != readBackValue) {
            String errorMessage = String.format("Original and read back double values mismatch\n0x%X 0x%X\n",
                                                originalValue,
                                                readBackValue);
            throw new RuntimeException(errorMessage);
        } else {
            System.out.printf("Written and read back double values match\n0x%X 0x%X\n",
                              originalValue,
                              readBackValue);
        }

    }

    public static void main(String args[]) {
        System.out.println("### NanTest started");

        testFloat();
        testDouble();

        System.out.println("### NanTest ended");
    }
}
