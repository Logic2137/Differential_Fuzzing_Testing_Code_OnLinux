



public class TestExpandedWBLostNullCheckDep {

    static void test(int i, int[] arr) {
        
        if (i < 0 || i >= arr.length) {
        }
        
        
        
        arr[i] = 0x42;
    }

    static public void main(String[] args) {
        int[] int_arr = new int[10];
        for (int i = 0; i < 20000; i++) {
            test(0, int_arr);
        }
        try {
            test(0, null);
        } catch (NullPointerException npe) {}
    }
}
