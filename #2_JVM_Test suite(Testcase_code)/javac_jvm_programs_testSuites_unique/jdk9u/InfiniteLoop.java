

package jdk.test.lib;

import java.util.Objects;


public class InfiniteLoop implements Runnable {
    private final Runnable target;
    private final long mills;


    
    public InfiniteLoop(Runnable target, long mills) {
        Objects.requireNonNull(target);
        if (mills < 0) {
            throw new IllegalArgumentException("mills < 0");
        }
        this.target = target;
        this.mills = mills;
    }

    @Override
    public void run() {
        try {
            while (true) {
                target.run();
                if (mills > 0) {
                    Thread.sleep(mills);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new Error(e);
        }
    }
}
