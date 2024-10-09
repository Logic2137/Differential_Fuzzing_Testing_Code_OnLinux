



import java.nio.CharBuffer;

public class StringRepeat {
    public static void main(String... arg) {
        test1();
        test2();
    }

    
    static int[] REPEATS = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
        32, 64, 128, 256, 512, 1024, 64 * 1024, 1024 * 1024,
        16 * 1024 * 1024
    };

    
    static String[] STRINGS = new String[] {
            "", "\0",  " ", "a", "$", "\u2022",
            "ab", "abc", "abcd", "abcde",
            "The quick brown fox jumps over the lazy dog."
    };

    
    static void test1() {
        for (int repeat : REPEATS) {
            for (String string : STRINGS) {
                long limit = (long)string.length() * (long)repeat;

                if ((long)(Integer.MAX_VALUE >> 1) <= limit) {
                    break;
                }

                verify(string.repeat(repeat), string, repeat);
            }
        }
    }

    
    static void test2() {
        try {
            "abc".repeat(-1);
            throw new RuntimeException("No exception for negative repeat count");
        } catch (IllegalArgumentException ex) {
            
        }

        try {
            "abc".repeat(Integer.MAX_VALUE - 1);
            throw new RuntimeException("No exception for large repeat count");
        } catch (OutOfMemoryError ex) {
            
        }
    }

    static String truncate(String string) {
        if (string.length() < 80) {
            return string;
        }
        return string.substring(0, 80) + "...";
    }

    
    static void verify(String result, String string, int repeat) {
        if (string.isEmpty() || repeat == 0) {
            if (!result.isEmpty()) {
                System.err.format("\"%s\".repeat(%d)%n", truncate(string), repeat);
                System.err.format("Result \"%s\"%n", truncate(result));
                System.err.format("Result expected to be empty, found string of length %d%n", result.length());
                throw new RuntimeException();
            }
        } else {
            int expected = 0;
            int count = 0;
            for (int offset = result.indexOf(string, expected);
                 0 <= offset;
                 offset = result.indexOf(string, expected)) {
                count++;
                if (offset != expected) {
                    System.err.format("\"%s\".repeat(%d)%n", truncate(string), repeat);
                    System.err.format("Result \"%s\"%n", truncate(result));
                    System.err.format("Repeat expected at %d, found at = %d%n", expected, offset);
                    throw new RuntimeException();
                }
                expected += string.length();
            }
            if (count != repeat) {
                System.err.format("\"%s\".repeat(%d)%n", truncate(string), repeat);
                System.err.format("Result \"%s\"%n", truncate(result));
                System.err.format("Repeat count expected to be %d, found %d%n", repeat, count);
                throw new RuntimeException();
            }
        }
    }
}
