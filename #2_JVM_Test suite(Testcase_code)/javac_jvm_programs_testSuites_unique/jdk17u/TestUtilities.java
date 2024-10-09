public class TestUtilities {

    public static boolean equalsBlock(byte[] b1, byte[] b2, int len) {
        for (int i = 0; i < len; i++) {
            if (b1[i] != b2[i]) {
                System.err.println("b1[" + i + "] : " + b1[i] + " b2[" + i + "] : " + b2[i]);
                return false;
            }
        }
        return true;
    }

    public static boolean equals(byte[] b1, byte[] b2) {
        if (b2.length != b1.length) {
            System.err.println("b1.length = " + b1.length + " b2.length = " + b2.length);
            return false;
        }
        return equalsBlock(b1, b2, b1.length);
    }

    public static boolean equalsBlockPartial(byte[] b1, byte[] b2, int b1BKSize, int b2BKSize) {
        int numOfBlock = b1.length / b1BKSize;
        for (int b = 0; b < numOfBlock; b++) {
            for (int i = 0; i < b2BKSize; i++) {
                int j1 = b * b1BKSize + i;
                int j2 = b * b2BKSize + i;
                if (b1[j1] != b2[j2]) {
                    System.err.println("Compare failed at b1[" + j1 + "]:" + b1[j1] + " b2[" + j2 + "]:" + b2[j2]);
                    return false;
                }
            }
        }
        return true;
    }

    public static byte[] generateBytes(int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) (i & 0xff);
        }
        return bytes;
    }
}
