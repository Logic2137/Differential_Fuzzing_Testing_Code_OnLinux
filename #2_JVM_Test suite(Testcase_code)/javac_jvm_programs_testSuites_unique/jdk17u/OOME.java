import java.util.LinkedList;

public class OOME {

    @SuppressWarnings("UnusedDeclaration")
    private static Object garbage;

    public static void main(String[] args) {
        int chunkSize = 0x8000;
        LinkedList<int[]> list = new LinkedList<>();
        garbage = list;
        while (true) {
            try {
                list.add(new int[chunkSize]);
            } catch (OutOfMemoryError e) {
                chunkSize >>= 1;
            }
        }
    }
}
