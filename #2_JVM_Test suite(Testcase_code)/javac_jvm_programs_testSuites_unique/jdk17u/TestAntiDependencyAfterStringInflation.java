
package compiler.controldependency;

public class TestAntiDependencyAfterStringInflation {

    static String reverseString(String str) {
        int size = str.length();
        char[] buffer = new char[size];
        reverse(str, buffer, size);
        return new String(buffer, 0, size);
    }

    static void reverse(String str, char[] buffer, int size) {
        str.getChars(0, size, buffer, 0);
        int half = size / 2;
        for (int l = 0, r = size - 1; l < half; l++, r--) {
            char tmp = buffer[l];
            buffer[l] = buffer[r];
            buffer[r] = tmp;
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 50_000; i++) {
            String res = reverseString("0123456789");
            if (!res.equals("9876543210")) {
                throw new RuntimeException("Unexpected result: " + res);
            }
        }
    }
}
