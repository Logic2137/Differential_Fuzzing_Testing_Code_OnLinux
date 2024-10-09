import java.util.*;

public class Test7174363 {

    static Object[] m(Object[] original, int from, int to) {
        return Arrays.copyOfRange(original, from, to, Object[].class);
    }

    static public void main(String[] args) {
        Object[] orig = new Object[10];
        for (int i = 0; i < 20000; i++) {
            try {
                m(orig, 15, 20);
            } catch (ArrayIndexOutOfBoundsException excp) {
            }
        }
    }
}
