
package compiler.c2;

public class Test7017746 {

    int i;

    static int test(Test7017746 t, int a, int b) {
        int j = t.i;
        int x = a - b;
        if (a < b)
            x = x + j;
        return x - j;
    }

    public static void main(String[] args) {
        Test7017746 t = new Test7017746();
        for (int n = 0; n < 1000000; n++) {
            int i = test(t, 1, 2);
        }
    }
}
