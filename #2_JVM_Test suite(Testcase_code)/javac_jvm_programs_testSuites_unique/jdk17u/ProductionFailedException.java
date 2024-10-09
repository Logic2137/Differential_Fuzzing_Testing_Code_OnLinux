
package jdk.test.lib.jittester;

public class ProductionFailedException extends Exception {

    static final long serialVersionUID = -2325617203741536725L;

    public ProductionFailedException(String msg) {
        super(msg, null, false, false);
    }

    public ProductionFailedException() {
        super(null, null, false, false);
    }
}
