
package gc;

public class InfiniteList {

    InfiniteList next;

    long[] data = new long[50000];

    public static void main(String[] args) throws Exception {
        InfiniteList p, q;
        p = new InfiniteList();
        p.data[p.data.length - 1] = 999;
        try {
            while (p != null) {
                q = new InfiniteList();
                q.next = p;
                p = q;
            }
            throw new Exception("OutOfMemoryError not thrown as expected.");
        } catch (OutOfMemoryError e) {
            return;
        }
    }
}
