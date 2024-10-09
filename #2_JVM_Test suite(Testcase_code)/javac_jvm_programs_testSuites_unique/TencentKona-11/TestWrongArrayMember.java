



public class TestWrongArrayMember {
    public static void main(String... args) throws Exception {
        Object[] src = new Object[3];
        src[0] = new Integer(0);
        src[1] = new Object();
        src[2] = new Object();
        Object[] dst = new Integer[3];
        dst[0] = new Integer(1);
        dst[1] = new Integer(2);
        dst[2] = new Integer(3);
        try {
            System.arraycopy(src, 0, dst, 0, 3);
            throw new RuntimeException("Expected ArrayStoreException");
        } catch (ArrayStoreException e) {
            if (src[0] != dst[0]) {
                throw new RuntimeException("First element not copied");
            } else if (src[1] == dst[1] || src[2] == dst[2]) {
                throw new RuntimeException("Second and third elements are affected");
            } else {
                return; 
            }
        }
    }
}

