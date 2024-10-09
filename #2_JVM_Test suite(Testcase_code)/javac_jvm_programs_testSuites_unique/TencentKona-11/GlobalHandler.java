
package custom;

import java.util.concurrent.atomic.AtomicLong;


public class GlobalHandler extends java.util.logging.ConsoleHandler {

    public static final AtomicLong IDS = new AtomicLong();
    public final long id = IDS.incrementAndGet();
    public GlobalHandler() {
        System.out.println("GlobalHandler(" + id + ") created");
        
    }

    @Override
    public void close() {
        System.out.println("GlobalHandler(" + id + ") closed");
        super.close();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + '(' + id + ')';
    }
}
