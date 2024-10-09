

public class HeapFragmentationApp {
    public static void main(String args[]) throws Exception {
        int BUF_SIZE = Integer.parseInt(args[0]);
        System.out.println("allocating byte[" + BUF_SIZE + "]");
        byte[] array = new byte[BUF_SIZE];
        System.out.println("array = " + array);
        System.out.println("array.length = " + array.length);
    }
}
