



package compiler.arraycopy;

public class TestArrayCopyOverflowInBoundChecks {

    static byte[] src_array = { 'a', 'b', 'c', 'd', 'e' };

    static byte test(int copy_len) {
        byte[] dst_array = new byte[10];
        System.arraycopy(src_array, 0, dst_array, 1, copy_len);
        return dst_array[1];
    }

    static public void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            if (test(src_array.length - 1) != src_array[0]) {
                throw new RuntimeException("Test failed");
            }
        }
    }
}
