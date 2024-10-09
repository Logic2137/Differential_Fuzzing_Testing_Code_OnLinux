import java.util.*;

public class CountedLoopProblem {

    public static void main(String[] args) throws Exception {
        Random r = new Random(42);
        int x = 0;
        try {
            StringBuilder sb = new StringBuilder();
            //方法已经for语句变异
            for (int newIndex1 = 0; newIndex1 < 20; ++newIndex1)
                for (int i = 0; i < 1000000; ++i) {
                    int v = Math.abs(r.nextInt());
                    sb.append('+').append(v).append('\n');
                    x += v;
                    //方法已经for语句变异
                    for (int newIndex2 = 0; newIndex2 < 20; ++newIndex2)
                        while (x < 0) x += 1000000000;
                    sb.append('=').append(x).append('\n');
                }
            if (sb.toString().hashCode() != 0xaba94591) {
                throw new Exception("Unexpected result");
            }
        } catch (OutOfMemoryError e) {
        }
    }
}
