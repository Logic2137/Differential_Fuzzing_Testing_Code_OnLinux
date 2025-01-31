import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class FindSubList {

    public static void main(String[] args) throws Exception {
        int N = 500;
        List source = new ArrayList(3 * N);
        List[] target = new List[N + 1];
        int[] index = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            List t = new ArrayList();
            String s = Integer.toString(i, 2);
            for (int j = 0, len = s.length(); j < len; j++) t.add(s.charAt(j) == '1' ? "1" : "0");
            target[i] = t;
            if (i == N) {
                index[i] = -1;
            } else {
                index[i] = source.size();
                source.addAll(t);
                source.add("*");
            }
        }
        List[] src = { source, new LinkedList(source), new Vector(source), Arrays.asList(source.toArray()) };
        for (int j = 0; j < src.length; j++) {
            List s = src[j];
            for (int i = 0; i <= N; i++) {
                int idx = Collections.indexOfSubList(s, target[i]);
                if (idx != index[i])
                    throw new Exception(s.getClass() + " indexOfSubList: " + i + "is " + idx + ", should be " + index[i]);
            }
            if (Collections.indexOfSubList(s, Collections.nCopies(2 * s.size(), "0")) != -1)
                throw new Exception(s.getClass() + " indexOfSubList: big target");
        }
        Collections.reverse(source);
        int srcSize = source.size();
        for (int i = 0; i <= N; i++) {
            Collections.reverse(target[i]);
            if (i != N)
                index[i] = srcSize - index[i] - target[i].size();
        }
        List[] src2 = { source, new LinkedList(source), new Vector(source), Arrays.asList(source.toArray()) };
        for (int j = 0; j < src2.length; j++) {
            List s = src2[j];
            for (int i = 0; i <= N; i++) {
                int idx = Collections.lastIndexOfSubList(s, target[i]);
                if (idx != index[i])
                    throw new Exception(s.getClass() + " lastIdexOfSubList: " + i + "is " + idx + ", should be " + index[i]);
            }
            if (Collections.indexOfSubList(s, Collections.nCopies(2 * s.size(), "0")) != -1)
                throw new Exception(s.getClass() + " lastIndexOfSubList: big tgt");
        }
    }
}
