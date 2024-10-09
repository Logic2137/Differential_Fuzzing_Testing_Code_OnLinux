
package p.three.internal;

public interface Q {

    default int m() {
        throw new UnsupportedOperationException("Q::m is in a non-exported package");
    }
}
