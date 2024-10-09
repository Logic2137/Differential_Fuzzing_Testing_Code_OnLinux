
package gc.cms;

import static java.lang.ref.Reference.reachabilityFence;

public class DisableResizePLAB {

    public static void main(String[] args) throws Exception {
        Object[] garbage = new Object[1_000];
        for (int i = 0; i < garbage.length; i++) {
            garbage[i] = new byte[0];
        }
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 10_000) {
            reachabilityFence(new byte[1024]);
        }
    }
}
