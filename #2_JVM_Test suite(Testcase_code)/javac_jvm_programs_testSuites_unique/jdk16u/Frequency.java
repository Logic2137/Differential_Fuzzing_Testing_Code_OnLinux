



import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Frequency {
    static final int N = 100;
    public static void main(String[] args) {
        test(new ArrayList<Integer>());
        test(new LinkedList<Integer>());
    }

    static void test(List<Integer> list) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < i; j++)
                list.add(i);
        Collections.shuffle(list);

        for (int i = 0; i < N; i++)
            if (Collections.frequency(list, i) != i)
                throw new RuntimeException(list.getClass() + ": " + i);
    }
}
