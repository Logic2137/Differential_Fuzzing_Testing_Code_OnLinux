
package compiler.arraycopy;

import java.util.Random;

public class TestArrayCopyDisjoint {

    public static final int SIZE = 4096;

    public static byte[] fromByteArr, toByteArr;

    public static char[] fromCharArr, toCharArr;

    public static int[] fromIntArr, toIntArr;

    public static long[] fromLongArr, toLongArr;

    static public void setup() {
        fromByteArr = new byte[SIZE];
        toByteArr = new byte[SIZE];
        fromCharArr = new char[SIZE];
        toCharArr = new char[SIZE];
        fromIntArr = new int[SIZE];
        toIntArr = new int[SIZE];
        fromLongArr = new long[SIZE];
        toLongArr = new long[SIZE];
        for (int i = 0; i < SIZE; i++) {
            fromByteArr[i] = (byte) i;
            fromCharArr[i] = (char) i;
            fromIntArr[i] = i;
            fromLongArr[i] = i;
        }
    }

    public static int validate_ctr = 0;

    public static <E> void validate(String msg, E arr, int length, int fromPos, int toPos) {
        validate_ctr++;
        if (arr instanceof byte[]) {
            byte[] barr = (byte[]) arr;
            for (int i = 0; i < length; i++) if (fromByteArr[i + fromPos] != barr[i + toPos]) {
                System.out.println(msg + "[" + arr.getClass() + "] Result mismtach at i = " + i + " expected = " + fromByteArr[i + fromPos] + " actual   = " + barr[i + toPos] + " fromPos = " + fromPos + " toPos = " + toPos);
                throw new Error("Fail");
            }
        } else if (arr instanceof char[]) {
            char[] carr = (char[]) arr;
            for (int i = 0; i < length; i++) if (fromCharArr[i + fromPos] != carr[i + toPos]) {
                System.out.println(msg + "[" + arr.getClass() + "] Result mismtach at i = " + i + " expected = " + fromCharArr[i + fromPos] + " actual   = " + carr[i + toPos] + " fromPos = " + fromPos + " toPos = " + toPos);
                throw new Error("Fail");
            }
        } else if (arr instanceof int[]) {
            int[] iarr = (int[]) arr;
            for (int i = 0; i < length; i++) if (fromIntArr[i + fromPos] != iarr[i + toPos]) {
                System.out.println(msg + "[" + arr.getClass() + "] Result mismtach at i = " + i + " expected = " + fromIntArr[i + fromPos] + " actual   = " + iarr[i + toPos] + " fromPos = " + fromPos + " toPos = " + toPos);
                throw new Error("Fail");
            }
        } else if (arr instanceof long[]) {
            long[] larr = (long[]) arr;
            for (int i = 0; i < length; i++) if (fromLongArr[i + fromPos] != larr[i + toPos]) {
                System.out.println(msg + "[" + arr.getClass() + "] Result mismtach at i = " + i + " expected = " + fromLongArr[i + fromPos] + " actual   = " + larr[i + toPos] + " fromPos = " + fromPos + " toPos = " + toPos);
                throw new Error("Fail");
            }
        }
    }

    public static void testByte(int length, int fromPos, int toPos) {
        System.arraycopy(fromByteArr, fromPos, toByteArr, toPos, length);
        validate(" Test ByteArr ", toByteArr, length, fromPos, toPos);
    }

    public static void testChar(int length, int fromPos, int toPos) {
        System.arraycopy(fromCharArr, fromPos, toCharArr, toPos, length);
        validate(" Test CharArr ", toCharArr, length, fromPos, toPos);
    }

    public static void testInt(int length, int fromPos, int toPos) {
        System.arraycopy(fromIntArr, fromPos, toIntArr, toPos, length);
        validate(" Test IntArr ", toIntArr, length, fromPos, toPos);
    }

    public static void testLong(int length, int fromPos, int toPos) {
        System.arraycopy(fromLongArr, fromPos, toLongArr, toPos, length);
        validate(" Test LongArr ", toLongArr, length, fromPos, toPos);
    }

    public static void testByte_constant_LT32B(int fromPos, int toPos) {
        System.arraycopy(fromByteArr, fromPos, toByteArr, toPos, 7);
        validate(" Test Byte constant length 7 ", toByteArr, 7, fromPos, toPos);
    }

    public static void testByte_constant_LT64B(int fromPos, int toPos) {
        System.arraycopy(fromByteArr, fromPos, toByteArr, toPos, 45);
        validate(" Test Byte constant length 45 ", toByteArr, 45, fromPos, toPos);
    }

    public static void testChar_constant_LT32B(int fromPos, int toPos) {
        System.arraycopy(fromCharArr, fromPos, toCharArr, toPos, 7);
        validate(" Test Char constant length 7 ", toCharArr, 7, fromPos, toPos);
    }

    public static void testChar_constant_LT64B(int fromPos, int toPos) {
        System.arraycopy(fromCharArr, fromPos, toCharArr, toPos, 22);
        validate(" Test Char constant length 22 ", toCharArr, 22, fromPos, toPos);
    }

    public static void testInt_constant_LT32B(int fromPos, int toPos) {
        System.arraycopy(fromIntArr, fromPos, toIntArr, toPos, 7);
        validate(" Test Int constant length 7 ", toIntArr, 7, fromPos, toPos);
    }

    public static void testInt_constant_LT64B(int fromPos, int toPos) {
        System.arraycopy(fromIntArr, fromPos, toIntArr, toPos, 11);
        validate(" Test Int constant length 11 ", toIntArr, 11, fromPos, toPos);
    }

    public static void testLong_constant_LT32B(int fromPos, int toPos) {
        System.arraycopy(fromLongArr, fromPos, toLongArr, toPos, 3);
        validate(" Test Long constant length 3 ", toLongArr, 3, fromPos, toPos);
    }

    public static void testLong_constant_LT64B(int fromPos, int toPos) {
        System.arraycopy(fromLongArr, fromPos, toLongArr, toPos, 6);
        validate(" Test Long constant length 6 ", toLongArr, 6, fromPos, toPos);
    }

    public static void main(String[] args) {
        int[] lengths = { 29, 59, 89, 125, 159, 189, 194, 1024 };
        Random r = new Random(1024);
        setup();
        try {
            for (int i = 0; i < 1000000; i++) {
                testByte(lengths[i % lengths.length], r.nextInt(2048), r.nextInt(2048));
                testByte_constant_LT32B(r.nextInt(2048), r.nextInt(2048));
                testByte_constant_LT64B(r.nextInt(2048), r.nextInt(2048));
                testChar(lengths[i % lengths.length] >> 1, r.nextInt(2048), r.nextInt(2048));
                testChar_constant_LT32B(r.nextInt(2048), r.nextInt(2048));
                testChar_constant_LT64B(r.nextInt(2048), r.nextInt(2048));
                testInt(lengths[i % lengths.length] >> 2, r.nextInt(2048), r.nextInt(2048));
                testInt_constant_LT32B(r.nextInt(2048), r.nextInt(2048));
                testInt_constant_LT64B(r.nextInt(2048), r.nextInt(2048));
                testLong(lengths[i % lengths.length] >> 3, r.nextInt(2048), r.nextInt(2048));
                testLong_constant_LT32B(r.nextInt(2048), r.nextInt(2048));
                testLong_constant_LT64B(r.nextInt(2048), r.nextInt(2048));
            }
            System.out.println("PASS : " + validate_ctr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
