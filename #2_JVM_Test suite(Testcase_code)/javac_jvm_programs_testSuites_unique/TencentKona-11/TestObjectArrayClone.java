



package compiler.arraycopy;

public class TestObjectArrayClone {

    public static TestObjectArrayClone[] test(TestObjectArrayClone[] arr) {
        return arr.clone();
    }

    public static void main(String[] args) {
        test(new TestObjectArrayClone[42]);
    }
}

