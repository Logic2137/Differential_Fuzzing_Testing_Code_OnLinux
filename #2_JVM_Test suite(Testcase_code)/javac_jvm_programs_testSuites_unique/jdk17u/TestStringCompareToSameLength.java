
package compiler.intrinsics.string;

public class TestStringCompareToSameLength {

    private final int size;

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Usage: $testClass $testLength1" + " [$testLength2 [...]] | -fullmode $maxLength");
        }
        if (args.length == 2 && "-fullmode".equals(args[0])) {
            int maxLength = Integer.parseInt(args[1]);
            for (int length = 1; length <= maxLength; length++) {
                TestStringCompareToSameLength test = new TestStringCompareToSameLength(length);
                for (int mismatchIdx = 0; mismatchIdx <= length; mismatchIdx++) {
                    test.testCompareTo(mismatchIdx);
                }
            }
        } else {
            for (String arg : args) {
                int size = Integer.parseInt(arg);
                TestStringCompareToSameLength test = new TestStringCompareToSameLength(size);
                for (int mismatchIdx = 0; mismatchIdx <= size; mismatchIdx++) {
                    test.testCompareTo(mismatchIdx);
                }
            }
        }
    }

    private TestStringCompareToSameLength(int size) {
        this.size = size;
    }

    private void testCompareTo(int mismatchIdx) {
        char[] latinSrc = new char[size];
        for (int i = 0; i < size; i++) {
            latinSrc[i] = (char) ('a' + (i % 26));
        }
        String latinStr1 = new String(latinSrc);
        if (mismatchIdx != size)
            latinSrc[mismatchIdx] = (char) ('a' - 1);
        String latinStr2 = new String(latinSrc);
        char[] cArray = latinStr1.toCharArray();
        cArray[cArray.length - 1] = '\uBEEF';
        if (mismatchIdx != size)
            cArray[mismatchIdx] = '\u1234';
        String utfStr1 = new String(cArray);
        if (mismatchIdx != size)
            cArray[mismatchIdx] = '\u5678';
        String utfStr2 = new String(cArray);
        if (mismatchIdx != size)
            cArray[mismatchIdx] = (char) ('a' - 2);
        String utfStr3 = new String(cArray);
        for (int i = 0; i < 10000; i++) {
            checkCase(mismatchIdx, latinStr1, latinStr2, "LL");
            checkCase(mismatchIdx, utfStr1, utfStr2, "UU");
            if (size != mismatchIdx) {
                checkCase(mismatchIdx, latinStr1, utfStr1, "U(large)L");
                if (mismatchIdx != size - 1) {
                    checkCase(mismatchIdx, latinStr1, utfStr3, "U(small)L");
                }
            }
        }
    }

    private void checkCase(int mismatchIdx, String str1, String str2, String caseName) {
        int expected;
        if (mismatchIdx != size) {
            expected = str1.charAt(mismatchIdx) - str2.charAt(mismatchIdx);
        } else {
            expected = str1.length() - str2.length();
        }
        int result = str1.compareTo(str2);
        int reversedResult = str2.compareTo(str1);
        if (expected != result || result != -reversedResult) {
            throw new AssertionError(String.format("%s CASE FAILED: size = %d, " + "mismatchIdx = %d, expected = %d, but got result = %d, " + "reversedResult = %d for string1 = '%s', string2 = '%s'", caseName, size, mismatchIdx, expected, result, reversedResult, str1, str2));
        }
    }
}
