
package compiler.intrinsics.string;

public class TestStringCompareToDifferentLength {

    private final int size;

    public static void main(String[] args) {
        if (args.length > 1) {
            int maxLengthDelta = Integer.parseInt(args[0]);
            for (int i = 1; i < args.length; i++) {
                int size = Integer.parseInt(args[i]);
                TestStringCompareToDifferentLength test = new TestStringCompareToDifferentLength(size);
                for (int delta = 1; delta <= maxLengthDelta; delta++) {
                    test.testCompareTo(delta);
                }
            }
        } else {
            System.out.println("Usage: $testClass $maxLengthDelta $testLength [$testLength2 [$testLength3 [...]]]");
        }
    }

    private TestStringCompareToDifferentLength(int size) {
        this.size = size;
    }

    private void testCompareTo(int delta) {
        char[] strsrc = new char[size + delta];
        for (int i = 0; i < size + delta; i++) {
            strsrc[i] = (char) ('a' + (i % 26));
        }
        String longLatin1 = new String(strsrc);
        String shortLatin1 = longLatin1.substring(0, size);
        String longUTF16LastChar = longLatin1.substring(0, longLatin1.length() - 1) + '\uBEEF';
        String longUTF16FirstChar = '\uBEEF' + longLatin1.substring(1, longLatin1.length());
        String shortUTF16FirstChar = longUTF16FirstChar.substring(0, size);
        for (int i = 0; i < 10000; i++) {
            checkCase(longLatin1, shortLatin1, delta, "LL");
            checkCase(longUTF16LastChar, shortLatin1, delta, "UL");
            checkCase(longUTF16FirstChar, shortUTF16FirstChar, delta, "UU");
        }
    }

    private void checkCase(String str2, String str1, int expected, String caseName) {
        int result = str2.compareTo(str1);
        int reversedResult = str1.compareTo(str2);
        if (expected != result || result != -reversedResult) {
            throw new AssertionError(String.format("%s CASE FAILED: size = %d, " + "expected = %d, but got result = %d, " + "reversedResult = %d for string1 = '%s', string2 = '%s'", caseName, size, expected, result, reversedResult, str1, str2));
        }
    }
}
