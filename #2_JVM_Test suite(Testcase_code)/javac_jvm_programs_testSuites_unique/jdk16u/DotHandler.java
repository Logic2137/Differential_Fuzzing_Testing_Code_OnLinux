
package custom;

import java.util.concurrent.atomic.AtomicLong;


public class DotHandler extends java.util.logging.ConsoleHandler {

    public static final AtomicLong IDS = new AtomicLong();
    public final long id = IDS.incrementAndGet();
    public DotHandler() {
        System.out.println("DotHandler(" + id + ") created");
        
    }

    @Override
    public void close() {
        System.out.println("DotHandler(" + id + ") closed");
        super.close();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + '(' + id + ')';
    }
}
