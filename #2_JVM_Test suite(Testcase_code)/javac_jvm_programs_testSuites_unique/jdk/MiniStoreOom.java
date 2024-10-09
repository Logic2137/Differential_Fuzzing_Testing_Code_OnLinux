
import java.util.HashMap;
public class MiniStoreOom {
    private static HashMap<Integer, Byte[]>  store = new HashMap<Integer, Byte[]>();
    public static void main(String... args) throws Exception {
        int size = Integer.valueOf(args[0]);
        int i = 0;
        while (i++ < Integer.MAX_VALUE) {
            store.put(i, new Byte[size]);
        }
    }
}
