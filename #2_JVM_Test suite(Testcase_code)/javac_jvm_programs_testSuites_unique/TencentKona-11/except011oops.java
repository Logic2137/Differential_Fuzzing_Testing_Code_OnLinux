

package nsk.stress.except;


public class except011oops {
    static boolean truth = true;

    static {
        if (truth)
            throw new RuntimeException("except011oops");
    }
}
