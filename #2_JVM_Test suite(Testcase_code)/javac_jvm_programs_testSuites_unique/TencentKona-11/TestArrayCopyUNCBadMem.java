




public class TestArrayCopyUNCBadMem {

    volatile static int field;

    static class unloaded {
        static int dummy;
    }

    static int test(int[] input) {
        int[] alloc = new int[10];
        System.arraycopy(input, 0, alloc, 0, 10);

        
        
        field = 0x42;

        
        unloaded.dummy = 0x42;

        return alloc[0] + alloc[1];
    }

    public static void main(String[] args) {
        int[] array = new int[10];
        System.arraycopy(array, 0, array, 0, 0); 
        test(array);
    }
}
