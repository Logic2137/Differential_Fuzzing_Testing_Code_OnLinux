
package compiler.loopopts;

public class TestSplitIfOpaque1 {

    static class MyClass {

        int f;

        MyClass(int f) {
            this.f = f;
        }
    }

    static int test1(boolean b, int limit, MyClass obj) {
        int res = 0;
        MyClass notNull = new MyClass(42);
        if (b) {
            limit = 100;
        }
        if (b) {
            obj = notNull;
        }
        for (int i = 0; i < 1000; ++i) {
            res += obj.f;
            for (int j = 0; j <= limit; ++j) {
            }
        }
        return res;
    }

    static int test2(boolean b, int limit, MyClass obj, int[] array) {
        int res = 0;
        MyClass notNull = new MyClass(12);
        if (b) {
            limit = 100;
        }
        if (b) {
            obj = notNull;
        }
        for (int i = 0; i < 1000; ++i) {
            res += obj.f;
            for (int j = 0; j <= limit; ++j) {
                array[j] = j;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        MyClass obj = new MyClass(42);
        int[] array = new int[101];
        for (int i = 0; i < 20_000; i++) {
            test1(true, 50, obj);
            test1(false, 100, obj);
            test2(true, 50, obj, array);
            test2(false, 100, obj, array);
        }
    }
}
