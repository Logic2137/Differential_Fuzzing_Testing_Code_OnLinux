


public class TestArrayCopyToFromObject {

    public void test(Object aArray[]) {
        Object a = new Object();

        try {
            System.arraycopy(aArray, 0, a, 0, 1);
            throw new RuntimeException ("FAILED: Expected ArrayStoreException " +
                                        "(due to destination not being an array) " +
                                        "was not thrown");
        } catch (ArrayStoreException e) {
            System.out.println("PASSED: Expected ArrayStoreException was thrown");
        }

        try {
            System.arraycopy(a, 0, aArray, 0, 1);
            throw new RuntimeException ("FAILED: Expected ArrayStoreException " +
                                        "(due to source not being an array) " +
                                        "was not thrown");
        } catch (ArrayStoreException e) {
            System.out.println("PASSED: Expected ArrayStoreException was thrown");
        }

    }

    public static void main(String args[]) {
        System.out.println("TestArrayCopyToFromObject");
        Object aArray[] = new Object[10];
        for (int i = 0; i < 10; i++) {
            aArray[i] = new Object();
        }
        new TestArrayCopyToFromObject().test(aArray);
    }
}
