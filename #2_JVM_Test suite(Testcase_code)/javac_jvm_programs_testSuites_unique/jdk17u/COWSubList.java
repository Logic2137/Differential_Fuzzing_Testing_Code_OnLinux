import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class COWSubList {

    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        expectThrow(() -> list.subList(-1, 5));
        expectThrow(() -> list.subList(0, 6));
        expectThrow(() -> list.subList(4, 3));
        expectThrow(() -> list.subList(0, 5).subList(-1, 5));
        expectThrow(() -> list.subList(0, 5).subList(0, 6));
        expectThrow(() -> list.subList(0, 5).subList(4, 3));
    }

    static void expectThrow(Runnable r) {
        try {
            r.run();
            throw new RuntimeException("Failed: expected IOOBE to be thrown");
        } catch (IndexOutOfBoundsException x) {
        }
    }
}
