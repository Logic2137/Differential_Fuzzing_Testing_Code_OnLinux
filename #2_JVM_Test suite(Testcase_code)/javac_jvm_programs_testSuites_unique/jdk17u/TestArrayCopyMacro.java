
package compiler.arraycopy;

public class TestArrayCopyMacro {

    static class A {
    }

    static Object m2(Object o1, Object o2, int i) {
        if (i == 4) {
            return o1;
        }
        return o2;
    }

    static Object m1(A[] src, Object dest) {
        int i = 1;
        for (; i < 3; i *= 4) {
        }
        dest = m2(new A[10], dest, i);
        System.arraycopy(src, 0, dest, 0, 10);
        return dest;
    }

    public static void main(String[] args) {
        A[] array_src = new A[10];
        for (int i = 0; i < array_src.length; i++) {
            array_src[i] = new A();
        }
        for (int i = 0; i < 20000; i++) {
            m2(null, null, 0);
        }
        for (int i = 0; i < 20000; i++) {
            Object[] array_dest = (Object[]) m1(array_src, null);
            for (int j = 0; j < array_src.length; j++) {
                if (array_dest[j] != array_src[j]) {
                    throw new RuntimeException("copy failed at index " + j + " src = " + array_src[j] + " dest = " + array_dest[j]);
                }
            }
        }
    }
}
