import java.lang.reflect.Array;

public class MultiAllocateNullCheck {

    public static void main(String[] args) throws Exception {
        Object x = null;
        try {
            x = Array.newInstance(String.class, new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE });
            System.out.println("Array was created");
        } catch (OutOfMemoryError e) {
            System.out.println("Out of memory occured, which is OK in this case");
        }
    }
}
