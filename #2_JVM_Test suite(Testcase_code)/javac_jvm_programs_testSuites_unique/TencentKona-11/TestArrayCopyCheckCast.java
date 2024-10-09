


public class TestArrayCopyCheckCast {

    static class Foo {}
    static class Bar {}

    public static void main(String[] args) throws Exception {
        try {
            Object[] array1 = new Object[1];
            array1[0] = new Bar();
            Foo[] array2 = new Foo[1];
            System.arraycopy(array1, 0, array2, 0, 1);
            throw new RuntimeException();
        } catch (ArrayStoreException ex) {
            
        }
    }

}
