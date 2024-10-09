
package metaspace.gc;

public class Fault extends RuntimeException {

    public Fault(String message) {
        super(message);
    }

    public Fault(Throwable t) {
        super(t);
    }
}
