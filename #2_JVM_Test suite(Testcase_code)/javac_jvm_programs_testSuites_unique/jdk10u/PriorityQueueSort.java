





import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;

public class PriorityQueueSort {

    static class MyComparator implements Comparator<Integer> {
        public int compare(Integer x, Integer y) {
            int i = x.intValue();
            int j = y.intValue();
            if (i < j) return -1;
            if (i > j) return 1;
            return 0;
        }
    }

    public static void main(String[] args) {
        int n = 10000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);

        List<Integer> sorted = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            sorted.add(new Integer(i));
        List<Integer> shuffled = new ArrayList<>(sorted);
        Collections.shuffle(shuffled);

        Queue<Integer> pq = new PriorityQueue<>(n, new MyComparator());
        for (Iterator<Integer> i = shuffled.iterator(); i.hasNext(); )
            pq.add(i.next());

        List<Integer> recons = new ArrayList<>();
        while (!pq.isEmpty())
            recons.add(pq.remove());
        if (!recons.equals(sorted))
            throw new RuntimeException("Sort test failed");

        recons.clear();
        pq = new PriorityQueue<>(shuffled);
        while (!pq.isEmpty())
            recons.add(pq.remove());
        if (!recons.equals(sorted))
            throw new RuntimeException("Sort test failed");

        
        pq = new PriorityQueue<>(shuffled);
        for (Iterator<Integer> i = pq.iterator(); i.hasNext(); )
            if ((i.next().intValue() & 1) == 1)
                i.remove();
        recons.clear();
        while (!pq.isEmpty())
            recons.add(pq.remove());

        for (Iterator<Integer> i = sorted.iterator(); i.hasNext(); )
            if ((i.next().intValue() & 1) == 1)
                i.remove();

        if (!recons.equals(sorted))
            throw new RuntimeException("Iterator remove test failed.");
    }
}
