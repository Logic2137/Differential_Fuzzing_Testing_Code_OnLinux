
package compiler.intrinsics.string;

import java.util.Arrays;

public class TestStringEqualsBadLength {

    int v1;

    int v2;

    boolean m(String s1) {
        int l = v2 - v1;
        char[] arr = new char[l];
        arr[0] = 'a';
        String s2 = new String(arr);
        return s2.equals(s1);
    }

    int m2(String s1) {
        int l = v2 - v1;
        char[] arr = new char[l + 1];
        arr[0] = 'a';
        arr[1] = 'b';
        String s2 = new String(arr);
        return s2.compareTo(s1);
    }

    boolean m3(char[] arr1) {
        int l = v2 - v1;
        char[] arr2 = new char[l];
        arr2[0] = 'a';
        return Arrays.equals(arr2, arr1);
    }

    static public void main(String[] args) {
        TestStringEqualsBadLength tse = new TestStringEqualsBadLength();
        tse.v1 = -1;
        tse.v2 = 0;
        char[] arr = new char[1];
        arr[0] = 'a';
        for (int i = 0; i < 20000; i++) {
            tse.m("a");
            tse.m2("ab");
            tse.m3(arr);
        }
        System.out.println("TEST PASSED");
    }
}
