



import java.util.ArrayList;
import java.util.List;

public class TestConcurrentSystemGC {
    public static List<char[]> memory;
    public static void main(String[] args) throws Exception {
        memory = new ArrayList<>();
        try {
            while (true) {
                memory.add(new char[1024 * 128]);
                System.gc(); 
            }
        } catch (OutOfMemoryError e) {
            memory = null;
            System.gc();
        }
    }
}
