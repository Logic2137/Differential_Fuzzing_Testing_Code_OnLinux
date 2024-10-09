public class QualBoxedPostOp extends Parent {

    public static void main(String[] args) {
        new QualBoxedPostOp().testAll();
    }

    private void testAll() {
        equals(test(), 0);
        equals(i, 1);
        Inner in = new Inner();
        equals(in.test(), 1);
        equals(i, 2);
        equals(testParent(), 10);
        equals(super.i, 11);
        equals(in.testParent(), 11);
        equals(super.i, 12);
    }

    private void equals(int a, int b) {
        if (a != b)
            throw new Error();
    }

    Integer i = 0;

    private Integer test() {
        return this.i++;
    }

    private Integer testParent() {
        return super.i++;
    }

    class Inner {

        private Integer test() {
            return QualBoxedPostOp.this.i++;
        }

        private Integer testParent() {
            return QualBoxedPostOp.super.i++;
        }
    }
}

class Parent {

    protected Integer i = 10;
}
