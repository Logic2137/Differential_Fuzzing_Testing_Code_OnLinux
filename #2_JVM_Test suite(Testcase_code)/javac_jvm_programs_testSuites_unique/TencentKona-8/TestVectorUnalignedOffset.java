




public class TestVectorUnalignedOffset {

    static void test1(int[] src_array, int[] dst_array, int l) {
        for (int i = 0; i < l; i++) {
            dst_array[i + 250] = src_array[i + 250];
        }
    }

    static void test2(byte[] src_array, byte[] dst_array, int l) {
        for (int i = 0; i < l; i++) {
            dst_array[i + 250] = src_array[i + 250];
        }
    }

    static public void main(String[] args) {
        int[] int_array = new int[1000];
        byte[] byte_array = new byte[1000];
        for (int i = 0; i < 20000; i++) {
            test1(int_array, int_array, int_array.length - 250);
            test2(byte_array, byte_array, byte_array.length - 250);
        }
    }
}
