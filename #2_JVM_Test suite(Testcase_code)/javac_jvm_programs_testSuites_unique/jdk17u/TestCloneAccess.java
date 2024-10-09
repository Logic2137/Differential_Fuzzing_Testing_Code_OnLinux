
package compiler.arraycopy;

public class TestCloneAccess {

    static int test(E src) throws CloneNotSupportedException {
        src.i1 = 3;
        E dest = (E) src.clone();
        dontInline(dest.i1, dest.i2);
        return dest.i1 + dest.i2;
    }

    public static void main(String[] args) throws Exception {
        E e = new E();
        e.i2 = 4;
        int res = 0;
        for (int i = 0; i < 20000; i++) {
            res = test(e);
            if (res != 7 || e.i1 != 3 || e.i2 != 4) {
                throw new RuntimeException("Wrong result! Expected: res = 7, e.i1 = 3, e.i2 = 4 " + "but got: res = " + res + ", e.i1 = " + e.i1 + ", e.i2 = " + e.i2);
            }
        }
    }

    public static void dontInline(int i1, int i2) {
    }
}

class E implements Cloneable {

    int i1;

    int i2;

    int i3;

    int i4;

    int i5;

    int i6;

    int i7;

    int i8;

    int i9;

    E() {
        i1 = 0;
        i2 = 1;
        i3 = 2;
        i4 = 3;
        i5 = 4;
        i6 = 5;
        i7 = 6;
        i8 = 7;
        i9 = 8;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
