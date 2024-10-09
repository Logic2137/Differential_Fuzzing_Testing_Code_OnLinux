


public class QualBoxedPostOp2<T> extends Parent2<Integer> {
    public static void main(String[] args) {
        new QualBoxedPostOp2().testAll();
    }

    private void testAll() {
        super.i = 10;

        equals(new Inner().testParent(), 10);
        equals(super.i, 11);
    }

    private void equals(int a, int b) {
        if (a != b) throw new Error();
    }

    T i;

    class Inner {
        private Integer testParent() {
            return QualBoxedPostOp2.super.i++;
        }
    }
}

class Parent2<T> {
    protected T i;
}
